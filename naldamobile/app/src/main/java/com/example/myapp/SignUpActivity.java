package com.example.myapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity {

    private Handler mHandler;
    Socket socket;
//     실제 서버
    private String ip = "210.114.12.66";
    private int port = 1387;
    // 내컴퓨터 테스트용
//    private String ip = "192.168.0.60";
//    private int port = 1001;
    EditText fullName;
    EditText password;
    EditText confirmPassword;
    EditText Name;
    String genderS;
    RadioButton genderM;
    RadioButton genderF;
    EditText userEmailId;
    EditText mobileNumber;
    EditText address1;
    EditText address2;
    EditText post_code;
    EditText birth;
    Button signUpBtn;
    TextView signUpTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);



        mHandler = new Handler();
        fullName = (EditText) findViewById(R.id.fullName);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        Name = (EditText) findViewById(R.id.Name);
        genderM = (RadioButton) findViewById(R.id.genderM);
        genderF = (RadioButton) findViewById(R.id.genderF);
        userEmailId = (EditText) findViewById(R.id.userEmailId);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        post_code = (EditText) findViewById(R.id.post_code);
        birth = (EditText) findViewById(R.id.birth);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpTV = (TextView) findViewById(R.id.signUpTV);

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fullName.getText().toString() == null || fullName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(fullName.getText().toString()).matches()){
                    Toast.makeText(getApplicationContext(), "아이디를 이메일 형식으로 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString() == null || password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(!(confirmPassword.getText().toString().equals(password.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                else if(Name.getText().toString() == null || Name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(genderS == null || genderS.equals("")){
                    Toast.makeText(getApplicationContext(), "성별을 선택하세요", Toast.LENGTH_SHORT).show();
                }
                else if(userEmailId.getText().toString() == null || userEmailId.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(userEmailId.getText().toString()).matches()){
                    Toast.makeText(getApplicationContext(), "이메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(mobileNumber.getText().toString() == null || mobileNumber.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(address1.getText().toString() == null || address1.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "주소를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(address2.getText().toString() == null || address2.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "주소를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(post_code.getText().toString() == null || post_code.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "우편번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(birth.getText().toString() == null || birth.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "주민등륵번호 앞 6자리를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    ConnectThread th = new ConnectThread();
                    th.start();

//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent);

                }
            }
        });
    }

    class ConnectThread extends Thread{
        String dataCount = "10";
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                String sndMsg =
                        "STX"
                                +"MS01"
                                +"00"
                                +dataCount
                                +"D01="+fullName.getText().toString()
                                +"D02="+password.getText().toString()
                                +"D03="+Name.getText().toString()
                                +"D04="+genderS
                                +"D05="+mobileNumber.getText().toString()
                                +"D06="+address1.getText().toString()
                                +"D07="+address2.getText().toString()
                                +"D08="+post_code.getText().toString()
                                +"D09="+birth.getText().toString()
                                +"D10="+userEmailId.getText().toString()
                                +"ETX";

                Log.d("=============", sndMsg);
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
    }

    public void onRadioButtonClicked(View view){
        // 체크된 버튼
        boolean checked = ((RadioButton) view).isChecked();
        // 클릭된 라디오버튼 확인
        switch(view.getId()) {
            case R.id.genderM:
                if (checked)
                    genderS = "M";
                break;
            case R.id.genderF:
                if (checked)
                    genderS = "F";
                break;
        }
    }

    class msgUpdate implements Runnable {
        private String msg;
        public msgUpdate(String str) {
            this.msg = str;
        }
        public void run() {
            signUpTV.setText(signUpTV.getText().toString() + msg + "\n");
            if((signUpTV.getText().toString()).contains("STXMS0100")){
                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                finish();
            }else if((signUpTV.getText().toString()).contains("STXMS0101")){
                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
            }else if((signUpTV.getText().toString()).contains("STXMS0102")){
                Toast.makeText(getApplicationContext(), "오류02", Toast.LENGTH_LONG).show();
            }else if(signUpTV.getText().toString() == null || signUpTV.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "빈값", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "서버오류 발생", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 하단바 없애기
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}