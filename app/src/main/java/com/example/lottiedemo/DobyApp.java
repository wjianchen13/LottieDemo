package com.example.lottiedemo;

import android.app.Application;
import android.content.Context;

public class DobyApp extends Application {

    private static DobyApp mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        mApplication = this;
        super.attachBaseContext(base);
    }

    public static DobyApp app() {
        return mApplication;
    }


}
