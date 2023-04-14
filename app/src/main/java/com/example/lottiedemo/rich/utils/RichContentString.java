package com.example.lottiedemo.rich.utils;

import android.text.NoCopySpan;
import android.text.SpannableString;

import java.io.Serializable;

/**
 * Created by rgy on 2021/4/16 0016.
 * 全局使用这个作为富文本
 */
public class RichContentString extends SpannableString implements Serializable {
    /**
     * For the backward compatibility reasons, this constructor copies all spans including {@link
     * NoCopySpan}.
     *
     * @param source source text
     */
    public RichContentString(CharSequence source) {
        super(source);
    }
}
