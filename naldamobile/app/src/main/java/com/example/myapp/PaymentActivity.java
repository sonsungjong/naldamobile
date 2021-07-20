package com.example.myapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iamport.sdk.data.sdk.IamPortRequest;
import com.iamport.sdk.data.sdk.PayMethod;
import com.iamport.sdk.domain.core.Iamport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import kotlin.Unit;

public class PaymentActivity extends AppCompatActivity {

    // orderNum 혹은 reserveResult 쪽에서 소켓으로 결제완료메시지 보내기
    // PaymentActivity 없애고 Product Fragment쪽에서 직접 결제하게하기
    // 장바구니에서 직접처리하게 하기
    public static Context mContext;
    TextView chatTvPay;
    Button closePayment;
    String menu_count, type1, shop_name, pdt_name1, pdtNameInfo, pdtInfo, cart_msg;
    String payNo = "", reserve_time = "";
    int total_price, classify, menu_count_num;
    Calendar cal = new GregorianCalendar(Locale.KOREA);
    /*int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH)+1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);*/
    // PayMethod payMethod = PayMethod.phone;
    String pg = "html5_inicis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment);
        mContext = this;
        getData();

        menu_count_num = Integer.parseInt(menu_count);
        getPdtName(pdtInfo);

        closePayment = (Button) findViewById(R.id.close_payment);
        chatTvPay = (TextView) findViewById(R.id.chatTv_pay);
        Iamport.INSTANCE.init(this);

        IamPortRequest request
                = IamPortRequest.builder()
//                html5_inicis: 이니시스웹표준, kakaopay: 카카오페이, chai: 차이페이
                .pg(pg)
//                PayMethod.card: 카드, .phone: 휴대폰소액결제, .samsung: 삼성페이, .vbank: 가상계좌
                .pay_method(PayMethod.card)
//                제품명
                .name(pdtNameInfo)
//                merchant_uid : 가맹점 관리 고유번호, 1970 년 1 월 1 일 00:00:00 GTM 이후의 밀리 초 수를 반환 (다른 형식으로 변경가능)
                .merchant_uid("mid_" + (new Date()).getTime())
//                가격
                .amount(Integer.toString(total_price))
                .buyer_tel("")     // 로그인할때 서버로부터 번호 받아와서 아이디처럼 저장해놓아야함
                .buyer_email(ShopActivity.member_email)      // 로그인할때 서버로부터 이메일 받아와서 아이디처럼 저장
//                .m_redirect_url("payment")
//                .app_scheme("payment")
//                디지털은 소액결제로 설정할 경우 필수항목: 컨텐츠인지 실물인지 여부제출 기본값 false
                .digital(false)
                .buyer_name(ShopActivity.member_name).build(); // 로그인할때 서버로부터 이름받아와서 아이디처럼 저장해놓아야함

        Iamport.INSTANCE.payment("imp99137995", request,
                iamPortApprove -> {
                    // (Optional) 차이 최종 결제전 콜백 함수.
                    return Unit.INSTANCE;
                }, iamPortResponse -> {
                    // 최종 결제 후 콜백함수
                    Log.d("sample", "결과 입니다." + iamPortResponse.toString());
                    if(iamPortResponse.getImp_success() == true){
//                        결제 성공시
                        payNo = iamPortResponse.getImp_uid();

                        IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                                "STX"+"MS06"+"00"
                                        +cal.get(Calendar.YEAR)+"-"+month(cal.get(Calendar.MONTH)+1)+"-"+day(cal.get(Calendar.DAY_OF_MONTH))
                                        +hour(cal.get(Calendar.HOUR_OF_DAY))+":"+min(cal.get(Calendar.MINUTE))+":"+sec(cal.get(Calendar.SECOND))
                                        +"ID="+ShopActivity.member_id
                                        +"SHOP="+shop_name
                                        +"COUNT="+menu_count
                                        +pdtInfo
                                        +"PRICE="+final_string_total_price(total_price)
                                        +"MSG="+cart_msg
                                        +"PAYNO="+payNo
                                        +"H01="+ShopActivity.member_name
                                        +"H02="+ShopActivity.member_phone
                                        +"CLS="+classify
                                        +"ETC="+reserve_time
                                        +"ETX"
                        );
                        mt.start();
                    }else if(iamPortResponse.getImp_success() == false){
//                        결제 취소시 취소한 화면 생성 + 뒤로가는 버튼
                        btnClosePayment(closePayment);
                    }
                    return Unit.INSTANCE;
                });
    } // onCreate()

    @Override
    public void onDestroy() {
        super.onDestroy();
        Iamport.INSTANCE.close();
    }

    private void getData(){
        if(getIntent().hasExtra("classify")){
            shop_name = getIntent().getStringExtra("shop_name");
            menu_count = getIntent().getStringExtra("menu_count");
            pdtInfo = getIntent().getStringExtra("pdtInfo");
            total_price = getIntent().getIntExtra("total_price",999999);
            reserve_time = getIntent().getStringExtra("reserve_time");
            cart_msg = getIntent().getStringExtra("cart_msg");
            classify = getIntent().getIntExtra("classify",1);
        }else{
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }
    }

    private void getPdtName(String pdtInfo) {
        int idxD01 = pdtInfo.indexOf("D01=");
        int idxD02 = pdtInfo.indexOf("D02=");

        if(menu_count_num == 1){
            pdtNameInfo = pdtInfo.substring(idxD01 + 4, idxD02);
        }else {
            pdtNameInfo = pdtInfo.substring(idxD01 + 4, idxD02) + " 외 " + (menu_count_num - 1) + "건";
        }
    }

    public void btnClosePayment(View v){
        finish();
    }

    private String final_string_total_price(int total_price) {
        if(total_price <1){
            return "000000";
        }else if(total_price < 10){
            return "00000"+total_price;
        }else if(total_price<100){
            return "0000"+total_price;
        }else if (total_price < 1000){
            return "000"+total_price;
        }else if (total_price < 10000){
            return "00"+total_price;
        }else if(total_price < 100000){
            return "0"+total_price;
        }else{
            return ""+total_price;
        }
    }

    private String string_total_price(int total_price) {
        if(total_price <1){
            return "00000";
        }else if(total_price < 10){
            return "0000"+total_price;
        }else if(total_price<100){
            return "000"+total_price;
        }else if (total_price < 1000){
            return "00"+total_price;
        }else if (total_price < 10000){
            return "0"+total_price;
        }else{
            return ""+total_price;
        }
    }

    private String stringChoice(int choice) {
        if(choice<10){
            return "0"+choice;
        }
        else{
            return ""+choice;
        }
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
    private String intToTwoString(int num){
        String str = "";
        if(num<10){str = "0"+num;}
        else{str = ""+num;}
        return str;
    }

    public void backToShop(String read){
        if(read.contains("STXMS0600")){
            Intent intent = new Intent(this, Order_NumActivity.class);
            intent.putExtra("MS06MSG", read);
            startActivity(intent);
            finish();
        }
        else if(read.contains("STXMS0601")){
            chatTvPay.setText("네트워크 오류, 지점에 문의해주세요");
        }
        else{
            chatTvPay.setText("네트워크 오류, 지점에 문의해주세요");
        }
    }
}