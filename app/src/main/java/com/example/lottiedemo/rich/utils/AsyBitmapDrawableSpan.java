package com.example.lottiedemo.rich.utils;

import static com.example.lottiedemo.rich.utils.ImageSpanAlign.XIU_ALIGN_CENTER;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

/**
 * Created by rgy on 2021/4/13 0013.
 * 异步bitmapSpan
 */
public class AsyBitmapDrawableSpan extends ImageSpanC {

    @NonNull
    IAsyBitmapDrawable asyBitmapDrawable;

    public AsyBitmapDrawableSpan(IAsyBitmapDrawable asyBitmapDrawable, int align) {
        super(asyBitmapDrawable.getTarget(), align);
        this.asyBitmapDrawable =  asyBitmapDrawable;
    }

    public AsyBitmapDrawableSpan(IAsyBitmapDrawable asyBitmapDrawable) {
        this(asyBitmapDrawable, XIU_ALIGN_CENTER);
    }

    public void setCallback(Drawable.Callback callback){
        asyBitmapDrawable.setCallback(callback);
    }

    @Override
    public Drawable getDrawable() {
        return asyBitmapDrawable.getTarget();
    }
}
