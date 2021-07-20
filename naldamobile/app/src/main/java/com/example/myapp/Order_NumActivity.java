package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
// 결제완료 후 대기번호 안내 혹은 예약시간 안내 페이지
public class Order_NumActivity extends AppCompatActivity {

//    public static Context mContext;

    Button goHome;
    String response;
    TextView chatTv_response;

    int choice, amount, total_price, classify;
    Calendar cal = new GregorianCalendar(Locale.KOREA);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH)+1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY)+9;
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order__num);
//        mContext = this;
        getData();

        goHome = (Button) findViewById(R.id.goHome);
        chatTv_response = (TextView) findViewById(R.id.chatTv_request);

        chatTv_response.setText(response);

        // 처음으로 버튼 눌렀을때
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                        "STX"+"MS06"+"00"
                        +year+"-"+month(month)+"-"+day(day)
                        +hour(hour)+":"+min(min)+":"+sec(sec)
                        +"ID="+ShopActivity.member_id
                        +"SHOP="+shop_name
                        +"COUNT="+menu_count
                        +"D01="+pdt_name
                        +"D02="+type
                        +"D03="+intToTwoString(amount)
                        +"D04="+stringChoice(choice)
                        +"D05="+string_total_price(total_price)
                        +"PRICE="+final_string_total_price(total_price)
                        +"PAYNO="+payNo
                        +"CLS="+classify
                        +"MSG="+reserve_time
                        +"ETX"
                );
                mt.start();*/
            }
        });
    }   // onCreate

    private void getData(){
        /*if(getIntent().hasExtra("shop_name")){
        shop_name = getIntent().getStringExtra("shop_name");
        menu_count = getIntent().getStringExtra("menu_count");
        pdt_name = getIntent().getStringExtra("pdt_name");
        type = getIntent().getStringExtra("type");
        choice = getIntent().getIntExtra("choice",0);
        amount = getIntent().getIntExtra("amount",10);
        total_price = getIntent().getIntExtra("total_price",99);
        payNo = getIntent().getStringExtra("payNo");
        classify = getIntent().getIntExtra("classify", 1);
        reserve_time = getIntent().getStringExtra("reserve_time");
    }*/
        if(getIntent().hasExtra("MS06MSG")){
            response = getIntent().getStringExtra("MS06MSG");
        }
        else{
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
    }
}

    /*private String final_string_total_price(int total_price) {
        if(total_price <1){
            return "000000";
        }else if(total_price < 10){
            return "00000"+total_price;
        }else if(total_price<100){
            return "0000"+total_price;
        }else if (total_price < 1000){
            return "000"+total_price;
        }else if (total_price < 10000){
            return "00"+total_price;
        }else if(total_price < 100000){
            return "0"+total_price;
        }else{
            return ""+total_price;
        }
    }

    private String string_total_price(int total_price) {
        if(total_price <1){
            return "00000";
        }else if(total_price < 10){
            return "0000"+total_price;
        }else if(total_price<100){
            return "000"+total_price;
        }else if (total_price < 1000){
            return "00"+total_price;
        }else if (total_price < 10000){
            return "0"+total_price;
        }else{
            return ""+total_price;
        }
    }

    private String stringChoice(int choice) {
        if(choice<10){
            return "0"+choice;
        }
        else{
            return ""+choice;
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
    private String intToTwoString(int num){
        String str = "";
        if(num<10){str = "0"+num;}
        else{str = ""+num;}
        return str;
    }

    public void backToShop(String read){
        if(read.contains("STXMS0600")){
            finish();
        }
        else if(read.contains("STXMS0601")){
            chatTv_request.setText("네트워크 오류, 지점에 문의해주세요");
        }
        else{
            chatTv_request.setText("네트워크 오류, 지점에 문의해주세요");
        }
    }*/
}