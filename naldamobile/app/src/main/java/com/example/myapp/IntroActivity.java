package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.myapp.ProductFragment.FragmentParent1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

// 구글 플레이스토어와 버전체크해야함
public class IntroActivity extends AppCompatActivity {

    public static Activity intro_activity;
    public static LinearLayout intro_Ll;
//    기존 4초, 테스트를 위해 2초
    private static int SPLASH_SCREEN = 2000;
    public static Handler mHandler;
    public static Socket socket;
    public String ip = "210.114.12.66";
    public int port = 1387;
    // 내컴퓨터용
//    public String ip = "192.168.0.60";
//    public int port = "1001";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        // 구글 플레이스토어 버전체크 -> 소켓연결

        mHandler = new Handler();
        // 아웃트로
        intro_activity = IntroActivity.this; // 액티비티의 정보를 저장한다.
        intro_Ll = (LinearLayout) findViewById(R.id.intro_Ll);

        ConnectServer cs = new ConnectServer();
        cs.start();

        // 페이지 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(IntroActivity.this);
                    startActivity(intent, options.toBundle());
                }else{
                    startActivity(intent);
                }
            }
        }, SPLASH_SCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ConnectServer extends Thread{
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static class MsgThread extends Thread{
        String sndMsg = "";
        public MsgThread(String sndMsg) {
            this.sndMsg = sndMsg;
        }

        @Override
        public void run() {
            try {
                Log.d("=============", sndMsg);
                // 전송
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-16le")), true);
                out.println(sndMsg);
                //수신
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-16le"));
                String read = input.readLine();
                mHandler.post(new MsgUpdate(read));
                Log.d("=============", read);

                // 로그인
                if(read.contains("STXMS02") && sndMsg.contains("STXMS02")){
                    ((LoginActivity)LoginActivity.mContext).goShop(read);
                }
                // 회원가입
                else if(read.contains("STXMS01") && sndMsg.contains("STXMS01")){
                    ((SignUpActivity)SignUpActivity.mContext).signUpServer(read);
                }
                // 주문요청
                else if(read.contains("STXMS04") && sndMsg.contains("STXMS04")){
                    ((ProductActivity)ProductActivity.mContext).orderSuccess(read+sndMsg.substring(sndMsg.indexOf("COUNT="),sndMsg.indexOf("ETX")));
                }
                // 예약요청
                else if(read.contains("STXMS05") && sndMsg.contains("STXMS05")){
                    ((ProductActivity)ProductActivity.mContext).reserveSuccess(read+sndMsg.substring(sndMsg.indexOf("STXMS05")+9,sndMsg.indexOf("ETX")));
                }
                // 결제요청
                else if(read.contains("STXMS06") && sndMsg.contains("STXMS06")){
                    ((Order_NumActivity)Order_NumActivity.mContext).backToShop(read);
                }
                // 결제리스트 요청
                else if(read.contains("STXMS07") && sndMsg.contains("STXMS07")){
                    ((HistoryActivity)HistoryActivity.mContext).printHistory(read);
                }
                // 결제상세내역 요청
                else if(read.contains("STXMS08") && sndMsg.contains("STXMS08")){
                    ((HistoryActivity)HistoryActivity.mContext).requestDetailInfo(read);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    static class MsgUpdate implements Runnable{
        private String read;
        public MsgUpdate(String read){
            this.read = read;
        }
        @Override
        public void run() {
            read += "\n";
        }
    }
}