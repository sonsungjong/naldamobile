package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.adapter.HistoryAdapter;
import com.example.myapp.adapter.ViewPagerAdapter;
import com.example.myapp.model.HistoryModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class HistoryActivity extends AppCompatActivity {

    // 실제 서버
    private String ip = "210.114.12.66";
    private int port = 1387;
    // 내컴퓨터 테스트용
//    private String ip = "192.168.0.60";
//    private int port = 1001;

    private Handler mHandler;
    Socket socket;

    RecyclerView historyRecycler;
    HistoryAdapter historyAdapter;
    Toolbar toolbar;
    Button search_today, search_month;
    public ArrayList<HistoryModel> items;
    public int record_count = 1;
    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);     // 한국날짜
    SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);       // 한국시간
    SimpleDateFormat dfTime2 = new SimpleDateFormat("HH:mm", Locale.KOREA);     // 한국 분까지만
    public String date_today = dfDate.format(new Date());   // 한국 현재날짜
    public String time_today = dfTime.format(new Date());   // 한국 현재시간
    public String time_today2 = dfTime2.format(new Date());   // 한국 현재시간(분까지만)
    public String pay_result = "결제 완료", d_pdt_name = "쿠키", d_shop_name = "군포점", d_date = date_today, d_time = time_today2, d_price = "2000원", d_classify = "실시간 주문", d_reserve_time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_history);

        mHandler = new Handler();

        items = new ArrayList<>();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_id));

        toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        search_today = (Button) findViewById(R.id.search_today);
        search_month = (Button) findViewById(R.id.search_month);
        historyRecycler = findViewById(R.id.today_item);

        // 잠시막음
//        ConnectThread th = new ConnectThread();
//        th.start();

    } // onCreate()

    @Override
    protected void onStart() {
        super.onStart();

        historyRecycler.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter(historyRecycler, this, items);

        search_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존에 있던 list값 삭제
                for(int i=items.size()-1; i>=0; i--){
                    items.remove(i);
                }
                // ArrayList에 값 추가
                for(int i=0; i<record_count; i++) {
                    // 조건문 추가하기 : 현재 날짜와 같지 않다면 break;
                    if(!d_date.equals(date_today)){break;}
                    items.add(new HistoryModel(d_shop_name, pay_result, d_date + ", " + d_time, d_pdt_name, d_price, d_classify, d_reserve_time));
                }

                // 테스트용 임시값
                items.add(new HistoryModel("군포점", "결제 완료", d_date + ", " + "11:42", "카페라떼", d_price, d_classify, d_reserve_time));
                items.add(new HistoryModel("군포점", "결제 완료", d_date + ", " + "09:33", "아메리카노", d_price, "예약", "16:30"));

                // 어댑터에 초기화
                historyRecycler.setAdapter(historyAdapter);
            }
        });

        search_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=items.size()-1; i>=0; i--){
                    items.remove(i);
                }
                // ArrayList에 값 추가
                for(int i=0; i<record_count; i++) {
                    items.add(new HistoryModel(d_shop_name, pay_result, d_date + ", " + d_time, d_pdt_name, d_price, d_classify, d_reserve_time));
                }

                // 테스트용 임시값
                items.add(new HistoryModel("군포점", "결제 완료", "2021-01-20" + ", " + "13:20", "카라멜 마끼아또", d_price, d_classify, d_reserve_time));
                items.add(new HistoryModel("군포점", "결제 완료", "2021-01-21" + ", " + "15:20", "카푸치노", d_price, "예약", "14:00"));
                items.add(new HistoryModel("군포점", "결제 완료", d_date + ", " + "11:42", "카페라떼", d_price, d_classify, d_reserve_time));
                items.add(new HistoryModel("군포점", "결제 완료", d_date + ", " + "09:33", "아메리카노", d_price, "예약", "18:30"));

                historyRecycler.setAdapter(historyAdapter);
            }
        });
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

    public void detail(View view){
        Intent intent = new Intent(this, DetailPageActivity.class);
        startActivity(intent);
    }

    // today
    class ConnectThread extends Thread{
        public String from_date = date_today;        // 한국 현재날짜에서 -3달
        public String to_date = date_today;       //한국 현재날짜 받아오기
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                // 보낼 메시지
                String sndMsg =
                        "STX"
                                +"MS07"
                                +"0128"
                                +"00"
                                +"D01="+from_date
                                +"D02="+to_date
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
            if(msg.contains("STXMS0700")) {
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
                record_count = Integer.parseInt(msg.substring(idx1 + 4, idx2));
                d_date = msg.substring(idx2 + 4, idx3);
                d_time = msg.substring(idx3 + 4, idx4);
                d_shop_name = msg.substring(idx4 + 4, idx5);
                d_pdt_name = msg.substring(idx5 + 4, idx6);
                d_price = msg.substring(idx6 + 4, idx7);
                d_classify = (msg.substring(idx7 + 4, idx8));
                d_reserve_time = reserve_time(msg.substring(idx8 + 4, idx8 + 8));
                pay_result = "결제 완료";
            }else{
                Toast.makeText(HistoryActivity.this, "서버와 통신이 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            
// pay_result = "결제 완료", d_pdt_name = "아메리카노", d_shop_name = "군포점", d_date = "2020-12-31", d_time = "09:40", d_price = "2000원", d_classify = "실시간 주문", d_reserve_time = "00:00";
        }
        // 예약시간이 00:00으로 들어오면 빈값으로 리턴
        public String reserve_time(String d_reserve_time){
            if(d_reserve_time.equals("00:00")){
                return "";
            }
            return d_reserve_time;
        }
    }
}