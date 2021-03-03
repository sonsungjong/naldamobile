package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.view.WindowManager;
import android.webkit.WebView;
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

import kotlin.Unit;

public class PaymentActivity extends AppCompatActivity {

    // orderNum 혹은 reserveResult 쪽에서 소켓으로 결제완료메시지 보내기
    // PaymentActivity 없애고 Product Fragment쪽에서 직접 결제하게하기
    // 장바구니에서 직접처리하게 하기

    //     실제 서버
    private String ip = "210.114.12.66";
    private int port = 1387;
    // 내컴퓨터 테스트용
//    private String ip = "192.168.0.60";
//    private int port = 1001;
//
    String member_id, menu_count, type, shop_name, pdt_name;
    String payNo = "", reserve_time = "";
    int choice, amount, total_price;
    int classify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //      상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment);

        Iamport.INSTANCE.init(this);
        getData();

//        mHandler = new Handler();

        // 액션바를 얻어와서 hide로 숨김 : NoActionBar와 곂쳐서 오류
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        IamPortRequest request
                = IamPortRequest.builder()
//                html5_inicis: 이니시스웹표준, kakaopay: 카카오페이, chai: 차이페이
                .pg("html5_inicis")
//                PayMethod.card: 카드, .phone: 휴대폰소액결제, .samsung: 삼성페이, .vbank: 가상계좌
                .pay_method(PayMethod.card)
//                제품명
                .name(pdt_name)
//                merchant_uid : 가맹점 관리 고유번호, 1970 년 1 월 1 일 00:00:00 GTM 이후의 밀리 초 수를 반환 (다른 형식으로 변경가능)
                .merchant_uid("mid_" + (new Date()).getTime())
//                가격
                .amount(Integer.toString(total_price))
                .buyer_tel("010-2580-2463")     // 로그인할때 서버로부터 번호 받아와서 아이디처럼 저장해놓아야함
                .buyer_email("tbxmtbfm@naver.com")      // 로그인할때 서버로부터 이메일 받아와서 아이디처럼 저장
//                .m_redirect_url("payment")
//                .app_scheme("payment")
//                디지털은 소액결제로 설정할 경우 필수항목: 컨텐츠인지 실물인지 여부제출 기본값 false
                .digital(false)
                .buyer_name("손성종").build(); // 로그인할때 서버로부터 이름받아와서 아이디처럼 저장해놓아야함

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

                        Intent intent = new Intent(this, Order_NumActivity.class);
                        intent.putExtra("menu_count",menu_count);
                        intent.putExtra("pdt_name",pdt_name);
                        intent.putExtra("classify",classify);
                        intent.putExtra("type",type);
                        intent.putExtra("choice",choice);
                        intent.putExtra("amount",amount);
                        intent.putExtra("payNo",payNo);
                        intent.putExtra("total_price",total_price);
                        startActivity(intent);
                        finish();
                    }else{
//                        결제 취소시
                        finish();
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
            member_id = getIntent().getStringExtra("member_id");
            shop_name = getIntent().getStringExtra("shop_name");
            menu_count = getIntent().getStringExtra("menu_count");
            pdt_name = getIntent().getStringExtra("pdt_name");
            type = getIntent().getStringExtra("type");
            choice = getIntent().getIntExtra("choice",0);
            amount = getIntent().getIntExtra("amount",10);
            total_price = getIntent().getIntExtra("total_price",99);
            classify = getIntent().getIntExtra("classify",0);
        }else{
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }
    }
//    private void setData(){
//
//    }
/*
    class ConnectThread extends Thread{
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
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                // 보낼 메시지
                String sndMsg =
                        "STX"
                                +"MS06"
                                +"00"
                                +"D01="+year+"-"+month(month)+"-"+day(day)
                                +"D02="+hour(hour)+":"+min(min)+":"+sec(sec)
                                +"D03="+member_id
                                +"D04="+shop_name
                                +"D05="+menu_count
                                +"D06="+pdt_name
                                +"D07="+type
                                +"D08="+choice
                                +"D09="+amount
                                +"D10="+total_price
                                +"D11="+cancel_info
                                +"ETX";
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

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class msgUpdate implements Runnable {
        private String msg;
        public msgUpdate(String str) {
            this.msg = str;
        }
        public void run() {
            if(msg.contains("STXMS0600ETX")){
                Toast.makeText(PaymentActivity.this, "결재 완료", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(PaymentActivity.this, "결재 오류", Toast.LENGTH_SHORT).show();
            }
        }
    }*/


}
