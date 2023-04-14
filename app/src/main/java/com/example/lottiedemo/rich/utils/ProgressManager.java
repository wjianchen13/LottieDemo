package com.example.lottiedemo.rich.utils;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rgy on 2021/4/15 0015.
 * 进度管理类，主要管理各种需要同步的动画，比如等级
 */
public class ProgressManager {

    private static final String TAG = ProgressManager.class.getSimpleName();

    private static ProgressManager INSTANCE;

    private Map<String, WeakReference<IAsyAnimDrawable>> mAllCache;
    private ReferenceQueue<IAsyAnimDrawable> referenceQueue = new ReferenceQueue<>();
    private Map<String, Progress> mProgressCaches;

    private static class Progress{
        long time;
        float progress;

        public Progress(long time, float progress) {
            this.time = time;
            this.progress = progress;
        }
    }

//    @Contract(pure = true)
    public static ProgressManager getInstance(){
        if (INSTANCE == null){
            synchronized (ProgressManager.class){
                if (INSTANCE == null){
                    INSTANCE = new ProgressManager();
                }
            }
        }
        return INSTANCE;
    }

    private ProgressManager(){

    }

    public float getProgress(String key, long duration){
        if (mProgressCaches == null || mProgressCaches.isEmpty())
            return 0;
        Progress progress = mProgressCaches.get(key);
        if (progress == null)
            return 0;
        long aa = (SystemClock.elapsedRealtime() - (progress.time - Float.valueOf(duration * progress.progress).longValue())) % duration;
        return (float)aa / duration;
    }

    public void setProgress(String key, float progress){
        if (TextUtils.isEmpty(key))
            return;
        if (mProgressCaches == null)
            mProgressCaches = new HashMap<>();
        mProgressCaches.put(key, new Progress(SystemClock.elapsedRealtime(), progress));
    }

    @Deprecated
    private  @NonNull IAsyAnimDrawable getDrawable(Context context, String key){

        return createDrawableWithoutCache(context);

//        IAsyAnimDrawable d = findInCache(key);
//        if (d != null)
//            return d;
//
//        d = createDrawableWithoutCache(context);
//        WeakReference<IAsyAnimDrawable> r = new WeakReference<>(d, referenceQueue);
//        if (mAllCache == null)
//            mAllCache = new HashMap<>();
//        mAllCache.put(key, r);
//
//        return d;
    }

//    public float getProgress(String key){
//        IAsyAnimDrawable d = findInCache(key);
//        if (d != null)
//            return d.getProgress();
//        return 0f;
//    }

    private @Nullable IAsyAnimDrawable findInCache(String key){
        if (mAllCache != null && mAllCache.size() > 0){
            WeakReference<IAsyAnimDrawable> r = mAllCache.get(key);
            if (r != null) {
                IAsyAnimDrawable d = r.get();
                if (d != null)
                    return d;
            }
        }
        return null;
    }

    public IAsyAnimDrawable createDrawableWithoutCache(Context context){
//        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), createTestCache(context, false));
//        Utils.setScaleBound(context, 15, bitmapDrawable);
//        AsyLottieDrawable a = new AsyLottieDrawable(context
//                , bitmapDrawable
//                , fileName
//                , imageDir);
//        a.setProgressKey("1");
        return null;
    }

}
