package com.example.lottiedemo.rich.utils;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rgy on 2021/9/16 0016.
 * richView辅助类，附加逻辑通用
 */
public class RichTextViewAssist {

    private static final String TAG = "IAsyBitmapView_" + RichTextViewAssist.class.getSimpleName();

    @NonNull
    private TextView masterView;
    @Nullable
    private Context context;

    private boolean isAttachToWindow;

    public RichTextViewAssist(@NonNull TextView masterView){
        this.masterView = masterView;
        this.context = masterView.getContext();
    }

    /**
     * 所有IAsyBitmapDrawable，用来判断是否都加载完
     */
    private Set<IAsyBitmapDrawable> allAsyBitmapDrawables;

    public void log(String msg){
        log("", msg);
    }

    public void log(String tag, String msg){
//        _95L.iTag(TAG + "_" + tag, msg
//                + ",visibility=" + masterView.getVisibility()
//                + ",isAttachToWindow=" + isAttachToWindow
//                + ",isShown=" + masterView.isShown()
//                + ",isVisibleRectShown=" + isVisibleRectShown(masterView));
    }

    public boolean isVisible(){
        return masterView.getVisibility() == VISIBLE;
    }

    public boolean isAttachToWindow(){
        return isAttachToWindow;
    }

    /**
     * 通知异步加载资源完成
     * 如果全部完成，那么重新设置text
     */
    public void onDrawableLoadCompleted(IAsyBitmapDrawable drawable) {
        log(drawable.getName() + " onDrawableLoadCompleted!");
        if (allAsyBitmapDrawables != null && !allAsyBitmapDrawables.isEmpty()){
            for (IAsyBitmapDrawable d : allAsyBitmapDrawables) {
                if (d != drawable && d != null && !d.isLoadCompleted())
                    return;
            }
        }
        log("notifyLoadCompleted execute");
        /*if (masterView instanceof ExpandableTextView) {
            ((ExpandableTextView) masterView).setContent(masterView.getText());
            *//**
             * editText如果重新设置会导致光标回到0位置
             *//*
        } else */if (!(masterView instanceof EditText)) {
            try {
                boolean isCompound = false;
                Drawable[] compounds = masterView.getCompoundDrawables();
                for (int i = 0; i < compounds.length; i++) {
                    if (compounds[i] == drawable.getTarget()) {
                        isCompound = true;
                        break;
                    }
                }
                if (isCompound) {
                    masterView.setCompoundDrawables(compounds[0], compounds[1], compounds[2], compounds[3]);
                } else {
                    masterView.setText(masterView.getText(), TextView.BufferType.SPANNABLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /******************************************************************************
     *
     * 处理内容
     *
     *****************************************************************************/
    /**
     * 检查是否调用attach,detach
     */
    public void checkAttachOrDetach(boolean isFromVisibleChanged){
        boolean isVisible = isFromVisibleChanged ? masterView.isShown() : isVisible();
        log("_asy_draw", "checkAttachOrDetach，isFromVisibleChanged=" + isFromVisibleChanged + ",isVisible=" + isVisible);
        if (isVisible && isAttachToWindow()){
            attachAnimDrawable();
        } else {
            detachAnimDrawable();
        }
    }

    private void attachAnimDrawable() {
        if (allAsyBitmapDrawables != null) {
            for (IAsyBitmapDrawable drawable : allAsyBitmapDrawables) {
                if (null != drawable) {
                    drawable.attach(masterView);
                }
            }
        }
    }

    private void detachAnimDrawable() {
        if (allAsyBitmapDrawables != null) {
            for (IAsyBitmapDrawable drawable : allAsyBitmapDrawables) {
                if (null != drawable) {
                    drawable.detach(masterView);
                }
            }
        }
    }

    @Nullable
    private AsyAnimDrawableSpan[] handleAnimationSpan(){
        return handleAnimationSpan(null);
    }

    /**
     * 处理动画span
     * @param callBack
     * @return
     */
    @Nullable
    private AsyAnimDrawableSpan[] handleAnimationSpan(ICallBack<AsyAnimDrawableSpan> callBack){
        CharSequence txt = masterView.getText();
        if (txt instanceof Spanned){
            AsyAnimDrawableSpan[] spans = ((Spanned) txt).getSpans(0, txt.length() - 1, AsyAnimDrawableSpan.class);
            log(txt + ",AnimDrawableSpan,getSpans,spans=" + spans + ",length=" + spans.length);
            if (callBack != null && spans.length > 0){
                for (AsyAnimDrawableSpan span : spans) {
                    callBack.onSuccess(span);
                }
            }
            return spans;
        }
        return null;
    }

    /**
     * 处理AsyBitmapSpan
     * @return
     */
    private @Nullable AsyBitmapDrawableSpan[] handleAsyBitmapSpan(){
        CharSequence txt = masterView.getText();
        if (txt instanceof Spanned){
            AsyBitmapDrawableSpan[] spans = ((Spanned) txt).getSpans(0, txt.length() - 1, AsyBitmapDrawableSpan.class);
            log(txt + ",AsyBitmapSpan,getSpans,spans=" + spans + ",length=" + spans.length);
            if (spans.length > 0){
                for (AsyBitmapDrawableSpan span : spans) {
                    span.setCallback(masterView);
                }
            }
            return spans;
        }
        return null;
    }

    private Drawable[] handleAnimationCompounds(ICallBack<Drawable> callBack) {
        Drawable[] compoundDrawables = masterView.getCompoundDrawables();
        if (null != compoundDrawables && null != callBack) {
            for (Drawable compoundDrawable : compoundDrawables) {
                if (null != compoundDrawable) {
                    callBack.onSuccess(compoundDrawable);
                }
            }
        }
        return compoundDrawables;
    }

    private void collectAllDrawables() {
        log("collectAllDrawables!");
        if (null == allAsyBitmapDrawables) {
            allAsyBitmapDrawables = new HashSet<>();
        } else if (allAsyBitmapDrawables.size() > 0){
            log("allAsyBitmapDrawables.size() > 0 detach it all!");
            detachAnimDrawable();
            allAsyBitmapDrawables.clear();
        }
        AsyBitmapDrawableSpan[] asyBitmapDrawableSpans = handleAsyBitmapSpan();
        if (asyBitmapDrawableSpans != null && asyBitmapDrawableSpans.length > 0){
            for (AsyBitmapDrawableSpan asyBitmapDrawableSpan : asyBitmapDrawableSpans) {
                if (asyBitmapDrawableSpan != null
                        && asyBitmapDrawableSpan.getDrawable() != null) {
                    log("have bitmap,name=" + ((IAsyBitmapDrawable) asyBitmapDrawableSpan.getDrawable()).getName());
                    allAsyBitmapDrawables.add((IAsyBitmapDrawable) asyBitmapDrawableSpan.getDrawable());
                }
            }
        }
        AsyAnimDrawableSpan[] asyAnimDrawableSpans = handleAnimationSpan();
        if (asyAnimDrawableSpans != null && asyAnimDrawableSpans.length > 0){
            for (AsyAnimDrawableSpan asyAnimDrawableSpan : asyAnimDrawableSpans) {
                if (asyAnimDrawableSpan != null
                        && asyAnimDrawableSpan.getDrawable() != null) {
                    log("have anim,name=" + ((IAsyBitmapDrawable) asyAnimDrawableSpan.getDrawable()).getName());
                    allAsyBitmapDrawables.add((IAsyBitmapDrawable) asyAnimDrawableSpan.getDrawable());
                }
            }
        }
        Drawable[] drawables = handleAnimationCompounds(null);
        int size = 0;
        if (null != drawables && drawables.length > 0) {
            for (Drawable drawable : drawables) {
                if (drawable instanceof IAsyBitmapDrawable) {
                    log("have Compounds[" + size + "],name=" + ((IAsyBitmapDrawable) drawable).getName());
                    size++;
                    allAsyBitmapDrawables.add((IAsyBitmapDrawable) drawable);
                }
            }
        }

        log("collectAllDrawables,asyBitmapDrawableSpans=" + (asyBitmapDrawableSpans == null ? "null" : asyBitmapDrawableSpans.length)
                + ",asyAnimDrawableSpans=" + (asyAnimDrawableSpans == null ? "null" : asyAnimDrawableSpans.length)
                + ",handleAnimationCompounds=" + size);
    }

    /******************************************************************************
     *
     * 代理回调
     *
     *****************************************************************************/
    public void onTextChanged(CharSequence text) {
        log("setText,text=" + text);
        collectAllDrawables();
        checkAttachOrDetach(false);
    }

    public void onAttachedToWindow() {
        isAttachToWindow = true;
        log(masterView.getText() + ",onAttachedToWindow");
        checkAttachOrDetach(false);
    }

    public void onDetachedFromWindow() {
        isAttachToWindow = false;
        log(masterView.getText() + ",onDetachedFromWindow");
        checkAttachOrDetach(false);
    }

    public void onVisibilityChanged(View changedView, int visibility) {
        log(masterView.getText() + ",onVisibilityChanged,changedView=" + changedView);
        checkAttachOrDetach(true);
    }

    public void onWindowVisibilityChanged(int visibility) {
        log(masterView.getText() + ",onWindowVisibilityChanged");
        checkAttachOrDetach(true);
    }

    /**
     * 不要改变任何IAsyBitmapDrawable的Callback，IAsyBitmapDrawable的Callback由自己生成
     */
    public void setCompoundDrawables(@NonNull Runnable execute, @Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        log("setCompoundDrawables,left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom);
        Drawable[] compoundDrawables = masterView.getCompoundDrawables();
        Drawable.Callback[] callback = new Drawable.Callback[compoundDrawables.length];
        for (int i = 0; i < compoundDrawables.length; i++) {
            callback[i] = null == compoundDrawables[i] ? null : compoundDrawables[i].getCallback();
        }
        Drawable.Callback leftCallback = null == left ? null : left.getCallback();
        Drawable.Callback topCallback = null == top ? null : top.getCallback();
        Drawable.Callback rightCallback = null == right ? null : right.getCallback();
        Drawable.Callback bottomCallback = null == bottom ? null : bottom.getCallback();
        execute.run();
        for (int i = 0; i < compoundDrawables.length; i++) {
            if (compoundDrawables[i] instanceof IAsyBitmapDrawable) {
                compoundDrawables[i].setCallback(callback[i]);
                ((IAsyBitmapDrawable) compoundDrawables[i]).detach(masterView);
            }
        }
        if (left instanceof IAsyBitmapDrawable) {
            left.setCallback(leftCallback);
        }
        if (top instanceof IAsyBitmapDrawable) {
            top.setCallback(topCallback);
        }
        if (right instanceof IAsyBitmapDrawable) {
            right.setCallback(rightCallback);
        }
        if (bottom instanceof IAsyBitmapDrawable) {
            bottom.setCallback(bottomCallback);
        }
        collectAllDrawables();
        checkAttachOrDetach(false);
    }

    public String getAllSize(){
        return allAsyBitmapDrawables == null ? "" : allAsyBitmapDrawables.size() + "";
    }
}
