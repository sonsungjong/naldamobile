package com.example.myapp;

import android.app.Application;
import com.iamport.sdk.domain.core.Iamport;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Iamport.INSTANCE.create(this);
    }
}
