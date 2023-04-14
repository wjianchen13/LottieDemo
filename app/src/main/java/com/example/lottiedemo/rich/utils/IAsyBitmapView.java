package com.example.lottiedemo.rich.utils;

import android.graphics.drawable.Drawable;

public interface IAsyBitmapView extends Drawable.Callback {
    void invalidate();
    void onDrawableLoadCompleted(IAsyBitmapDrawable drawable);
    boolean isVisible();
    boolean isAttachToWindow();
    void checkAttachOrDetach(boolean isFromVisibleChanged);
    String name();
}
