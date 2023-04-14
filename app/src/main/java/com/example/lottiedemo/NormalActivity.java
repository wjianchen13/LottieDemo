package com.example.lottiedemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class NormalActivity extends AppCompatActivity {

    private LottieAnimationView lavTest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        lavTest = findViewById(R.id.lav_test);
    }

    public void onTest(View v) {
        lavTest.setImageAssetsFolder("assets/");
        lavTest.setAnimation("AndroidWave.json");
        lavTest.setRepeatMode(LottieDrawable.REVERSE);//设置播放模式
        lavTest.setRepeatCount(LottieDrawable.INFINITE);//设置重复次数
        lavTest.playAnimation();
        
    }
    
}
