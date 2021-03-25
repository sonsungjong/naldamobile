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

    String menu_count, type, shop_name, pdt_name;
    String payNo = "", reserve_time = "";
    int choice, amount, total_price;
    int classify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //      상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment);
        getData();
        Iamport.INSTANCE.init(this);


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
                        intent.putExtra("shop_name",shop_name);
                        intent.putExtra("menu_count",menu_count);
                        intent.putExtra("pdt_name",pdt_name);
                        intent.putExtra("type",type);
                        intent.putExtra("choice",choice);
                        intent.putExtra("amount",amount);
                        intent.putExtra("payNo",payNo);
                        intent.putExtra("total_price",total_price);
                        intent.putExtra("classify",classify);
                        intent.putExtra("reserve_time", reserve_time);
                        startActivity(intent);
                        finish();
                    }else if(iamPortResponse.getImp_success() == false){
//                        결제 취소시
                        this.finish();
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
            pdt_name = getIntent().getStringExtra("pdt_name");
            type = getIntent().getStringExtra("type");
            choice = getIntent().getIntExtra("choice",0);
            amount = getIntent().getIntExtra("amount",10);
            total_price = getIntent().getIntExtra("total_price",99);
            classify = getIntent().getIntExtra("classify",0);
            reserve_time = getIntent().getStringExtra("reserve_time");
        }else{
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }
    }
}
