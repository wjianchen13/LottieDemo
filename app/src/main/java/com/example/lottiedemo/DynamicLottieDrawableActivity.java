package com.example.lottiedemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.airbnb.lottie.TextDelegate;
import com.example.lottiedemo.utils.ScreenUtils;
import com.example.lottiedemo.view.ImageSpanAlign;
import com.example.lottiedemo.view.ImageSpanC;

public class DynamicLottieDrawableActivity extends AppCompatActivity {

//    private LottieAnimationView lavTest;
    private TextView tvTest;
    private LottieDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_drawable);
//        lavTest = findViewById(R.id.lav_test);
        tvTest = findViewById(R.id.tv_test);
    }

    public void onTest(View v) {
//        final LottieDrawable drawable = new LottieDrawable();
//        LottieComposition  composition = LottieCompositionFactory.fromAssetSync(this, "AndroidWave.json").getValue();
//        drawable.setComposition(composition);
//        lavTest.setImageDrawable(drawable);
//        lavTest.playAnimation();

        drawable = new LottieDrawable();
        drawable.setImagesAssetsFolder("assets/");
        LottieComposition.Factory.fromAssetFileName(this, "data.json",
                new OnCompositionLoadedListener() {
                    @Override
                    public void onCompositionLoaded(@Nullable LottieComposition composition) {
                        drawable.setComposition(composition);
                        drawable.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                tvTest.invalidate();
                            }
                        });


                        TextDelegate textDelegate = new TextDelegate(drawable);
                        textDelegate.setText("txt_1", "🔥💪💯");
                        drawable.setTextDelegate(textDelegate);

                        LottieDrawable drawable1 = new LottieDrawable();
                        drawable1.setImagesAssetsFolder("assets/");
                        drawable1.setComposition(composition);
                        TextDelegate textDelegate1 = new TextDelegate(drawable);
                        textDelegate1.setText("txt_1", "🔥💪💪💪💪💪💪💯");
                        drawable1.setTextDelegate(textDelegate1);
                        drawable1.setRepeatCount(LottieDrawable.INFINITE);
                        drawable1.setCallback(tvTest);
                        drawable1.playAnimation();


                        SpannableStringBuilder sb = new SpannableStringBuilder();
                        sb.append("hello");
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 50)));
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable1, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 50)));
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 50)));
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 25)));
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 25)));
                        sb.append("hello");
                        sb.append("hello");
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 50)));
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 50)));
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 50)));
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 25)));
                        sb.append(getScaleImageSpan(DynamicLottieDrawableActivity.this, drawable, ScreenUtils.dip2px(DynamicLottieDrawableActivity.this, 25)));
                        tvTest.setText(sb);
//                        drawable.setRepeatMode(LottieDrawable.REVERSE);
                        drawable.setRepeatCount(LottieDrawable.INFINITE);
                        drawable.setCallback(tvTest);
                        drawable.playAnimation();
//                        lavTest.setComposition(composition);
//                        lavTest.setImageDrawable(drawable);
                    }
                });

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
//        textDelegate.setText("NAME", "🔥💪💯");
//        lavTest.playAnimation();
        
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(drawable != null) {
            drawable.removeAllAnimatorListeners();
            drawable.removeAllUpdateListeners();
        }
    }

    public void onTest2(View v) {

    }


    /**
     * 按照比例获取设定大小的image span
     *
     * @param context
     * @param
     * @param height 图片绘制高度 px
     * @param 
     */
    public SpannableString getScaleImageSpan(Context context, Drawable img, int height) {
        SpannableString spanStr = null;
        if (img != null && context != null) {
            try {
                setScaleBoundPix(height, img);

                ImageSpan imgSpan = createImageSpan(img, ImageSpanAlign.XIU_ALIGN_CENTER);
                spanStr = new SpannableString("img ");
                spanStr.setSpan(imgSpan, 0, spanStr.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return spanStr == null ? new SpannableString("") : spanStr;
    }


    public ImageSpan createImageSpan(Drawable drawable, int v) {
//        WebpDrawable webpDrawable = new WebpDrawable(LokApp.app(),R.raw.a1);
        return new ImageSpanC(drawable, v);
    }

    public float setScaleBoundPix(int height, @NonNull Drawable drawable){
        int intrinsicWidth = drawable.getIntrinsicWidth(); // 获取图片的宽度 px
        int intrinsicHeight = drawable.getIntrinsicHeight(); // 获取图片的高度 px
        float factor = (float)height / intrinsicHeight; // 转换因子
//        drawable.setBounds(0, 0, (int)(intrinsicWidth * factor), height);
        drawable.setBounds(0, 0, 300, 100);
        return factor;
    }
}
