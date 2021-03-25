package com.example.myapp.ProductFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapp.BasketActivity;
import com.example.myapp.IntroActivity;
import com.example.myapp.PaymentActivity;
import com.example.myapp.ProductActivity;
import com.example.myapp.R;
import com.example.myapp.adapter.ProductImageAdapter;
import com.example.myapp.model.ProductImageModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentParent3 extends Fragment {

    private Handler mHandler;

    ViewPager viewPager;
    ProductImageAdapter adapter;
    List<ProductImageModel> models;

    TextView chatTV_f3;
    ImageButton reserveNow_f3, orderNow_f3, addCart_f3, cancel_f3;

    TextView amountText_f3, price1_f3;
    int amountCnt = 1;
    String menu_count = "01";
    String pdt_name = "플레인요거트";
    String pdt_price = "4000";
    String typeCnt = "HOT";
    int shotCnt = 0;
    int total_price = Integer.parseInt(pdt_price);
    int itemId = 301;
    int basket_price;

    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH)+1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);
    String reserve_hour = "";
    String reserve_minute = "";
//    String open_flag = "Y";
//    String reserve_flag = "Y";

    Button amountDown_f3, amountUp_f3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent3, container, false);
        mHandler = new Handler();

        amountDown_f3 = (Button) view.findViewById(R.id.amountDown_f3);
        amountUp_f3 = (Button) view.findViewById(R.id.amountUp_f3);

        amountText_f3 = (TextView) view.findViewById(R.id.amountText_f3);
        price1_f3 = (TextView) view.findViewById(R.id.price1_f3);

        orderNow_f3 = (ImageButton) view.findViewById(R.id.orderNow_f3);
        reserveNow_f3 = (ImageButton) view.findViewById(R.id.reserveNow_f3);
        addCart_f3 = (ImageButton) view.findViewById(R.id.addCart_f3);
        cancel_f3 = (ImageButton) view.findViewById(R.id.cancel_f3);
        chatTV_f3 = (TextView) view.findViewById(R.id.chatTV_f3);

        amountCnt = Integer.parseInt(amountText_f3.getText().toString());

        models = new ArrayList<>();
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_1, "플레인요거트", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_2, "딸기요거트스무디", "4500"));
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_3, "블루베리요거트스무디", "4500"));
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_4, "망고요거트스무디", "4500"));
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_5, "마카롱", "2500"));
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_6, "컵쿠키", "2700"));
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_7, "쿠키", "2000"));
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_8, "커피콩빵", "3000"));
        models.add(new ProductImageModel(R.drawable.pdt_smoothie_9, "구슬아이스크림", "3000"));
        adapter = new ProductImageAdapter(models, getContext());

        viewPager = view.findViewById(R.id.viewPager4);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);
        basket_price = Integer.parseInt(total_price(total_price));

        // 지점오픈여부
