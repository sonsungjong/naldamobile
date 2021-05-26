package com.example.myapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.ProductFragment.FragmentParent1;
import com.example.myapp.ProductFragment.FragmentParent2;
import com.example.myapp.ProductFragment.FragmentParent3;
import com.example.myapp.ProductFragment.FragmentParent4;
import com.example.myapp.ProductFragment.FragmentParent5;
import com.example.myapp.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ProductActivity extends AppCompatActivity{

    public static String shop_name, reserve_text;   // 가게이름, 예약가능여부
    public static Context mContext;
    public static int cartMenuCount = 0, cartFlag = 0;
    public static List<String> cartKey;     // 초이스+이름+샷
    public static List<String> cartPdtName; // 이름
    public static List<Integer> cartChoice; // normal,hot,ice : 0, 1, 2
    public static List<Integer> cartAmount; // 2
    public static List<Integer> cartShot;   // 2
    public static List<Integer> cartPay;   // 8000 개당 가격 (amount만 곱해줘야함, type과 shot은 적용된 개당 가격)
    public static List<Integer> cartImg;

    String menu_count, pdt_name, type, choice, amount, total_price, reserve_time, cart_msg, pdtInfo;
    int classify;   // 모바일주문/모바일예약 구분 플래그
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView toolbarProductTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product);
        mContext = this;
//      인텐트로부터 데이터 받아오기
        getData();
        cartPdtName = new ArrayList<>();
        cartChoice = new ArrayList<>();
        cartAmount = new ArrayList<>();
        cartShot = new ArrayList<>();
        cartPay = new ArrayList<>();
        cartKey = new ArrayList<>();
        cartImg = new ArrayList<>();
        cartFlag = 0;
        cartMenuCount = 0;
//        setData();
        toolbar = (Toolbar) findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarProductTitle = (TextView) findViewById(R.id.toolbar_product_title);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

