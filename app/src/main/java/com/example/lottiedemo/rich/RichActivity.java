package com.example.lottiedemo.rich;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.lottiedemo.DobyApp;
import com.example.lottiedemo.R;
import com.example.lottiedemo.rich.ems.EmResManager;
import com.example.lottiedemo.rich.other.BadgeUtils;
import com.example.lottiedemo.rich.other.ExpressionUtil;
import com.example.lottiedemo.rich.utils.RichTextView;

public class RichActivity extends AppCompatActivity {

    private RichTextView tvTest1;
    private RichTextView tvTest2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich);
        tvTest1 = findViewById(R.id.tv_test1);
        tvTest2 = findViewById(R.id.tv_test2);
        EmResManager.getInstance().check();
    }

    public void onTest1(View v) {
//        String content = "[em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2]";
        String content = "[em:0]";
        tvTest1.setText(ExpressionUtil.getExpressionString(DobyApp.app(), content));
        
    }

    public void onTest2(View v) {
//        String content = "[em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2][em:0][em:1][em:2]";
//        String content = "[em:0]";
//        tvTest2.setText(ExpressionUtil.getExpressionString(DobyApp.app(), content));
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String content = "[em:0]";
        SpannableString str = BadgeUtils.getBadgeString(DobyApp.app(), content);
        sb.append(str);
        String content1 = "[em:1]";
        SpannableString str1 = BadgeUtils.getBadgeString(DobyApp.app(), content1);
        sb.append(str1);
        String content2 = "[em:2]";
        SpannableString str2 = BadgeUtils.getBadgeString(DobyApp.app(), content2);
        sb.append(str2);
        tvTest2.setText(sb);

    }
    
}
