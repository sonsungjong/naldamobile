package com.example.myapp.ProductFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.myapp.CartActivity;
import com.example.myapp.IntroActivity;
import com.example.myapp.ProductActivity;
import com.example.myapp.R;
import com.example.myapp.ShopActivity;
import com.example.myapp.adapter.ProductImageAdapter;
import com.example.myapp.model.ProductImageModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentParent5 extends Fragment {

    private Handler mHandler;

    ViewPager viewPager;
    ProductImageAdapter adapter;
    List<ProductImageModel> models;

    TextView chatTV_f5;
    ImageButton reserveNow_f5, orderNow_f5, addCart_f5, cancel_f5;

    TextView typeText_f5, amountText_f5, price1_f5;
    String typeCnt = "HOT";
    int shotCnt = 0;
    int amountCnt = 1;
    String menu_count = "01";
    int pdt_image = R.drawable.pdt_tea_1;
    String pdt_name = "사이다";
    String pdt_price = "1500";
    int total_price = Integer.parseInt(pdt_price);
    int itemId = 501;
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

    Button type1_f5, type2_f5, amountDown_f5, amountUp_f5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent5, container, false);
        mHandler = new Handler();

        type1_f5 = (Button) view.findViewById(R.id.type1_f5);
        type2_f5 = (Button) view.findViewById(R.id.type2_f5);
        amountDown_f5 = (Button) view.findViewById(R.id.amountDown_f5);
        amountUp_f5 = (Button) view.findViewById(R.id.amountUp_f5);

        typeText_f5 = (TextView) view.findViewById(R.id.typeText_f5);
        amountText_f5 = (TextView) view.findViewById(R.id.amountText_f5);
        price1_f5 = (TextView) view.findViewById(R.id.price1_f5);

        orderNow_f5 = (ImageButton) view.findViewById(R.id.orderNow_f5);
        reserveNow_f5 = (ImageButton) view.findViewById(R.id.reserveNow_f5);
        addCart_f5 = (ImageButton) view.findViewById(R.id.addCart_f5);
        cancel_f5 = (ImageButton) view.findViewById(R.id.cancel_f5);
        chatTV_f5 = (TextView) view.findViewById(R.id.chatTV_f5);

        amountCnt = Integer.parseInt(amountText_f5.getText().toString());

        models = new ArrayList<>();
        models.add(new ProductImageModel(R.drawable.pdt_tea_1, "사이다", "1500"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_2, "콜라", "1500"));
//        models.add(new ProductImageModel(R.drawable.pdt_tea_3, "한라봉차", "4500"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_3, "한라봉차", "100"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_4, "오미자차", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_5, "대추차", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_6, "석류차", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_7, "유자차", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_8, "자몽차", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_9, "레몬차", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_10, "얼그레이홍차", "3000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_11, "하비스커스", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_12, "자스민", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_13, "캐모마일", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_14, "페퍼민트", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_15, "석류아이스티", "4000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_16, "레몬아이스티", "3500"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_17, "모과차", "5000"));
        models.add(new ProductImageModel(R.drawable.pdt_tea_18, "생강차", "5000"));
        adapter = new ProductImageAdapter(models, getContext());

        viewPager = view.findViewById(R.id.viewPager6);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);
        basket_price = Integer.parseInt(total_price(total_price));

        // 지점오픈여부
