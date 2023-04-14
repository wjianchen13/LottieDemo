package com.example.lottiedemo.rich;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.lottiedemo.DobyApp;
import com.example.lottiedemo.R;
import com.example.lottiedemo.rich.ems.EmResManager;
import com.example.lottiedemo.rich.other.ExpressionUtil;
import com.example.lottiedemo.rich.utils.RichTextView;

public class RichActivity extends AppCompatActivity {

    private RichTextView tvTest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich);
        tvTest = findViewById(R.id.tv_test);
        EmResManager.getInstance().check();
    }

    public void onTest(View v) {
        String content = "[em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2]";
        tvTest.setText(ExpressionUtil.getExpressionString(DobyApp.app(), content));
        
    }
    
}
