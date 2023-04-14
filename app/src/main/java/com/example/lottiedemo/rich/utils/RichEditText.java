package com.example.lottiedemo.rich.utils;

import static com.example.lottiedemo.rich.utils.RichUtils.enableOrDisableHardwareLayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by rgy on 2021/9/16 0016.
 * 需要展示动画的editText
 */
public class RichEditText extends androidx.appcompat.widget.AppCompatEditText implements IAsyBitmapView {

    private static final String TAG = RichEditText.class.getSimpleName();

    /**
     * 使用getAssist()方法代替
     */
    @Deprecated
    private RichTextViewAssist assist;

    private TextWatcher textWatcher;

    public RichEditText(@NonNull Context context) {
        super(context);
        init();
    }

    public RichEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        enableOrDisableHardwareLayer(this);
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
                RichEditText.super.setCompoundDrawables(left, top, right, bottom);
            }
        }, left, top, right, bottom);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        getAssist().onTextChanged(text);
    }

    /**
     * java.lang.ClassCastExceptionjava.lang.String cannot be cast to android.text.Editable
     * @return
     */
    @Override
    public Editable getText() {
        try {
            return super.getText();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Editable.Factory.getInstance().newEditable("");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getAssist().onAttachedToWindow();
        if (textWatcher == null){
            addTextChangedListener(textWatcher = new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    getAssist().onTextChanged(s.toString());
                }
            });
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getAssist().onDetachedFromWindow();
        if (textWatcher != null){
            removeTextChangedListener(textWatcher);
            textWatcher = null;
        }
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
        return RichEditText.class.getSimpleName() + "(" + hashCode() + "),size=" + getAssist().getAllSize() + ",text=" + getText();
    }

}