//        if(open_flag == "N"){orderNow_f3.setEnabled(false);}
//        else{ orderNow_f3.setEnabled(true); }
        orderNow_f3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                        "STX"
                                +"MS04"
                                +"00"
                                +"ID="+ProductActivity.member_id
                                +"SHOP="+ProductActivity.shop_name
                                +"COUNT="+menu_count
                                +"D01="+pdt_name
                                +"D02="+typeCntNumber(typeCnt)
                                +"D03="+amountCnt(amountCnt)
                                +"D04="+shotCnt(shotCnt)
                                +"D05="+total_price(total_price)
                                +"PRICE="+basket_total_price(Integer.parseInt(total_price(total_price)))
                                +"MSG="+""
                                +"ETX"
                );
                mt.start();
            }
        });

        // 예약하기 버튼
        if(ProductActivity.reserve_text.contains("예약 불가능")){reserveNow_f3.setEnabled(false);}
        else{reserveNow_f3.setEnabled(true);}
        reserveNow_f3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay<10){
                            reserve_hour = "0"+hourOfDay;
                        }else{reserve_hour = ""+hourOfDay;}
                        if(minute<10){reserve_minute="0"+minute;}else{reserve_minute=""+minute;}
                            IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                                "STX"
                                        +"MS05"
                                        +"00"
                                        +year+"-"+reserve_month(month)+"-"+reserve_day(day)
                                        +reserve_hour+":"+reserve_minute
                                        +"ID="+ProductActivity.member_id
                                        +"SHOP="+ProductActivity.shop_name
                                        +"COUNT="+menu_count
                                        +"D01="+pdt_name
                                        +"D02="+typeCntNumber(typeCnt)
                                        +"D03="+amountCnt(amountCnt)
                                        +"D04="+shotCnt(shotCnt)
                                        +"D05="+total_price(total_price)
                                        +"PRICE="+basket_total_price(Integer.parseInt(total_price(total_price)))
                                        +"ETX");
                            mt.start();
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("예약 설정");
                timePickerDialog.show();
            }
        });
        addCart_f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        cancel_f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        price1_f3.setText("가격 : "+pdt_price+" 원");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                amountCnt = 1;
                pdt_name = models.get(position).getTitle();
                pdt_price = models.get(position).getDesc();
                amountText_f3.setText(""+amountCnt);
                price1_f3.setText("가격 : "+pdt_price+" 원");
                total_price = Integer.parseInt(pdt_price);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        amountDown_f3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(amountCnt <= 1){amountCnt = 1;}
                else {amountCnt--;
                    if(typeCnt == "HOT") {
                        total_price = total_price - Integer.parseInt(pdt_price);
                    }
                    else if(typeCnt == "ICE"){
                        total_price = total_price - (Integer.parseInt(pdt_price)+500);
                    }
                    price1_f3.setText("가격 : "+(total_price)+" 원");
                }
                amountText_f3.setText(""+amountCnt);
            }
        });

        amountUp_f3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(amountCnt>=99){amountCnt = 99;}
                else {amountCnt++;
                    if(typeCnt == "HOT"){
                        total_price = total_price +Integer.parseInt(pdt_price);
                    }
                    else if(typeCnt == "ICE"){
                        total_price = total_price +(Integer.parseInt(pdt_price)+500);
                    }
                    price1_f3.setText("가격 : "+(total_price)+" 원");
                }
                amountText_f3.setText(""+amountCnt);
            }
        });
        return view;
    }
        String result = "";
        String result2 = "";
        private String basket_total_price(int basket_price) {
            if(basket_price <1){
                result2 = "000000";
            }else if(basket_price < 10){
                result2 = "00000"+basket_price;
            }else if(basket_price<100){
                result2 ="0000"+basket_price;
            }else if (basket_price < 1000){
                result2 = "000"+basket_price;
            }else if (basket_price < 10000){
                result2 = "00"+basket_price;
            }else if (basket_price < 100000){
                result2 = "0"+basket_price;
            }else if(basket_price <= 999999){
                result2 = ""+basket_price;
            }else{
                result2 = "999999";
            }
            return result2;
        }

        private String total_price(int total_price) {
            if(total_price <1){
                result = "00000";
            }else if(total_price < 10){
                result = "0000"+total_price;
            }else if(total_price<100){
                result="000"+total_price;
            }else if (total_price < 1000){
                result = "00"+total_price;
            }else if (total_price < 10000){
                result = "0"+total_price;
            }else if (total_price < 99999){
                result = ""+total_price;
            }else{
                result = "99999";
            }
            return result;
        }
        private int typeCntNumber(String typeCnt){
            int typeNum;
            if(typeCnt == "HOT"){
                typeNum = 1;
            }else if(typeCnt == "ICE"){
                typeNum = 2;
            }else{
                typeNum = 0;
            }
            return typeNum;
        }

        public String amountCnt(int amountCnt){
            String amountString = "";
            if(amountCnt<10){
                amountString = "0"+amountCnt;
            }else{
                amountString = ""+amountCnt;
            }
            return amountString;
        }
        public String shotCnt(int shotCnt){
            String shotString = "";
            if(shotCnt<10){
                shotString = "0"+shotCnt;
            }else{shotString = ""+shotCnt;}
            return shotString;
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
        // 지정한 예약시간 함수
        private String reserve_month(int month) {
            String monthR = "";
            if(month<10){monthR = "0"+month;}
            else{monthR = ""+month;}
            return monthR;
        }
        private String reserve_day(int day) {
            String dayR = "";
            if(day<10){dayR = "0"+day;}
            else{dayR = ""+day;}
            return dayR;
        }
        private String reserve_hour(int hour) {
            String hourR = "";
            if(hour<10){hourR = "0"+hour;}
            else{hourR = ""+hour;}
            return hourR;
        }
        private String reserve_min(int min) {
            String minR = "";
            if(min<10){minR = "0"+min;}
            else{minR = ""+min;}
            return minR;
        }
        private String reserve_sec(int sec) {
            String secR = "";
            if(sec<10){secR = "0"+sec;}
            else{secR = ""+sec;}
            return secR;
        }
    }