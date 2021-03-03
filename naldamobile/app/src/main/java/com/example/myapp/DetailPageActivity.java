package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.myapp.adapter.DetailAdapter;
import com.example.myapp.model.DetailModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class DetailPageActivity extends AppCompatActivity {

//    // 실제 서버
    private String ip = "210.114.12.66";
    private int port = 1387;
//    // 내컴퓨터 테스트용
//    private String ip = "192.168.0.60";
//    private int port = 1001;
    private Handler mHandler;
    Socket socket;
    RecyclerView detailRecycler;
    DetailAdapter detailAdapter;
    ArrayList<DetailModel> ditems;
    String member_id = ShopActivity.member_id;
    String uid;
    String data_address, data_shop_name, data_shop_number, data_date, data_time, data_pay_no, data_classify, data_reserve_time,
            data_total_price, data_pay_price, data_pay_method, data_phone, data_msg;
    String data_pdt_name, data_type, data_shot, data_quantity, data_price;

    TextView d_shop_name, d_shop_number, d_date_time, d_pay_no, d_classify, d_reserve_time,
            d_total_price, d_pay_price, d_pay_method, d_phone, d_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_page);
        ditems = new ArrayList<>();
        mHandler = new Handler();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_id);
        d_shop_name = findViewById(R.id.shop_name);
        d_shop_number = findViewById(R.id.d_shop_number);
        d_date_time = findViewById(R.id.d_date_time);
        d_pay_no = findViewById(R.id.d_pay_no);
        d_classify = findViewById(R.id.d_classify);
        d_reserve_time = findViewById(R.id.d_reserve_time);
        d_total_price = findViewById(R.id.d_total_price);
        d_pay_price = findViewById(R.id.d_pay_price);
        d_pay_method = findViewById(R.id.d_pay_method);
        d_phone = findViewById(R.id.d_phone);
        d_msg = findViewById(R.id.d_msg);
        detailRecycler = findViewById(R.id.recycle_detail);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 중간영역의 recycler item
        ditems.add(new DetailModel("아메리카노","ICE","","2잔","2,222원"));
        ditems.add(new DetailModel("블루베리요거트스무디","","","2잔","5,000원"));
        ditems.add(new DetailModel("아메리카노","ICE","","2잔","2222원"));
        ditems.add(new DetailModel("아메리카노","ICE","","2잔","2222원"));

        detailRecycler.setLayoutManager(new LinearLayoutManager(this));
        detailAdapter = new DetailAdapter(detailRecycler, this, ditems);
        detailRecycler.setAdapter(detailAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
                                +"MS08"
                                +"00"
                                +"D01="+""
                                +"D02="+""
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
    }

    class msgUpdate implements Runnable {
        private String msg;
        int idx1, idx2, idx3, idx4, idx5, idx6, idx7, idx8, idx9, idx10, idx11, idx12, idx13, idx14, idx15, idx16, idxEnd;
        public msgUpdate(String msg) {
            this.msg = msg;
        }
        public void run() {
            // 받은 msg를 파싱처리해서 변수에 저장
            idx1 = msg.indexOf("D01=");  //+4
            idx2 = msg.indexOf("D02=");
            idx3 = msg.indexOf("D03=");
            idx4 = msg.indexOf("D04=");
            idx5 = msg.indexOf("D05=");
            idx6 = msg.indexOf("D06=");
            idx7 = msg.indexOf("D07=");
            idx8 = msg.indexOf("D08=");
            idxEnd = msg.indexOf("ETX");
            // 반복문용 카운트
//            record_count = Integer.parseInt(msg.substring(idx1+4, idx2));
//            d_date = msg.substring(idx2+4, idx3);
//            d_time = msg.substring(idx3+4, idx4);
//            d_shop_name = msg.substring(idx4+4, idx5);
//            d_pdt_name = msg.substring(idx5+4, idx6);
//            d_price = msg.substring(idx6+4, idx7);
//            d_classify = (msg.substring(idx7+4, idx8));
//            d_reserve_time = reserve_time(msg.substring(idx8+4, idx8+8));

        }
        // 예약시간이 00:00으로 들어오면 빈값으로 반환, 값이 들어오면 괄호처리
        public String reserve_time(String d_reserve_time){
            if(d_reserve_time.equals("00:00")){
                return "";
            }
            return "( "+d_reserve_time+" )";
        }
    }
}