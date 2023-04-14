package com.example.lottiedemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class UrlActivity extends AppCompatActivity {

    private LottieAnimationView lavTest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        lavTest = findViewById(R.id.lav_test);
    }

    public void onTest(View v) {
        lavTest.setAnimationFromUrl("https://cqz-1256838880.cos.ap-shanghai.myqcloud.com/bird1.json");
        lavTest.setRepeatMode(LottieDrawable.REVERSE);
        lavTest.setRepeatCount(LottieDrawable.INFINITE);
        lavTest.playAnimation();
        
    }
    
}
