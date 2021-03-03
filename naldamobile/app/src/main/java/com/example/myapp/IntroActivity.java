package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;

// 구글 플레이스토어와 버전체크해야함
public class IntroActivity extends AppCompatActivity {

    public static Activity intro_activity;
    public static LinearLayout intro_Ll;
//    기존 4초, 테스트를 위해 2초
    private static int SPLASH_SCREEN = 2000;
    public Socket socket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        // 구글 플레이스토어 버전체크 -> 소켓연결

        intro_activity = IntroActivity.this; // 액티비티의 정보를 저장한다.
        intro_Ll = (LinearLayout) findViewById(R.id.intro_Ll);
//      startService(new Intent(getApplicationContext(), SocketClass.class));

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
    }
}