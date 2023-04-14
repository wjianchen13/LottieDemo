package com.example.lottiedemo.rich.utils;

import static android.view.View.LAYER_TYPE_SOFTWARE;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;

import com.example.lottiedemo.rich.other.ObjectUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rgy on 2021/9/27 0027.
 * 富文本相关工具
 */
public class RichUtils {

    /**
     * 清空为null的缓存
     * @param caches
     */
    public static void clearAllNull(Map<String, WeakReference<Drawable>> caches) {

        if (ObjectUtils.isEmpty(caches))
            return;

        try {

            List<String> keys = null;

            for (Map.Entry<String, WeakReference<Drawable>> entry : caches.entrySet()) {
                WeakReference<Drawable> value = entry.getValue();
                if (value == null || value.get() == null) {
                    if (keys == null)
                        keys = new ArrayList<>();
                    keys.add(entry.getKey());
                }
            }

            if (ObjectUtils.isNotEmpty(keys)) {
                for (int i = 0; i < keys.size(); i++) {
                    caches.remove(keys.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isViewNotAlive(View v){
//        Context c = v != null ? v.getContext() : null;
//        return c instanceof AppCompatActivity && !((AppCompatActivity) c).isAlive();
        return false;
    }

    public static boolean isVisibleRectShown(View v){
        return isVisibleRectShown(null, v);
    }

    public static boolean isVisibleRectShown(Rect r, View v){
        if (r == null)
            r = new Rect();
        return v != null && v.getWidth() > 0 && v.getGlobalVisibleRect(r);
    }

//    public static String cacheKey(){
//        return EncryptUtils.UUID();
//    }

    @SuppressLint("RestrictedApi")
    public static void enableOrDisableHardwareLayer(View view) {
        /**
         * EditText必须关闭硬件加速，否则无法刷新动画
         */
        if (view instanceof EditText
                && view.getLayerType() != LAYER_TYPE_SOFTWARE){
            view.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

//        if (view == null)
//            return;
//
//        LottieComposition composition = getComposition();
//
//        int layerType = LAYER_TYPE_SOFTWARE;
//
//        /**
//         * EditText必须关闭硬件加速，否则无法刷新动画
//         */
//        if (!(view instanceof EditText)) {
//            boolean useHardwareLayer = true;
//            if (composition != null && composition.hasDashPattern() && Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
//                useHardwareLayer = false;
//            } else if (composition != null && composition.getMaskAndMatteCount() > 4) {
//                useHardwareLayer = false;
//            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                useHardwareLayer = false;
//            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N || Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
//                useHardwareLayer = false;
//            }
//            layerType = useHardwareLayer ? LAYER_TYPE_HARDWARE : LAYER_TYPE_SOFTWARE;
//        } else {
//            log("EditText my layerType=" + view.getLayerType());
//        }
//
//        if (layerType != view.getLayerType()) {
//            log("setLayerType=" + layerType);
//            view.setLayerType(layerType, null);
//        }
    }

}
