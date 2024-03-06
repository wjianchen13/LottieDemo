package com.example.lottiedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lottiedemo.rich.RichActivity;

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

    /**
     * json动画富文本
     * @param v
     */
    public void onRich(View v) {
        startActivity(new Intent(this, RichActivity.class));
    }


    /**
     * json动画富文本，改变文本
     * @param v
     */
    public void onDynamicRich(View v) {
        startActivity(new Intent(this, DynamicLottieDrawableActivity.class));
    }

    /**
     * json动画 替换图片
     * @param v
     */
    public void onBitmap(View v) {
        startActivity(new Intent(this, UpdateBitmapActivity.class));
    }



}