//        if(open_flag == "N"){orderNow_f5.setEnabled(false);}
//        else{ orderNow_f5.setEnabled(true); }
        orderNow_f5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                        "STX"
                                +"MS04"
                                +"00"
                                +"ID="+ ShopActivity.member_id
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
        if(ProductActivity.reserve_text.contains("예약 불가능")){reserveNow_f5.setEnabled(false);}
        else{reserveNow_f5.setEnabled(true);}
        reserveNow_f5.setOnClickListener(new View.OnClickListener(){
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
                                        +"ID="+ShopActivity.member_id
                                        +"SHOP="+ProductActivity.shop_name
                                        +"COUNT="+menu_count
                                        +"D01="+pdt_name
                                        +"D02="+typeCntNumber(typeCnt)
                                        +"D03="+amountCnt(amountCnt)
                                        +"D04="+shotCnt(shotCnt)
                                        +"D05="+total_price(total_price)
                                        +"PRICE="+basket_total_price(Integer.parseInt(total_price(total_price)))
                                        +"MSG="+""
                                        +"ETX");
                            mt.start();
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("예약 설정");
                timePickerDialog.show();
            }
        });
        // 장바구니 담기 버튼
        addCart_f5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cartKey = typeCnt+pdt_name+shotCnt;
                int cartPay = (Integer.parseInt(pdt_price)) + (shotCnt*500) + typePrice(typeCnt);
                int i = 0;
                for (i=0;i<ProductActivity.cartKey.size();i++){
                    if(cartKey.equals(ProductActivity.cartKey.get(i))){
                        ProductActivity.cartFlag = i;   // 중복 플래그 작동
                        break;
                    }
                }
                if(ProductActivity.cartFlag != 0 && ProductActivity.cartFlag == i){
                    int intCartAmount = ProductActivity.cartAmount.get(i);
                    ProductActivity.cartAmount.set(i,amountCnt+intCartAmount);
                    ProductActivity.cartFlag = 0;   // 중복 플래그 해제
                }else{
                    ProductActivity.cartMenuCount++;
                    ProductActivity.cartKey.add(typeCnt + pdt_name + shotCnt);
                    ProductActivity.cartImg.add(pdt_image);
                    ProductActivity.cartPdtName.add(pdt_name);
                    ProductActivity.cartChoice.add(typeCntNumber(typeCnt));
                    ProductActivity.cartAmount.add(amountCnt);
                    ProductActivity.cartShot.add(shotCnt);
                    ProductActivity.cartPay.add(cartPay);   // 개당 가격 (amount 곱해줘야됨)
                }
                Toast.makeText(getContext(), pdt_name+"("+amountCnt+") 장바구니 추가", Toast.LENGTH_SHORT).show();
                // Notification 메시지 숫자에 +=amountCnt 누적합
            }
        });


        cancel_f5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });

        price1_f5.setText("가격 : "+pdt_price+" 원");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                typeCnt = "HOT";
                amountCnt = 1;
                pdt_image = models.get(position).getImage();
                pdt_name = models.get(position).getTitle();
                pdt_price = models.get(position).getDesc();
                typeText_f5.setText(typeCnt);
                amountText_f5.setText(""+amountCnt);
                price1_f5.setText("가격 : "+pdt_price+" 원");
                total_price = Integer.parseInt(pdt_price);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        type1_f5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(typeCnt == "HOT"){
                    typeCnt = "ICE";
                    amountCnt = 1;
                    total_price = Integer.parseInt(pdt_price) + 500;
                }else{
                    typeCnt = "HOT";
                    amountCnt = 1;
                    total_price = Integer.parseInt(pdt_price);
                }
                typeText_f5.setText(typeCnt);
                amountText_f5.setText(""+amountCnt);
                price1_f5.setText("가격 : "+(total_price)+" 원");
            }
        });

        type2_f5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(typeCnt == "HOT"){
                    typeCnt = "ICE";
                    amountCnt = 1;
                    total_price = Integer.parseInt(pdt_price) + 500;
                }else{
                    typeCnt = "HOT";
                    total_price = Integer.parseInt(pdt_price);
                    amountCnt = 1;
                }
                typeText_f5.setText(typeCnt);
                amountText_f5.setText(""+amountCnt);
                price1_f5.setText("가격 : "+(total_price)+" 원");
            }
        });

        amountDown_f5.setOnClickListener(new View.OnClickListener(){
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
                    price1_f5.setText("가격 : "+(total_price)+" 원");
                }
                amountText_f5.setText(""+amountCnt);
            }
        });

        amountUp_f5.setOnClickListener(new View.OnClickListener(){
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
                    price1_f5.setText("가격 : "+(total_price)+" 원");
                }
                amountText_f5.setText(""+amountCnt);
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

        // 장바구니용 하나의 제품의 개당 가격계산용
        private int typePrice(String typeCnt){
            int typeNum;
            if(typeCnt == "ICE"){
                typeNum = 500;
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