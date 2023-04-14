package com.example.lottiedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Assets 加载
    public void onAssets(View v) {
        startActivity(new Intent(this, NormalActivity.class));
    }

    // 网络加载
    public void onUrl(View v) {
        startActivity(new Intent(this, UrlActivity.class));
    }

    // LottieDrawable
    public void onLottieDrawable(View v) {
        startActivity(new Intent(this, LottieDrawableActivity.class));
    }
    
}
