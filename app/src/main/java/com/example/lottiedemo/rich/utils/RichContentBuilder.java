package com.example.lottiedemo.rich.utils;

import android.text.SpannableStringBuilder;

import java.io.Serializable;

/**
 * Created by rgy on 2021/4/15 0015.
 * 全局使用这个作为富文本
 */
public class RichContentBuilder extends SpannableStringBuilder implements Serializable {

    public RichContentBuilder() {
    }

    public RichContentBuilder(CharSequence text) {
        super(text);
    }

    public RichContentBuilder(CharSequence text, int start, int end) {
        super(text, start, end);
    }

    @Override
    public RichContentBuilder append(CharSequence text) {
        super.append(text);
        return this;
    }

    @Override
    public RichContentBuilder append(CharSequence text, Object what, int flags) {
        super.append(text, what, flags);
        return this;
    }

    @Override
    public RichContentBuilder append(CharSequence text, int start, int end) {
        super.append(text, start, end);
        return this;
    }

    @Override
    public RichContentBuilder append(char text) {
        super.append(text);
        return this;
    }

    @Override
    public RichContentBuilder replace(int start, int end, CharSequence tb) {
        super.replace(start, end, tb);
        return this;
    }

    @Override
    public RichContentBuilder replace(final int start, final int end,
                                          CharSequence tb, int tbstart, int tbend) {
        super.replace(start, end, tb, tbstart, tbend);
        return this;
    }

    @Override
    public RichContentBuilder insert(int where, CharSequence tb, int start, int end) {
        super.insert(where, tb, start, end);
        return this;
    }

    @Override
    public RichContentBuilder insert(int where, CharSequence tb) {
        super.insert(where, tb);
        return this;
    }

    @Override
    public RichContentBuilder delete(int start, int end) {
        super.delete(start, end);
        return this;
    }
}
