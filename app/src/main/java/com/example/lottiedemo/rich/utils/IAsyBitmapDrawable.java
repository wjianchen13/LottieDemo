package com.example.lottiedemo.rich.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by rgy on 2021/4/15 0015.
 * 异步获取资源的drawable
 */
public interface IAsyBitmapDrawable {
    void attach(@NonNull View view);
    void detach(@NonNull View view);
    boolean isLoadCompleted();
    Drawable getTarget();
    void setCallback(Drawable.Callback callback);
    Drawable.Callback getCallback();
    Bitmap getBitmap();

    /**
     * 下载完成的回调
     * @param callback
     */
    void addLoadCallback(ICallBack<Drawable> callback);
    String getName();
}
