package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
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

    public static Context mContext;
    private Handler mHandler;

    TextView chatTV;
    public EditText ID;
    String member_name, member_email, member_phone;
    EditText Password;
    Button requestLogin;
    ImageView login_clogo;
    LinearLayout login_cafe;
    int j = 0;
    IntroActivity intro;
    long first_time; // 첫번 째 눌렀을 때 시간
    long second_time; // 두번 째 눌렀을 때 시간
    int msg_flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mContext = this;
        mHandler = new Handler();

        ID = (EditText) findViewById(R.id.ID);
        Password = (EditText) findViewById(R.id.Password);
        requestLogin = (Button) findViewById(R.id.requestLogin);
        chatTV = (TextView) findViewById(R.id.chatTV);

        login_clogo = (ImageView) findViewById(R.id.login_clogo);
        login_cafe = (LinearLayout) findViewById(R.id.login_cafe);

        intro = (IntroActivity) IntroActivity.intro_activity; // 객체를 만든다.
        msg_flag = -1;

        // 로그인 버튼을 눌렀을때
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
                    requestLogin.setEnabled(false);
                    IntroActivity.MsgThread mt = new IntroActivity.MsgThread("STX"+"MS02"+"00"+"02"+"D01="+ID.getText().toString()+"D02="+Password.getText().toString()+"ETX");
                    mt.start();
                    if(msg_flag == 1){
                        Toast.makeText(LoginActivity.this, "아이디가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        requestLogin.setEnabled(true);
                    }
                    else if(msg_flag == 2){
                        Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        requestLogin.setEnabled(true);
                    }else if(msg_flag > 2){
                        Toast.makeText(LoginActivity.this, "서버와의 연결이 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                        requestLogin.setEnabled(true);
                    }
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

    @Override
    protected void onDestroy() {
        if(msg_flag == 0){
            Toast.makeText(mContext, ID.getText().toString()+"님 안녕하세요!", Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
    }

    public void goShop(String read){
        if(read.contains("STXMS0200")){
            chatTV.setText("로그인 중입니다");
            saveMemberInfo(read);
            msg_flag = 0;
            // 응답파싱
            Intent intent = new Intent(this, ShopActivity.class);
            intent.putExtra("member_id",ID.getText().toString());
            intent.putExtra("member_name",member_name);
            intent.putExtra("member_email",member_email);
            intent.putExtra("member_phone",member_phone);
            startActivity(intent);
            finish();
        }
        else if(read.contains("STXMS0201")){
            chatTV.setText("아이디가 일치하지 않습니다");
            msg_flag = 1;
        }
        else if(read.contains("STXMS0202")){
            chatTV.setText("비밀번호가 일치하지 않습니다");
            msg_flag = 2;
        }
        else if(read.equals(null) || read.equals("")){
            chatTV.setText("빈값");
            msg_flag = 3;
        }
        else{
            chatTV.setText("서버 오류");
            msg_flag = 4;
        }
    }

    // name, phone, email 정보를 파싱해서 변수에 저장
    private void saveMemberInfo(String receive){
        int idxName = receive.indexOf("NAME=");
        int idxEmail = receive.indexOf("EMAIL=");
        int idxPhone = receive.indexOf("PHONE=");
        int idxEnd = receive.indexOf("ETX");
        member_name = receive.substring(idxName+5, idxEmail);
        member_email = receive.substring(idxEmail+6, idxPhone);
        member_phone = receive.substring(idxPhone+6, idxEnd);
    }
}