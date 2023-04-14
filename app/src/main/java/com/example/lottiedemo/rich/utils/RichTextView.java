package com.example.lottiedemo.rich.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by rgy on 2021/3/24 0024.
 * 富文本统一使用这个textView
 */
public class RichTextView extends androidx.appcompat.widget.AppCompatTextView implements IAsyBitmapView {

    private static final String TAG = RichTextView.class.getSimpleName();

    /**
     * 使用getAssist()方法代替
     */
    @Deprecated
    private RichTextViewAssist assist;

    public RichTextView(@NonNull Context context) {
        super(context);
    }

    public RichTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RichTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    private RichTextViewAssist getAssist(){
        if (assist == null)
            assist = new RichTextViewAssist(this);
        return assist;
    }

    /**
     * 不要改变任何IAsyBitmapDrawable的Callback，IAsyBitmapDrawable的Callback由自己生成
     */
    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        getAssist().setCompoundDrawables(new Runnable() {
            @Override
            public void run() {
                RichTextView.super.setCompoundDrawables(left, top, right, bottom);
            }
        }, left, top, right, bottom);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        getAssist().onTextChanged(text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getAssist().onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getAssist().onDetachedFromWindow();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        getAssist().onVisibilityChanged(changedView, visibility);
        getAssist().log("AsyView.onVisibilityChanged,getText=" + getText());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        getAssist().onWindowVisibilityChanged(visibility);
//        getAssist().log("AsyView.onWindowVisibilityChanged,getText=" + getText());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getAssist().log(getText() + ",onLayout,changed=" + changed
                + ",left=" + left
                + ",top=" + top
                + ",right=" + right
                + ",bottom=" + bottom);
    }

    /**
     * 通知异步加载资源完成
     * 如果全部完成，那么重新设置text
     */
    @Override
    public void onDrawableLoadCompleted(IAsyBitmapDrawable drawable) {
        getAssist().onDrawableLoadCompleted(drawable);
    }

    @Override
    public boolean isVisible() {
        return getAssist().isVisible();
    }

    @Override
    public boolean isAttachToWindow() {
        return getAssist().isAttachToWindow();
    }

    @Override
    public void checkAttachOrDetach(boolean isFromVisibleChanged) {
        getAssist().checkAttachOrDetach(isFromVisibleChanged);
    }

    @Override
    public String name() {
        return RichTextView.class.getSimpleName() + "(" + hashCode() + "),size=" + getAssist().getAllSize() + ",text=" + getText();
    }
}
