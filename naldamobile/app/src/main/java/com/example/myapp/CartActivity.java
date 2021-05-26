package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import com.example.myapp.adapter.CartAdapter;
import com.example.myapp.adapter.HistoryAdapter;
import com.example.myapp.listener.OnItemClick;

import java.util.Calendar;

public class CartActivity extends AppCompatActivity implements OnItemClick {

    public Context mContext;
//    public static Activity cart_activity;
    public int cartTotal = 0;   // 하단 총가격 변수
    TextView cartTotalPrice;    // 하단 총가격 표시 텍스트
    CartAdapter cartAdapter;
    RecyclerView recyclerCart;
    Button orderNow, reserveNow;
    int classify;   // 모바일주문/모바일예약 구분 플래그
    String menu_count, pdtInfo, reserve_time, total_price, cart_msg;
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH)+1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int min = cal.get(Calendar.MINUTE);
//    int sec = cal.get(Calendar.SECOND);
    String reserve_hour = "";
    String reserve_minute = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        recyclerCart = (RecyclerView) findViewById(R.id.recycler_cart);
        cartTotalPrice = (TextView) findViewById(R.id.cart_total_price);
        orderNow = (Button) findViewById(R.id.cart_order);
        reserveNow = (Button) findViewById(R.id.cart_reserve);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(CartActivity.this, this);
        recyclerCart.setAdapter(cartAdapter);
        mContext = this;

        // cart가격 갱신
        updateCartTotalPrice();

        // 주문하기
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                        "STX"
                                +"MS04"
                                +"00"
                                +"ID="+ ShopActivity.member_id
                                +"SHOP="+ProductActivity.shop_name
                                +"COUNT="+twoString(ProductActivity.cartMenuCount)
                                +countPdtInfo(ProductActivity.cartMenuCount)
                                +"PRICE="+cartTotalPay(cartTotal)    // 장바구니 전체의 가격
                                +"MSG="+""  // 메모
                                +"ETX"
                );
                mt.start();
                finish();
            }
        });

        // 예약하기
        if(ProductActivity.reserve_text.contains("예약 불가능")){reserveNow.setEnabled(false);}
        reserveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
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
                                +"ID="+ ShopActivity.member_id
                                +"SHOP="+ProductActivity.shop_name
                                +"COUNT="+twoString(ProductActivity.cartMenuCount)
                                +countPdtInfo(ProductActivity.cartMenuCount)
                                +"PRICE="+cartTotalPay(cartTotal)    // 장바구니 전체의 가격
                                +"MSG="+""  // 메모
                                +"ETX"
                );
                mt.start();
                if(!reserve_hour.equals("")){
                    finish();
                }
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("예약 설정");
                timePickerDialog.show();
            }
        });
    }   // onCreate



    @Override
    public void updateCartTotalPrice() {
        cartTotal = 0;
        for (int i=0; ProductActivity.cartPay.size()>i; i++){
            for(int j=0; ProductActivity.cartAmount.size()>j; j++){
                if(i==j){
                    cartTotal += ProductActivity.cartPay.get(i)*ProductActivity.cartAmount.get(j);
                }
            }
        }
        cartTotalPrice.setText("총 금액 : "+cartTotal+"원");
        if(cartTotal > 0){
            orderNow.setEnabled(true);
            reserveNow.setEnabled(true);
        }else if(cartTotal <= 0){
            orderNow.setEnabled(false);
            reserveNow.setEnabled(false);
        }
        if(ProductActivity.reserve_text.contains("예약 불가능")){reserveNow.setEnabled(false);}
    }
    // 한종류 제품의 가격
    private String listPay(int position) {
        // 샷수가 포함된 가격 * 수량
        int _listPay = ProductActivity.cartPay.get(position)* ProductActivity.cartAmount.get(position);
        if(_listPay < 10){  // ~9
            return "0000"+_listPay;
        }else if(_listPay < 100){       // ~99
            return "000"+_listPay;
        }else if(_listPay < 1000){      // ~999
            return "00"+_listPay;
        }else if(_listPay < 10000){     // ~9999
            return "0"+_listPay;
        }
        return ""+_listPay;
    }
    // 전부 총 가격String
    private String cartTotalPay(int cartTotal) {
        if(cartTotal<10){
            return "00000"+cartTotal;
        }else if(cartTotal< 100){
            return "0000"+cartTotal;
        }else if(cartTotal < 1000){
            return "000"+cartTotal;
        }else if(cartTotal < 10000){
            return "00"+cartTotal;
        }else if(cartTotal < 100000){
            return "0"+cartTotal;
        }
        return ""+cartTotal;
    }

    private String twoString(int num){
        if(num<10){
            return "0"+num;
        }
        return ""+num;
    }
    // 메뉴수 2개 이상
    private String countPdtInfo(int cartMenuCount){
        String pdtInfo = "";
        /*if(cartMenuCount < 2){}
        if(cartMenuCount < 3){
            pdtInfo +=
                    "D06="+ProductActivity.cartPdtName.get(1)+
                    "D07="+ProductActivity.cartChoice.get(1)+
                    "D08="+ProductActivity.cartAmount.get(1)+
                    "D09="+ProductActivity.cartShot.get(1)+
                    "D10="+listPay(1);
        }
        if(cartMenuCount < 4){
            pdtInfo +=
                    "D11="+ProductActivity.cartPdtName.get(2)+
                    "D12="+ProductActivity.cartChoice.get(2)+
                    "D13="+ProductActivity.cartAmount.get(2)+
                    "D14="+ProductActivity.cartShot.get(2)+
                    "D15="+listPay(2);
        }
        if(cartMenuCount < 5){
            pdtInfo +=
                    "D16="+ProductActivity.cartPdtName.get(3)+
                    "D17="+ProductActivity.cartChoice.get(3)+
                    "D18="+ProductActivity.cartAmount.get(3)+
                    "D19="+ProductActivity.cartShot.get(3)+
                    "D20="+listPay(3);
        }
        if(cartMenuCount < 6){
            pdtInfo +=
                    "D21="+ProductActivity.cartPdtName.get(4)+
                    "D22="+ProductActivity.cartChoice.get(4)+
                    "D23="+ProductActivity.cartAmount.get(4)+
                    "D24="+ProductActivity.cartShot.get(4)+
                    "D25="+listPay(4);
        }
        if(cartMenuCount < 7){
            pdtInfo +=
                    "D26="+ProductActivity.cartPdtName.get(5)+
                    "D27="+ProductActivity.cartChoice.get(5)+
                    "D28="+ProductActivity.cartAmount.get(5)+
                    "D29="+ProductActivity.cartShot.get(5)+
                    "D30="+listPay(5);
        }
        */
        for (int i=0;i<cartMenuCount;i++){
            pdtInfo = pdtInfo
                    +"D"+twoString(i*5+1)+"="+ProductActivity.cartPdtName.get(i)
                    +"D"+twoString(i*5+2)+"="+ProductActivity.cartChoice.get(i)
                    +"D"+twoString(i*5+3)+"="+twoString(ProductActivity.cartAmount.get(i))
                    +"D"+twoString(i*5+4)+"="+twoString(ProductActivity.cartShot.get(i))
                    +"D"+twoString(i*5+5)+"="+listPay(i);
        }
        return pdtInfo;
    }

    public void orderSuccess(String read){
        int idx1, idx2, idx3, idx4, idx5, idx6, idx7, idx8;
        Log.d("orderSuccess(): ",read);

        if(read.contains("STXMS0400")){
            // 주문확인 팝업으로 이동
//                Intent intent1 = new Intent(getApplicationContext(), Order_PopupActivity.class);
//                startActivityForResult(intent1, 1);

            // 상품종류수 01
            idx1 = read.indexOf("COUNT=");
            // 상품이름 아메리카노
            idx2 = read.indexOf("D01=");
            idx7 = read.indexOf("PRICE=");
            // 고객요청 메시지
            idx8 = read.indexOf("MSG=");
            menu_count = read.substring(idx1 +6, idx2);
            pdtInfo = read.substring(idx2, idx7);
            total_price = read.substring(idx7 +6, idx8);
            cart_msg = read.substring(idx8+4);
            reserve_time = read.substring(22, 27);
            classify = 1;

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("shop_name",ProductActivity.shop_name);
            intent.putExtra("menu_count",menu_count);
            intent.putExtra("pdtInfo",pdtInfo);
            intent.putExtra("total_price",Integer.parseInt(total_price));
            intent.putExtra("reserve_time","");
            intent.putExtra("cart_msg", cart_msg);
            intent.putExtra("classify",classify);
            startActivity(intent);
            /*ProductActivity productActivity = (ProductActivity) ProductActivity.productActivity; // 객체를 만든다.
            productActivity.finish();*/
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
        int idx1, idx2, idx3, idx4, idx5, idx6, idx7, idx8, idx9, idx10;
        Log.d("reserveSuccess(): ",read);

        if(read.contains("STXMS0500")){
            // 상품종류수 01
            idx1 = read.indexOf("COUNT=");
            // 상품이름 아메리카노
            idx2 = read.indexOf("D01=");
            /*idx3 = read.indexOf("D02=");
            // 상품 수량 01
            idx4 = read.indexOf("D03=");
            // 샷추가 00
            idx5 = read.indexOf("D04=");
            // 해당상품 합계 가격 00000
            idx6 = read.indexOf("D05=");*/
            // 장바구니 전체 가격
            idx7 = read.indexOf("PRICE=");
            // 고객요청 메시지
            idx8 = read.indexOf("MSG=");
            menu_count = read.substring(idx1 +6, idx2);
            pdtInfo = read.substring(idx2, idx7);
            total_price = read.substring(idx7 +6, idx8);
            cart_msg = read.substring(idx8+4);
            reserve_time = read.substring(22, 27);
            classify = 2;

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("shop_name",ProductActivity.shop_name);
            intent.putExtra("menu_count",menu_count);
            intent.putExtra("pdtInfo",pdtInfo);     // 상품이름, 상품기호, 상품수량, 상품샷추가횟수, 상품개별가격
            intent.putExtra("total_price",Integer.parseInt(total_price));
            intent.putExtra("reserve_time",reserve_time);
            intent.putExtra("cart_msg", cart_msg);
            intent.putExtra("classify",classify);
            startActivity(intent);
            /*ProductActivity productActivity = (ProductActivity) ProductActivity.productActivity; // 객체를 만든다.
            productActivity.finish();*/
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
}
