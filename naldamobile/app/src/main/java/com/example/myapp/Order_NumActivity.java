package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.ProductFragment.FragmentParent1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Order_NumActivity extends AppCompatActivity {

    Socket socket;
    //     실제 서버
    private String ip = "210.114.12.66";
    private int port = 1387;
    // 내컴퓨터 테스트용
//    private String ip = "192.168.0.60";
//    private int port = 1001;

    private Handler mHandler;
    Button goHome;
    String menu_count, type, shop_name, pdt_name, payNo, reserve_time;
    TextView chatTv_request;

    int choice, amount, total_price;
    public String classify = "1";
    Calendar cal = new GregorianCalendar(Locale.KOREA);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH)+1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order__num);

        mHandler = new Handler();
        getData();

        goHome = (Button) findViewById(R.id.goHome);
        chatTv_request = (TextView) findViewById(R.id.chatTv_request);

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectThread th = new ConnectThread();
                th.start();
            }
        });
    }

    private void getData(){
        if(getIntent().hasExtra("shop_name")){
            shop_name = getIntent().getStringExtra("shop_name");
            menu_count = getIntent().getStringExtra("menu_count");
            pdt_name = getIntent().getStringExtra("pdt_name");
            type = getIntent().getStringExtra("type");
            choice = getIntent().getIntExtra("choice",0);
            amount = getIntent().getIntExtra("amount",10);
            total_price = getIntent().getIntExtra("total_price",99);
            reserve_time = getIntent().getStringExtra("reserve_time");
            payNo = getIntent().getStringExtra("payNo");
            classify = getIntent().getStringExtra("classify");
        }else{

        }
    }

    class ConnectThread extends Thread{
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                // 보낼 메시지
                String sndMsg =
                        "STX"
                                +"MS06"
                                +"00"
                                +year+"-"+month(month)+"-"+day(day)
                                +hour(hour)+":"+min(min)+":"+sec(sec)
                                +"ID="+ShopActivity.member_id
                                +"SHOP="+"군포점"
                                +"COUNT="+"01"
                                +"D01="+"카푸치노"
                                +"D02="+"1"
                                +"D03="+"01"
                                +"D04="+"00"
                                +"D05="+"00051"
                                +"PRICE="+"000051"
                                +"PAYNO="+"imp_921853162590"
                                +"CLS="+classify
                                +"MSG="+msg_func(classify)
                                +"ETX";
                Log.d("=============", sndMsg);

                // 전송
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-16le")), true);
                out.println(sndMsg);

                // 수신
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-16le"));
                String read = input.readLine();
                mHandler.post(new msgUpdate(read));
                // 테스트용 : 화면출력
                Log.d("=============", read);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public String msg_func(String classify){
            if(classify.equals("2")){
                return "14:00";
            }else{
                return "";
            }
        }
        private String month(int month) {
            String monthS = "";
            if(month<10){monthS = "0"+month;}
            else{monthS = ""+month;}
            return monthS;
        }
        private String day(int day) {
            String dayS = "";
            if(day<10){dayS = "0"+day;}
            else{dayS = ""+day;}
            return dayS;
        }
        private String hour(int hour) {
            String hourS = "";
            if(hour<10){hourS = "0"+hour;}
            else{hourS = ""+hour;}
            return hourS;
        }
        private String min(int min) {
            String minS = "";
            if(min<10){minS = "0"+min;}
            else{minS = ""+min;}
            return minS;
        }
        private String sec(int sec) {
            String secS = "";
            if(sec<10){secS = "0"+sec;}
            else{secS = ""+sec;}
            return secS;
        }
    }

    class msgUpdate implements Runnable {
        private String msg;
        public msgUpdate(String str) {
            this.msg = str;
        }
        public void run() {
            chatTv_request.setText(chatTv_request.getText().toString() + msg + "\n");
            if(chatTv_request.getText().toString().contains("STXMS0600")){
                finish();
            }else if(chatTv_request.getText().toString().contains("STXMS0601")){
                finish();
            }else{
                finish();
            }
        }
    }
}