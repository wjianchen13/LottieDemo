package com.example.lottiedemo.rich.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

import java.lang.ref.SoftReference;

/**
 * Created by rgy on 2021/3/24 0024.
 * 动画span
 */
public class AsyAnimDrawableSpan extends ImageSpanC {

    private static final String TAG = AsyAnimDrawableSpan.class.getSimpleName();

    @NonNull SoftReference<Context> mContext;
    @NonNull IAsyAnimDrawable mDrawable;

    public AsyAnimDrawableSpan(Context context, @NonNull IAsyAnimDrawable drawable, int spanAlign){
        super(drawable.getTarget(), spanAlign);
        this.mContext = new SoftReference<>(context);
        mDrawable = drawable;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
//        _95L.iTag("no_pic_draw", "span draw-it-" + mDrawable.getName());
    }

    @Override
    public Drawable getDrawable() {
        return mDrawable.getTarget();
    }

    public void attach(@NonNull View view) {
        mDrawable.attach(view);
    }

    public void detach(@NonNull View view) {
        mDrawable.detach(view);
    }

}
