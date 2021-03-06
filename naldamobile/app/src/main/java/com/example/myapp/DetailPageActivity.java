package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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

    public static Context mContext;
    RecyclerView detailRecycler;
    DetailAdapter detailAdapter;
    ArrayList<DetailModel> ditems;
    String uid;
    String data_address, data_shop_name, data_shop_number, data_date_time, data_pay_no, data_classify, data_reserve_time,
            data_total_price, data_pay_price, data_pay_method, data_phone, data_msg;
    String data_pdt_name, data_type, data_shot, data_quantity, data_price;

    TextView d_shop_name, d_shop_number, d_date_time, d_pay_no, d_classify, d_reserve_time,
            d_total_price, d_pay_price, d_pay_method, d_phone, d_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_page);
        mContext = this;
        getData();
        ditems = new ArrayList<>();

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ???????????? ??????, ???????????? true??? ?????? ???????????? ??????

    }

    @Override
    protected void onStart() {
        super.onStart();
        // ??????????????? recycler item
        ditems.add(new DetailModel("???????????????","ICE","","2???","2,222???"));
        ditems.add(new DetailModel("??????????????????????????????","","","2???","5,000???"));
        ditems.add(new DetailModel("???????????????","ICE","","2???","2222???"));
        ditems.add(new DetailModel("???????????????","ICE","","2???","2222???"));

        detailRecycler.setLayoutManager(new LinearLayoutManager(this));
        detailAdapter = new DetailAdapter(detailRecycler, this, ditems);
        detailRecycler.setAdapter(detailAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar??? back??? ????????? ??? ??????
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

//        public void run() {
//            // ?????? msg??? ?????????????????? ????????? ??????
//            idx1 = msg.indexOf("D01=");  //+4
//            idx2 = msg.indexOf("D02=");
//            idx3 = msg.indexOf("D03=");
//            idx4 = msg.indexOf("D04=");
//            idx5 = msg.indexOf("D05=");
//            idx6 = msg.indexOf("D06=");
//            idx7 = msg.indexOf("D07=");
//            idx8 = msg.indexOf("D08=");
//            idxEnd = msg.indexOf("ETX");
            // ???????????? ?????????
//            record_count = Integer.parseInt(msg.substring(idx1+4, idx2));
//            d_date = msg.substring(idx2+4, idx3);
//            d_time = msg.substring(idx3+4, idx4);
//            d_shop_name = msg.substring(idx4+4, idx5);
//            d_pdt_name = msg.substring(idx5+4, idx6);
//            d_price = msg.substring(idx6+4, idx7);
//            d_classify = (msg.substring(idx7+4, idx8));
//            d_reserve_time = reserve_time(msg.substring(idx8+4, idx8+8));

//        }
    // ??????????????? 00:00?????? ???????????? ???????????? ??????, ?????? ???????????? ????????????
    public String reserve_time(String d_reserve_time){
        if(d_reserve_time.equals("00:00")){
            return "";
        }
        return "( "+d_reserve_time+" )";
    }
    // MS08 ????????????
    public String requestDetailInfo(String read){
        if(read.contains("STXMS0800")){
            return read;
        }
        return "";
    }
    
    
    private void getData(){
        if(getIntent().hasExtra("h_date_time")){
            data_shop_name = getIntent().getStringExtra("h_shop_name");
            // data_state = getIntent().getStringExtra("h_state");
            data_date_time = getIntent().getStringExtra("h_date_time");
            data_pay_no = getIntent().getStringExtra("h_pay_price");
            data_classify = getIntent().getStringExtra("h_classify");
            data_reserve_time = getIntent().getStringExtra("h_reserve_time");

        }else{
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }
    }
}