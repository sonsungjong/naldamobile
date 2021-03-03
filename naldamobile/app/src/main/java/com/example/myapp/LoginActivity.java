package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


// 로그인될때 ini 버전체크해서 http 통신으로 assets 폴더에 ShopInfo.ini 파일다운받기 (1. 로그인허용 2.자동로그인) -> 버전이 다른데 다운을 못받으면 다이얼로그 띄우고 finish(); 앱종료
public class LoginActivity extends AppCompatActivity {

    private Handler mHandler;
    Socket socket;
//    // 실제 서버
    private String ip = "210.114.12.66";
    private int port = 1387;
    // 내컴퓨터 테스트용
//    private String ip = "192.168.0.60";
//    private int port = 1001;

    TextView chatTV;
    EditText ID;
    EditText Password;
    Button requestLogin;
    ImageView login_clogo;
    LinearLayout login_cafe;
    int j = 0;
    IntroActivity intro;
    long first_time; // 첫번 째 눌렀을 때 시간
    long second_time; // 두번 째 눌렀을 때 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mHandler = new Handler();

        ID = (EditText) findViewById(R.id.ID);
        Password = (EditText) findViewById(R.id.Password);
        requestLogin = (Button) findViewById(R.id.requestLogin);
        chatTV = (TextView) findViewById(R.id.chatTV);

        login_clogo = (ImageView) findViewById(R.id.login_clogo);
        login_cafe = (LinearLayout) findViewById(R.id.login_cafe);

        intro = (IntroActivity) IntroActivity.intro_activity; // 객체를 만든다.

        requestLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ID.getText().toString() == null || ID.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(Password.getText().toString() == null || Password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    ConnectThread th = new ConnectThread();
                    th.start();
                }
            }
        });

        // 배경 클릭시 이미지 변경
        login_clogo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(j == 0) {
                    login_cafe.setBackgroundResource(R.drawable.login_origin);;
                    j++;
                }else {
                    login_cafe.setBackgroundResource(R.drawable.login_cafe);
                    //login_clogo.setImageResource(R.drawable.login_ologo);
                    j = 0;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void signUpBtn(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        second_time = System.currentTimeMillis(); // System.currentTimeMillis() 현재 시각을 밀리 초로 바꾼 값을 알려줌
        Toast.makeText(LoginActivity.this, "한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show();
        if(second_time - first_time < 2000){
            intro.finish();
            super.onBackPressed();
        }
        first_time = System.currentTimeMillis();
    }

    class ConnectThread extends Thread{
        String dataCount = "02";
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                // 보낼 메시지
                String sndMsg =
                        "STX"
                                +"MS02"
                                +"00"
                                +dataCount
                                +"D01="+ID.getText().toString()
                                +"D02="+Password.getText().toString()
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
            chatTV.setText(chatTV.getText().toString() + msg + "\n");
            if((chatTV.getText().toString()).contains("STXMS0200")){
                Toast.makeText(getApplicationContext(), "로그인", Toast.LENGTH_LONG).show();
                // ini파일 버전체크 후 다운받기 -> 버전이 다른데 다운을 못받으면 다이얼로그 띄우고 finish(); 앱종료

                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                intent.putExtra("member_id", ID.getText().toString());
                startActivity(intent);
                finish();
            }else if((chatTV.getText().toString()).contains("STXMS0201")){
                Toast.makeText(getApplicationContext(), "아이디가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
            }else if((chatTV.getText().toString()).contains("STXMS0202")){
                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
            }else if(chatTV.getText().toString() == null || chatTV.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "빈값", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "서버오류 발생", Toast.LENGTH_LONG).show();
            }
        }
    }
}