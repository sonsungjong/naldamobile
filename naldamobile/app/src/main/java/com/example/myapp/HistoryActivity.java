package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class HistoryActivity extends AppCompatActivity {

    public static Context mContext;
    int idx1, idx2, idx3, idx4, idx5, idx6, idx7, idx8, idx9, idx10, idx11, idx12, idx13, idx14, idx15, idx16, idxEnd;
    String response_detail_information;

    RecyclerView historyRecycler;
    HistoryAdapter historyAdapter;
    Toolbar toolbar;
    Button search_today, search_month;
    public ArrayList<HistoryModel> items;
    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);     // 한국날짜
    SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);       // 한국시간
    SimpleDateFormat dfTime2 = new SimpleDateFormat("HH:mm", Locale.KOREA);     // 한국 분까지만
    public String date_today = dfDate.format(new Date());   // 한국 현재날짜
    public String time_today = dfTime.format(new Date());   // 한국 현재시간
    public String time_today2 = dfTime2.format(new Date());   // 한국 현재시간(분까지만)
    public String from_date = date_today;        // 한국 현재날짜에서 -3달
    public String to_date = date_today;       //한국 현재날짜 받아오기
    public int recordCount;
    public ArrayList<String> salesCode;
    public ArrayList<String> salesDate;
    public ArrayList<String> salesTime;
    public ArrayList<String> salesShopName;
    public ArrayList<String> salesResult;
    public ArrayList<String> salesPdtName;
    public ArrayList<String> salesPrice;
    public ArrayList<String> salesClassify;
    public ArrayList<String> salesReserveTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_history);
        mContext = this;

        items = new ArrayList<>();
        salesCode = new ArrayList<>();
        salesDate = new ArrayList<>();
        salesTime = new ArrayList<>();
        salesShopName = new ArrayList<>();
        salesResult = new ArrayList<>();
        salesPdtName = new ArrayList<>();
        salesPrice = new ArrayList<>();
        salesClassify = new ArrayList<>();
        salesReserveTime = new ArrayList<>();
        recordCount = 0;
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_id));

        toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        search_today = (Button) findViewById(R.id.search_today);
        search_month = (Button) findViewById(R.id.search_month);
        historyRecycler = findViewById(R.id.today_item);

        historyRecycler.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter(historyRecycler, this, items);



        // 1일치
        search_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존에 있던 list값 삭제
                items.clear();
                salesCode.clear();
                salesDate.clear();
                salesTime.clear();
                salesShopName.clear();
                salesResult.clear();
                salesPdtName.clear();
                salesPrice.clear();
                salesClassify.clear();
                salesReserveTime.clear();

                IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                        "STX"
                                +"MS07"
                                +"00"
                                +"ID="+ShopActivity.member_id
                                +"D01="+from_date       // DB에서 time=00:00:00으로 찾기
                                +"D02="+to_date         // DB에서 time=23:59:59로 찾기
                                +"ETX"
                );
                mt.start();

                // 내역이 1개 이상일 때
                if(recordCount > 0){
                    for(int i=0; i<recordCount; i++){
                        items.add(new HistoryModel(salesShopName.get(i), salesResult.get(i), salesDate.get(i)+", "+salesTime.get(i), salesPdtName.get(i), salesPrice.get(i), salesClassify.get(i), salesReserveTime.get(i)));
                    }
                }
                else{
                    Toast.makeText(HistoryActivity.this, "당일 결제내역이 없습니다", Toast.LENGTH_SHORT).show();
                }

                // 어댑터에 초기화
                historyRecycler.setAdapter(historyAdapter);

                search_today.setBackgroundResource(R.drawable.click_today);
                search_month.setBackgroundResource(R.drawable.click_none);
                search_today.setTextColor(Color.parseColor("#ffffff"));
                search_month.setTextColor(Color.parseColor("#000000"));
            }
        });

        // 3달치
        search_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                salesCode.clear();
                salesDate.clear();
                salesTime.clear();
                salesShopName.clear();
                salesResult.clear();
                salesPdtName.clear();
                salesPrice.clear();
                salesClassify.clear();
                salesReserveTime.clear();

                Date dDate = new Date();
                dDate = new Date(dDate.getTime()+(1000*60*60*24*90*-1));
                from_date = dfDate.format(dDate);

                IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                        "STX"
                                +"MS07"
                                +"00"
                                +"ID="+ShopActivity.member_id
                                +"D01="+from_date       // DB에서 time=00:00:00으로 찾기
                                +"D02="+to_date         // DB에서 time=23:59:59로 찾기
                                +"ETX"
                );
                mt.start();

                historyRecycler.setAdapter(historyAdapter);

                search_today.setBackgroundResource(R.drawable.click_none);
                search_month.setBackgroundResource(R.drawable.click_today);
                search_today.setTextColor(Color.parseColor("#000000"));
                search_month.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    } // onCreate()

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

    // pay_result = "결제 완료", d_pdt_name = "아메리카노", d_shop_name = "군포점", d_date = "2020-12-31", d_time = "09:40", d_price = "2000원", d_classify = "실시간 주문", d_reserve_time = "00:00";

    // 예약시간이 00:00으로 들어오면 빈값으로 리턴
    public String reserve_time(String d_reserve_time){
        if(d_reserve_time.equals("00:00")){
            return "";
        }
        return d_reserve_time;
    }
    public void printHistory(String read){
        if(read.contains("STXMS0700")){
            // 받은 msg를 파싱처리해서 변수에 저장
            idx1 = read.indexOf("COUNT=");
            idxEnd = read.indexOf("ETX");
            // 반복문용 카운트
            recordCount = Integer.parseInt(read.substring(idx1+6, idx1+8));
            if(recordCount > 0){
                for(int j=0; j<recordCount; j++){
                    salesCode.add(read.substring(read.indexOf("D"+hundredStr(j*9+1)+"=")+5, read.indexOf("D"+hundredStr(j*9+1)+"=")));
                    salesDate.add(read.substring(read.indexOf("D"+hundredStr(j*9+2)+"=")+5, read.indexOf("D"+hundredStr(j*9+2)+"=")));
                    salesTime.add(read.substring(read.indexOf("D"+hundredStr(j*9+3)+"=")+5, read.indexOf("D"+hundredStr(j*9+3)+"=")));
                    salesShopName.add(read.substring(read.indexOf("D"+hundredStr(j*9+4)+"=")+5, read.indexOf("D"+hundredStr(j*9+4)+"=")));
                    salesPdtName.add(read.substring(read.indexOf("D"+hundredStr(j*9+5)+"=")+5, read.indexOf("D"+hundredStr(j*9+5)+"=")));
                    salesPrice.add(read.substring(read.indexOf("D"+hundredStr(j*9+6)+"=")+5, read.indexOf("D"+hundredStr(j*9+6)+"=")));
                    salesClassify.add(read.substring(read.indexOf("D"+hundredStr(j*9+7)+"=")+5, read.indexOf("D"+hundredStr(j*9+8)+"=")));
                    salesReserveTime.add(read.substring(read.indexOf("D"+hundredStr(j*9+8)+"=")+5, read.indexOf("D"+hundredStr(j*9+9)+"=")));
                    salesResult.add(salesResult(Integer.parseInt(read.substring(read.indexOf("D"+hundredStr(j*9+9)+"=")+5, read.indexOf("D"+hundredStr(j*9+9)+"=")+6))));
                }
            }
        }else{
            // 네트워크 환경이 원활하지 않습니다.
        }
    }

    String hundredStr(int num){
        if(num <= 0){
            return "000";
        }else if(num < 10){
            return "00"+num;
        }else if(num < 100){
            return "0"+num;
        }
        return ""+num;
    }

    String salesResult(int result){
        if(result == 1){
            return "결제완료";
        }else{
            return "결제취소";
        }
    }
}