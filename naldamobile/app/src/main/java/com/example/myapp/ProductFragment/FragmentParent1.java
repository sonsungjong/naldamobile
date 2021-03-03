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

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class FragmentParent1 extends Fragment {

    private Handler mHandler;
    Socket socket;
    // 실제 서버
    private String ip = "210.114.12.66";
    private int port = 1387;
    // 내컴퓨터 테스트용
//    private String ip = "192.168.0.60";
//    private int port = 1001;

    ViewPager viewPager;
    ProductImageAdapter adapter;
    List<ProductImageModel> models;

    TextView chatTV_f1;
    ImageButton reserveNow_f1, orderNow_f1, addCart_f1, cancel_f1;

    TextView typeText_f1, shotText_f1, amountText_f1, price1_f1;
    String typeCnt = "HOT";
    int shotCnt = 0;
    int amountCnt = 1;
    String menu_count = "01";
    String pdt_name = "아메리카노";
    String pdt_price = "2500";
    int total_price = Integer.parseInt(pdt_price);
    int itemId = 101;

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

    Button type1_f1, type2_f1, amountDown_f1, amountUp_f1, shotDown_f1, shotUp_f1;

    // member_id와 shop_name static 버그날때 사용하기
//    ProductActivity pa = new ProductActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent1, container, false);
        mHandler = new Handler();

        type1_f1 = (Button) view.findViewById(R.id.type1_f1);
        type2_f1 = (Button) view.findViewById(R.id.type2_f1);
        amountDown_f1 = (Button) view.findViewById(R.id.amountDown_f1);
        amountUp_f1 = (Button) view.findViewById(R.id.amountUp_f1);
        shotDown_f1 = (Button) view.findViewById(R.id.shotDown_f1);
        shotUp_f1 = (Button) view.findViewById(R.id.shotUp_f1);

        typeText_f1 = (TextView) view.findViewById(R.id.typeText_f1);
        shotText_f1 = (TextView) view.findViewById(R.id.shotText_f1);
        amountText_f1 = (TextView) view.findViewById(R.id.amountText_f1);
        price1_f1 = (TextView) view.findViewById(R.id.price1_f1);

        orderNow_f1 = (ImageButton) view.findViewById(R.id.orderNow_f1);
        reserveNow_f1 = (ImageButton) view.findViewById(R.id.reserveNow_f1);
        addCart_f1 = (ImageButton) view.findViewById(R.id.addCart_f1);
        cancel_f1 = (ImageButton) view.findViewById(R.id.cancel_f1);
        chatTV_f1 = (TextView) view.findViewById(R.id.chatTV_f1);

        amountCnt = Integer.parseInt(amountText_f1.getText().toString());

        models = new ArrayList<>();
        models.add(new ProductImageModel(R.drawable.pdt_coffee_1, "아메리카노", "2500"));
        models.add(new ProductImageModel(R.drawable.pdt_coffee_2, "카페라떼", "3300"));
        models.add(new ProductImageModel(R.drawable.pdt_coffee_3, "카푸치노", "3300"));
        models.add(new ProductImageModel(R.drawable.pdt_coffee_4, "바닐라라떼", "3300"));
        models.add(new ProductImageModel(R.drawable.pdt_coffee_5, "카라멜마끼아또", "3800"));
        models.add(new ProductImageModel(R.drawable.pdt_coffee_6, "카페모카", "3800"));
        models.add(new ProductImageModel(R.drawable.pdt_coffee_7, "헤이즐넛라떼", "4300"));
        models.add(new ProductImageModel(R.drawable.pdt_coffee_8, "카페사이공", "4000"));
        adapter = new ProductImageAdapter(models, getContext());

        viewPager = view.findViewById(R.id.viewPager2);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        // 지점오픈여부
//        if(open_flag == "N"){orderNow_f1.setEnabled(false);}
//        else{ orderNow_f1.setEnabled(true); }
        orderNow_f1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ConnectThread th = new ConnectThread();
                th.start();
            }
        });

        // 예약하기 버튼
        if(ProductActivity.reserve_text.contains("예약 불가능")){reserveNow_f1.setEnabled(false);}
        else{reserveNow_f1.setEnabled(true);}
        reserveNow_f1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay<10){
                            reserve_hour = "0"+hourOfDay;
                        }else{reserve_hour = ""+hourOfDay;}
                        if(minute<10){reserve_minute="0"+minute;}else{reserve_minute=""+minute;}
                        ConnectThreadReserve th2 = new ConnectThreadReserve();
                        th2.start();
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("예약 설정");
                timePickerDialog.show();

            }
        });

        addCart_f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