//      지점이름 셋팅
        toolbarProductTitle.setText(shop_name);
        // 프레그먼트 추가
        addFragment();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void addFragment() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentParent1(), "커피 메뉴");
        adapter.addFragment(new FragmentParent2(), "라떼 메뉴");
        adapter.addFragment(new FragmentParent3(), "디저트 메뉴");
        adapter.addFragment(new FragmentParent4(), "에이드 메뉴");
        adapter.addFragment(new FragmentParent5(), "음료 차 메뉴");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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



    private void getData(){
        if(getIntent().hasExtra("shop_name")){
            shop_name = getIntent().getStringExtra("shop_name");
            reserve_text = getIntent().getStringExtra("reserve_text");
        }else{
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }
    }
    private void setData(){

    }

    // 하단바 없애기
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void orderSuccess(String read){
        int idx1, idx2, idx3, idx4, idx5, idx6, idx7, idx8, idxEnd;
        Log.d("orderSuccess(): ",read);

        if(read.contains("STXMS0400")){
            // 주문확인 팝업으로 이동
//                Intent intent1 = new Intent(getApplicationContext(), Order_PopupActivity.class);
//                startActivityForResult(intent1, 1);

            // 상품종류수 01
            idx1 = read.indexOf("COUNT=");
            // 상품이름 아메리카노
            idx2 = read.indexOf("D01=");
            // 상품 타입 HOT
            idx3 = read.indexOf("D02=");
            // 상품 수량 01
            idx4 = read.indexOf("D03=");
            // 샷추가 00
            idx5 = read.indexOf("D04=");
            // 해당상품 합계 가격 00000
            idx6 = read.indexOf("D05=");
            // 장바구니 전체 가격
            idx7 = read.indexOf("PRICE=");
            // 고객요청 메시지
            idx8 = read.indexOf("MSG=");
            //끝부분
            idxEnd = read.indexOf("ETX");
            menu_count = read.substring(idx1 +6, idx2);
            pdtInfo = read.substring(idx2, idx7);
            /*pdt_name = read.substring(idx2 +4, idx3);
            type = read.substring(idx3 +4, idx4);
            choice = (read.substring(idx5 +4, idx6));
            amount = read.substring(idx4 +4, idx5);*/
//            total_price = read.substring(idx6 +4, idx7);
            total_price = read.substring(idx7+6, idx8);
            cart_msg = "";
            classify = 1;

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("shop_name",ProductActivity.shop_name);
            intent.putExtra("menu_count",menu_count);
            intent.putExtra("pdtInfo", pdtInfo);
            intent.putExtra("total_price",Integer.parseInt(total_price));
            intent.putExtra("reserve_time","");
            intent.putExtra("cart_msg", cart_msg);
            intent.putExtra("classify",classify);
            startActivity(intent);
            finish();
        }
        else if(read.contains("STXMS0401")){
//            Toast.makeText(mContext, "주문 실패", Toast.LENGTH_LONG).show();
        }
        else if(read.contains("STXMS0402")){
//            Toast.makeText(mContext, "해당 제품이 품절되었습니다", Toast.LENGTH_LONG).show();
        }
        else if(read.equals(null) || read.equals("")){
//            Toast.makeText(mContext, "빈값", Toast.LENGTH_LONG).show();
        }
        else{
//            Toast.makeText(mContext, "서버 오류발생", Toast.LENGTH_LONG).show();
        }
    }

    public void reserveSuccess(String read){
        int idx1, idx2, idx3, idx4, idx5, idx6, idx7, idx8, idx9, idx10, idxEnd;
        Log.d("reserveSuccess(): ",read);

        if(read.contains("STXMS0500")){
            // 상품종류수 01
            idx1 = read.indexOf("COUNT=");
            // 상품이름 아메리카노
            idx2 = read.indexOf("D01=");
            // 상품 타입 HOT
            idx3 = read.indexOf("D02=");
            // 상품 수량 01
            idx4 = read.indexOf("D03=");
            // 샷추가 00
            idx5 = read.indexOf("D04=");
            // 해당상품 합계 가격 00000
            idx6 = read.indexOf("D05=");
            // 장바구니 전체 가격
            idx7 = read.indexOf("PRICE=");
            // 고객요청 메시지
            idx8 = read.indexOf("MSG=");
            //끝부분
            idxEnd = read.indexOf("ETX");
            menu_count = read.substring(idx1 +6, idx2);
            pdtInfo = read.substring(idx2, idx7);
            /*pdt_name = read.substring(idx2 +4, idx3);
            type = read.substring(idx3 +4, idx4);
            choice = (read.substring(idx5 +4, idx6));
            amount = read.substring(idx4 +4, idx5);*/
//            total_price = read.substring(idx7, idx8);
            total_price = read.substring(idx6 +4, idx7);
            cart_msg = "";
            reserve_time = read.substring(22, 27);
            classify = 2;

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("shop_name",ProductActivity.shop_name);
            intent.putExtra("menu_count",menu_count);
            intent.putExtra("pdtInfo",pdtInfo);
            intent.putExtra("total_price",Integer.parseInt(total_price));
            intent.putExtra("reserve_time",reserve_time);
            intent.putExtra("cart_msg", cart_msg);
            intent.putExtra("classify",classify);
            startActivity(intent);
            finish();
        }
        else if(read.contains("STXMS0501")){
//            Toast.makeText(mContext, "예약 실패", Toast.LENGTH_LONG).show();
        }
        else if(read.contains("STXMS0502")){
//            Toast.makeText(mContext, "같은 시간에 예약이 존재합니다", Toast.LENGTH_LONG).show();
        }
        else if(read.equals(null) || read.equals("")){
//            Toast.makeText(mContext, "빈값", Toast.LENGTH_LONG).show();
        }
        else{
//            Toast.makeText(mContext, "서버 오류발생", Toast.LENGTH_LONG).show();
        }
    }
}