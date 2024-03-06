package com.example.lottiedemo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.TextDelegate;

public class NormalActivity extends AppCompatActivity {

    private LottieAnimationView lavTest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        lavTest = findViewById(R.id.lav_test);
    }

    public void onTest(View v) {
//        lavTest.setImageAssetsFolder("assets/");
//        lavTest.setAnimation("AndroidWave.json");
//        lavTest.setRepeatMode(LottieDrawable.REVERSE);//设置播放模式
//        lavTest.setRepeatCount(LottieDrawable.INFINITE);//设置重复次数
//        lavTest.playAnimation();

        lavTest.setImageAssetsFolder("assets/");
        lavTest.setAnimation("333.zip");
        TextDelegate textDelegate = new TextDelegate(lavTest);
        lavTest.setTextDelegate(textDelegate);
        textDelegate.setText("img_0", "替换文字");
        lavTest.setRepeatMode(LottieDrawable.REVERSE);//设置播放模式
        lavTest.setRepeatCount(LottieDrawable.INFINITE);//设置重复次数
        lavTest.playAnimation();


//        lavTest.setImageAssetsFolder("assets/");
//        lavTest.setAnimation("DynamicText.json");
//        lavTest.setRepeatMode(LottieDrawable.RESTART);//设置播放模式
//        lavTest.setRepeatCount(LottieDrawable.INFINITE);//设置重复次数、
////        lavTest.setFontAssetDelegate(new FontAssetDelegate() {
////            @Override
////            public Typeface fetchFont(String fontFamily) {
////                return null;
////            }
////        });
//        TextDelegate textDelegate = new TextDelegate(lavTest);
//        lavTest.setTextDelegate(textDelegate);
//        textDelegate.setText("NAME", "askfjaskjdf");
//        lavTest.playAnimation();


    }
    
}
