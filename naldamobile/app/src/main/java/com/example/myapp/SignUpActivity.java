package com.example.myapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    public static Context mContext;
//    private Handler mHandler;

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
    String dataCount = "10";
    int msg_flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        mContext = this;
//        mHandler = new Handler();

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
        msg_flag = -1;

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fullName.getText().toString() == null || fullName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "???????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(fullName.getText().toString()).matches()){
                    Toast.makeText(getApplicationContext(), "???????????? ????????? ???????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString() == null || password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "??????????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
                else if(!(confirmPassword.getText().toString().equals(password.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "??????????????? ???????????? ????????????", Toast.LENGTH_SHORT).show();
                }
                else if(Name.getText().toString() == null || Name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
                else if(genderS == null || genderS.equals("")){
                    Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
                else if(userEmailId.getText().toString() == null || userEmailId.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "???????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(userEmailId.getText().toString()).matches()){
                    Toast.makeText(getApplicationContext(), "????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
                else if(mobileNumber.getText().toString() == null || mobileNumber.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "????????? ????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
                else if(address1.getText().toString() == null || address1.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
                else if(address2.getText().toString() == null || address2.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
                else if(post_code.getText().toString() == null || post_code.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
                else if(birth.getText().toString() == null || birth.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "?????????????????? ??? 6????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
                else {
                    IntroActivity.MsgThread mt = new IntroActivity.MsgThread(
                            "STX"+"MS01"+"00"+dataCount
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
                                    +"ETX"
                    );
                    mt.start();
                    if(msg_flag == 1){
                        Toast.makeText(SignUpActivity.this, "????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    }
                    else if(msg_flag >1){
                        Toast.makeText(SignUpActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(msg_flag == 0){
            Toast.makeText(SignUpActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
        }
        else{}
        super.onDestroy();
    }

    public void onRadioButtonClicked(View view){
        // ????????? ??????
        boolean checked = ((RadioButton) view).isChecked();
        // ????????? ??????????????? ??????
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

    // ????????? ?????????
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

    public void signUpServer(String read){
        if(read.contains("STXMS0100")){
            msg_flag = 0;
            finish();
        }
        else if(read.contains("STXMS0101")){
            msg_flag = 1;
            signUpTV.setText("????????? ??????????????????");
        }
        else if(read.equals(null) || read.equals("")){
            msg_flag = 2;
            signUpTV.setText("??????");
        }
        else{
            msg_flag = 3;
            signUpTV.setText("?????? ????????????");
        }
    }
}