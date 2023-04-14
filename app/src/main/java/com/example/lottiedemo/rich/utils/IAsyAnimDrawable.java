package com.example.lottiedemo.rich.utils;

import android.view.View;

import com.example.lottiedemo.R;


/**
 * Created by rgy on 2021/4/13 0013.
 * 异步动画drawable统一接口
 * 多个View可能共用一个Drawable
 * 不再提供播放停止接口，attach view自动播放，最后一个view detach停止
 */
public interface IAsyAnimDrawable extends IAsyBitmapDrawable {
    static Object getTag(View view){
        if (view != null)
            return view.getTag(R.id.tag_anim_drawable);
        return null;
    }

    static void setTag(View view, Object tag){
        if (view != null)
            view.setTag(R.id.tag_anim_drawable, tag);
    }

    static void setTag(View view){
        if (view != null)
            view.setTag(R.id.tag_anim_drawable, null);
    }
}
