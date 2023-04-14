package com.example.lottiedemo.rich.other;

import android.os.Handler;

public class HandleManager {

    private static HandleManager INSTANCE = null;

    private Handler mHandler;

    private HandleManager() {
        mHandler = new Handler();
    }

    public static HandleManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HandleManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HandleManager();
                }
            }
        }
        return INSTANCE;
    }

    public Handler getHandler() {
        return mHandler;
    }
}
