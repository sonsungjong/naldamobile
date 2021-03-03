package com.example.myapp;

import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClass extends Service {

    private final IBinder mBinder = new LocalBinder();
    private int number;

    class LocalBinder extends Binder{
        SocketClass getService(){
            return SocketClass.this;
        }
    }

    Socket socket;
    private Handler mHandler;
    ConnectThread th;

    // 실제 서버
//    private String ip = "210.114.12.66";
//    private int port = 1387;
    // 내컴퓨터 테스트용
    private String ip = "192.168.0.60";
    private int port = 1001;

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스가 최초 생설될 때만 호출
        // onStartCommand() 또는 onBind() 호출 전에 가장 먼저 호출됨
        number = 0;
        mHandler = new Handler();
        th = new ConnectThread();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // startService()를 액티비티에서 호출하여 실행, 계속 실행됨(세션처리 필요)
        // 백그라운드에서 무한히 실행 stopSelf() 사용 또는 stopService() 를 액티비티에서 호출해서 해제
        th.start();
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // bindService()를 액티비티에서 써서 호출, 해당 구성요소에 바인딩되어 실행
        
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // unbindService()로 바인딩을 해제할 때 호출
        
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        // 이미 onUnbind()가 호출된 후에 bindService()로 바인딩을 실행할 때 호출

        super.onRebind(intent);
    }

//    PendingIntent pi = PendingIntent.getBroadcast();

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service end", Toast.LENGTH_SHORT).show();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 서비스를 소멸시킬때 호출, 각종 리소스 정리하기위해 구현하는 곳
    }

    class ConnectThread extends Thread{
        String socMsg;
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                while(true) {
                    // 보낼 메시지
                    String sndMsg =
                            "STX" + socMsg + "ETX";
                    Log.d("=============", sndMsg);

                    // 전송
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-16le")), true);
                    out.println(sndMsg);

                    // 수신
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-16le"));
                    String read = input.readLine();
                    mHandler.post(new msgUpdate(read));

                    // 테스트용 : 화면출력
                    Log.d("=============", read);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    class msgUpdate implements Runnable {
        private String msg;
        public msgUpdate(String msg) {
            this.msg = msg;
        }
        public void run() {
            if(msg.contains("STXMS0200")){

            }else if(msg.contains("STXMS0200")) {

            }else{

            }
        }
    }

    int getNUmber(){
        return number++;
    }
}