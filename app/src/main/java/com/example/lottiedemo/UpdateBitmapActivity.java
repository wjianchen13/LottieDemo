package com.example.lottiedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.TextDelegate;

public class UpdateBitmapActivity extends AppCompatActivity {

    private LottieAnimationView lavTest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        lavTest = findViewById(R.id.lav_test);
    }

    public void onTest(View v) {

        lavTest.setImageAssetsFolder("assets/");
        lavTest.setAnimation("test1.zip");
        ImageAssetDelegate imageAssetDelegate = new ImageAssetDelegate() {
            @Override
            public Bitmap fetchBitmap(LottieImageAsset asset) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_01);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }
        };
        lavTest.setImageAssetDelegate(imageAssetDelegate);

        lavTest.setRepeatMode(LottieDrawable.REVERSE);//设置播放模式
        lavTest.setRepeatCount(LottieDrawable.INFINITE);//设置重复次数
        lavTest.playAnimation();



    }

    public void onTest1(View v) {

    }
    
}
