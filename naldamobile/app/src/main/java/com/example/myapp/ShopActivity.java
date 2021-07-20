package com.example.myapp;


import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.myapp.adapter.ViewPagerAdapter;
import com.example.myapp.shopFragment.FiveFragment;
import com.example.myapp.shopFragment.FourFragment;
import com.example.myapp.shopFragment.OneFragment;
import com.example.myapp.shopFragment.ThreeFragment;
import com.example.myapp.shopFragment.TwoFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.ini4j.Ini;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ShopActivity extends AppCompatActivity {

    public static String member_id;
    public static String member_phone;
    public static String member_email;
    public static String member_name;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shop);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_id));
        getSupportActionBar().setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바 메뉴버튼 생성
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_hamburger); // 메뉴 버튼 모양 설정

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getData();

        viewPager = (ViewPager) findViewById(R.id.viewPager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new OneFragment(), "서울특별시");
        adapter.addFragment(new TwoFragment(), "경기도 전체");
        adapter.addFragment(new ThreeFragment(), "인천광역시");
        adapter.addFragment(new FourFragment(), "부산광역시");
        adapter.addFragment(new FiveFragment(), "제주특별자치도");

        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        tabLayout.setupWithViewPager(viewPager);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent = null;
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.item_home){
                }
                else if(id == R.id.item_history){
                    intent = new Intent(ShopActivity.this, HistoryActivity.class);
                }
                startActivity(intent);
//                finish();
                return true;
            }
        });
        IntroActivity.intro_Ll.setBackgroundResource(R.drawable.bye); //아웃트로
    }   // onCreate()

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        IntroActivity.intro_Ll.setBackgroundResource(R.drawable.bye); //아웃트로
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IntroActivity intro = (IntroActivity) IntroActivity.intro_activity; // 객체를 만든다.
        intro.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //int id = item.getItemId();
        Intent intent = null;

        switch (item.getItemId()) {
            // 햄버거 메뉴 눌렀을 때 펼쳐지게함
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void getData(){
        if(getIntent().hasExtra("member_id")){
            member_id = getIntent().getStringExtra("member_id");
            member_name = getIntent().getStringExtra("member_name");
            member_email = getIntent().getStringExtra("member_email");
            member_phone = getIntent().getStringExtra("member_phone");
        }else{
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }
    }
    private void setData(){

    }


}