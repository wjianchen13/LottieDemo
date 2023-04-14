package com.example.lottiedemo.rich.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lottiedemo.DobyApp;
import com.example.lottiedemo.rich.other.ObjectUtils;
import com.example.lottiedemo.utils.CallBack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CallbackView extends View {
    private final @NonNull Set<WeakReference<View>> viewSet;

    CallbackView(@NonNull Set<WeakReference<View>> viewSet) {
        /**
         * 使用Activity的context会导致泄漏
         */
        super(DobyApp.app());
        this.viewSet = viewSet;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        iteratorView(viewSet, new CallBack<View>(){
            @Override
            public void onSuccess(View view) {
                super.onSuccess(view);
                view.invalidateDrawable(drawable);
            }
        });
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        iteratorView(viewSet, new CallBack<View>(){
            @Override
            public void onSuccess(View view) {
                super.onSuccess(view);
                view.scheduleDrawable(who, what, when);
            }
        });
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        iteratorView(viewSet, new CallBack<View>(){
            @Override
            public void onSuccess(View view) {
                super.onSuccess(view);
                view.unscheduleDrawable(who, what);
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + ",size=" + viewSet.size();
    }

    /**
     * 包含多少有效view
     * @param viewSet
     * @return
     */
    public static int checkSize(Set<WeakReference<View>> viewSet){
        if (ObjectUtils.isNotEmpty(viewSet)){
            List<WeakReference<View>> removes = null;
            for (WeakReference<View> r : viewSet) {
                if (r == null || r.get() == null) {
                    if (removes == null)
                        removes = new ArrayList<>();
                    removes.add(r);
                }
            }
            if (ObjectUtils.isNotEmpty(removes))
                viewSet.removeAll(removes);
        }
        return viewSet != null ? viewSet.size() : 0;
    }

    /**
     * 去除无效的view后是否为空
     * @param viewSet
     * @return
     */
    public static boolean isCheckNullEmpty(Set<WeakReference<View>> viewSet){
        return checkSize(viewSet) == 0;
    }

    /**
     * 是否包含这个view
     * @param viewSet
     * @param view
     * @return
     */
    public static boolean hasThisView(Set<WeakReference<View>> viewSet, View view){
        if (ObjectUtils.isNotEmpty(viewSet) && view != null){
            for (WeakReference<View> r : viewSet) {
                if (r != null && r.get() == view)
                    return true;
            }
        }
        return false;
    }

    /**
     * 返回这个view的reference
     * @param viewSet
     * @param view
     * @return
     */
    @Nullable
    public static WeakReference<View> hasThisViewRef(Set<WeakReference<View>> viewSet, View view){
        if (ObjectUtils.isNotEmpty(viewSet) && view != null){
            for (WeakReference<View> r : viewSet) {
                if (r != null && r.get() == view)
                    return r;
            }
        }
        return null;
    }

    /**
     * 如果不包含这个view就添加
     * @param viewSet
     * @param view
     * @return
     */
    public static boolean addIfLackView(Set<WeakReference<View>> viewSet, View view){
        if (!hasThisView(viewSet, view) && viewSet != null && view != null){
            viewSet.add(new WeakReference<>(view));
            return true;
        }
        return false;
    }

    /**
     * 如果包含这个view就移除
     * @param viewSet
     * @param view
     * @return
     */
    public static boolean removeIfContainsView(Set<WeakReference<View>> viewSet, View view){
        WeakReference<View> ref = hasThisViewRef(viewSet, view);
        if (ref != null){
            viewSet.remove(ref);
            return true;
        }
        return false;
    }

    /**
     * 遍历有效view
     * @param viewSet
     * @param callBack
     */
    public static void iteratorView(Set<WeakReference<View>> viewSet, @NonNull ICallBack<View> callBack){
        if (ObjectUtils.isNotEmpty(viewSet)){
            for (WeakReference<View> r : viewSet) {
                if (r != null){
                    View v = r.get();
                    if (v != null)
                        callBack.onSuccess(v);
                }
            }
        }
    }

    /**
     * 是否所有view都合法
     * @param viewSet
     * @return
     */
    public static boolean isAllViewLegal(Set<WeakReference<View>> viewSet){
        if (ObjectUtils.isNotEmpty(viewSet)){
            for (WeakReference<View> r : viewSet) {
                if (r != null){
                    View v = r.get();
                    if (v != null && !(v instanceof IAsyBitmapView))
                        return false;
                }
            }
        }
        return true;
    }

    public static boolean isEditText(Set<WeakReference<View>> viewSet){
        if (viewSet != null && viewSet.size() == 1){
            for (WeakReference<View> r : viewSet) {
                if (r == null || !(r.get() instanceof EditText))
                    return false;
            }
            return true;
        }
        return false;
    }
}