//                String title = pdt_name;
//                String price = pdt_price;
//                String type = typeText_f1.getText().toString().trim();
//                String quantity = amountText_f1.getText().toString().trim();
//                String shot = shotText_f1.getText().toString().trim();
//
//                //add to db(SQLite)
//                addToCart(productId, title, price, type, quantity, shot);
            }
        });

        cancel_f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        price1_f1.setText("가격 : "+pdt_price+" 원");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                typeCnt = "HOT";
                amountCnt = 1;
                shotCnt = 0;
                pdt_name = models.get(position).getTitle();
                pdt_price = models.get(position).getDesc();
                typeText_f1.setText(typeCnt);
                amountText_f1.setText(""+amountCnt);
                shotText_f1.setText(""+shotCnt);
                price1_f1.setText("가격 : "+pdt_price+" 원");
                total_price = Integer.parseInt(pdt_price);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        type1_f1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(typeCnt == "HOT"){
                    typeCnt = "ICE";
                    amountCnt = 1;
                    shotCnt = 0;
                    total_price = Integer.parseInt(pdt_price) + 500;
                }else{
                    typeCnt = "HOT";
                    amountCnt = 1;
                    shotCnt = 0;
                    total_price = Integer.parseInt(pdt_price);
                }
                typeText_f1.setText(typeCnt);
                amountText_f1.setText(""+amountCnt);
                shotText_f1.setText(""+shotCnt);
                price1_f1.setText("가격 : "+(total_price)+" 원");
            }
        });

        type2_f1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(typeCnt == "HOT"){
                    typeCnt = "ICE";
                    amountCnt = 1;
                    shotCnt = 0;
                    total_price = Integer.parseInt(pdt_price) + 500;
                }else{
                    typeCnt = "HOT";
                    total_price = Integer.parseInt(pdt_price);
                    amountCnt = 1;
                    shotCnt = 0;
                }
                typeText_f1.setText(typeCnt);
                amountText_f1.setText(""+amountCnt);
                shotText_f1.setText(""+shotCnt);
                price1_f1.setText("가격 : "+(total_price)+" 원");
            }
        });

        amountDown_f1.setOnClickListener(new View.OnClickListener(){
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
                    price1_f1.setText("가격 : "+(total_price)+" 원");
                }
                amountText_f1.setText(""+amountCnt);
            }
        });

        amountUp_f1.setOnClickListener(new View.OnClickListener(){
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
                    price1_f1.setText("가격 : "+(total_price)+" 원");
                }
                amountText_f1.setText(""+amountCnt);
            }
        });

        shotDown_f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shotCnt <= 0){shotCnt = 0;}
                else {shotCnt--;
                    total_price = total_price - 500;
                    price1_f1.setText("가격 : "+(total_price)+" 원");
                }
                shotText_f1.setText(""+shotCnt);
            }
        });

        shotUp_f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shotCnt>=99){shotCnt = 99;}
                else {shotCnt++;
                    total_price = total_price + 500;
                    price1_f1.setText("가격 : "+(total_price)+" 원");
                }
                shotText_f1.setText(""+shotCnt);
            }
        });

        return view;
    }


    //주문
    class ConnectThread extends Thread{
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
        //주문
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                int basket_price = Integer.parseInt(total_price(total_price));

                // 보낼 메시지
                String sndMsg =
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
                                +"PRICE="+basket_total_price(basket_price)
                                +"MSG="+""
                                +"ETX";

//                sndMsg = getStringToByte(sndMsg);
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

                socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
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
    }
    //주문
    class msgUpdate implements Runnable {
        private String msg;
        public msgUpdate(String str) {
            this.msg = str;
        }
        public void run() {
            chatTV_f1.setText(chatTV_f1.getText().toString() + msg + "\n");
            if((chatTV_f1.getText().toString()).contains("STXMS0400")){
//                Toast.makeText(getApplicationContext(), "주문 진행", Toast.LENGTH_LONG).show();
                // 주문확인 팝업으로 이동
//                Intent intent1 = new Intent(getApplicationContext(), Order_PopupActivity.class);
//                startActivityForResult(intent1, 1);

                // 결제모듈 테스트용(주문하기)
                Intent payjoa = new Intent(getActivity().getApplicationContext(), PaymentActivity.class);
                payjoa.putExtra("member_id",ProductActivity.member_id);
                payjoa.putExtra("shop_name",ProductActivity.shop_name);
                payjoa.putExtra("menu_count",menu_count);
                payjoa.putExtra("pdt_name",pdt_name);
                payjoa.putExtra("type",typeCnt);
                payjoa.putExtra("choice",shotCnt);
                payjoa.putExtra("amount",amountCnt);
                payjoa.putExtra("total_price",total_price);
                payjoa.putExtra("reserve_time","");
                payjoa.putExtra("classify",1);
                startActivity(payjoa);
                getActivity().finish();

            }else if((chatTV_f1.getText().toString()).contains("STXMS0401")){
                Toast.makeText(getActivity().getApplicationContext(), "주문 실패", Toast.LENGTH_LONG).show();
            }else if((chatTV_f1.getText().toString()).contains("STXMS0402")){
                Toast.makeText(getActivity().getApplicationContext(), "해당 제품이 품절되었습니다", Toast.LENGTH_LONG).show();
            }else if((chatTV_f1.getText().toString()).contains("STXMS0403")){
                Toast.makeText(getActivity().getApplicationContext(), "오류03", Toast.LENGTH_LONG).show();
            }else if(chatTV_f1.getText().toString() == null || chatTV_f1.getText().toString().equals("")){
                Toast.makeText(getActivity().getApplicationContext(), "빈값", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "서버오류 발생", Toast.LENGTH_LONG).show();
            }
        }
    }

    //예약
    class ConnectThreadReserve extends Thread{
        int basket_price = Integer.parseInt(total_price(total_price));

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
        // 예약
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);
                // 보낼 메시지
                String sndMsg =
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
                                +"PRICE="+basket_total_price(basket_price)
                                +"ETX";

                Log.d("=============", sndMsg);

                // 전송
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-16le")), true);
                out.println(sndMsg);

                // 예약응답 대기 시간동안 프로그레스바 출력

                // 수신
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-16le"));
                String read = input.readLine();
                mHandler.post(new msgUpdateReserve(read));
                // 프로그레스바 없애기
                Log.d("=============", read);

                socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        // 현재시간 함수
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
    //예약
    class msgUpdateReserve implements Runnable{
        private String msg;
        public msgUpdateReserve(String str) {
            this.msg = str;
        }
        public void run() {
            chatTV_f1.setText(chatTV_f1.getText().toString() + msg + "\n");
            if((chatTV_f1.getText().toString()).contains("STXMS0500")){
                // 예약 완료
                Intent payjoa = new Intent(getActivity().getApplicationContext(), PaymentActivity.class);
                payjoa.putExtra("member_id",ProductActivity.member_id);
                payjoa.putExtra("shop_name",ProductActivity.shop_name);
                payjoa.putExtra("menu_count",menu_count);
                payjoa.putExtra("pdt_name", pdt_name);
                payjoa.putExtra("type",typeCnt);
                payjoa.putExtra("choice",shotCnt);
                payjoa.putExtra("amount",amountCnt);
                payjoa.putExtra("total_price",total_price);
                payjoa.putExtra("reserve_time",reserve_hour+":"+reserve_minute);
                payjoa.putExtra("classify",2);
                startActivity(payjoa);
                getActivity().finish();
            }else if((chatTV_f1.getText().toString()).contains("STXMS0401")){
                Toast.makeText(getActivity().getApplicationContext(), "예약 실패", Toast.LENGTH_LONG).show();
            }else if((chatTV_f1.getText().toString()).contains("STXMS0402")){
                Toast.makeText(getActivity().getApplicationContext(), "같은 시간에 예약이 존재합니다. 다시 선택해주세요", Toast.LENGTH_LONG).show();
            }else if((chatTV_f1.getText().toString()).contains("STXMS0403")){
                Toast.makeText(getActivity().getApplicationContext(), "오류03", Toast.LENGTH_LONG).show();
            }else if(chatTV_f1.getText().toString() == null || chatTV_f1.getText().toString().equals("")){
                Toast.makeText(getActivity().getApplicationContext(), "빈값", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "서버오류 발생", Toast.LENGTH_LONG).show();
            }
        }
    }

        private void addToCart(String productId, String title, String price, String type, String quantity, String shot){
        itemId++;

        EasyDB easyDB = EasyDB.init(getContext(), "ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Title", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Type", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Shot", new String[]{"text","not null"}))
                .doneTableColumn();

        Boolean b = easyDB
                .addData("Item_Id",itemId)
                .addData("Item_PID",productId)
                .addData("Item_Title",title)
                .addData("Item_Price",price)
                .addData("Item_Type",type)
                .addData("Item_Quantity",quantity)
                .addData("Item_Shot",shot)
                .doneDataAdding();

        Toast.makeText(getContext(), "장바구니 추가", Toast.LENGTH_SHORT).show();
    }
}