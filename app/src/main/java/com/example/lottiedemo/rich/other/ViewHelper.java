package com.example.lottiedemo.rich.other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.example.lottiedemo.rich.utils.CallbackView;
import com.example.lottiedemo.rich.utils.RichTextView;

import java.lang.ref.WeakReference;
import java.util.Set;


/**
 * Created by Administrator on 2017/6/22 0022.
 */

public final class ViewHelper {

    public static void clear(View v) {

        if (v == null)
            return;

        ViewCompat.setAlpha(v, 1);
        ViewCompat.setScaleY(v, 1);
        ViewCompat.setScaleX(v, 1);
        ViewCompat.setTranslationY(v, 0);
        ViewCompat.setTranslationX(v, 0);
        ViewCompat.setRotation(v, 0);
        ViewCompat.setRotationY(v, 0);
        ViewCompat.setRotationX(v, 0);
        ViewCompat.setPivotY(v, v.getMeasuredHeight() / 2);
        ViewCompat.setPivotX(v, v.getMeasuredWidth() / 2);
        ViewCompat.animate(v).setInterpolator(null).setStartDelay(0);
        v.clearAnimation();
    }

    /**
     * 检查富文本必须由RichTextView展示
     * @param callback
     */
    public static void checkRichCallback(Drawable.Callback callback, Set<WeakReference<View>> viewSet) {
        if (!(callback instanceof CallbackView) && (viewSet == null || viewSet.size() == 0)) {
            boolean isView = callback instanceof View;
            Context context = null;
            if (isView) {
                context = ((View) callback).getContext();
            }
            throw new IllegalStateException("you mush use " + RichTextView.class.getSimpleName() /*+ "|" + AsyAnimImageView.class.getSimpleName()*/
                    + " instead for rich content!callback=" + callback
                    + ",isView=" + isView
                    + ",context=" + context);
        }
    }

}