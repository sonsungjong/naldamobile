package com.example.myapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapp.ProductFragment.FragmentParent1;
import com.example.myapp.ProductFragment.FragmentParent2;
import com.example.myapp.ProductFragment.FragmentParent3;
import com.example.myapp.ProductFragment.FragmentParent4;
import com.example.myapp.ProductFragment.FragmentParent5;
import com.example.myapp.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;

public class ProductActivity extends AppCompatActivity{

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView toolbarProductTitle;

    public static String member_id;
    public static String shop_name;
    public static String reserve_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product);
//      인텐트로부터 데이터 받아오기
        getData();
        setData();
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
            member_id = getIntent().getStringExtra("member_id");
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

}