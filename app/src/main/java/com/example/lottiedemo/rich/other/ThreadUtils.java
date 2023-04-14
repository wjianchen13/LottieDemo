package com.example.lottiedemo.rich.other;

import android.os.Looper;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.executor.GlideExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/10/22 0022.
 */

public class ThreadUtils {
    private ThreadUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    // 用于PomeloClient发送消息的线程,由于PomeloClient处理消息并不是线程安全的，因此消息以队列的形式在单条线程中执行
    private static ExecutorService sendExecutor = Executors.newSingleThreadExecutor();

    // 缓存线程池，用于本地子线程任务
    private static ExecutorService executorService = Executors.newFixedThreadPool(16);

    // 3个线程的线程池，用于下载游戏
    private static ExecutorService downloadExecutor = Executors.newFixedThreadPool(4);

    // 加载礼物图片线程
    private static ExecutorService giftBmpExecutor = Executors.newSingleThreadExecutor();

    // 直播间使用
    @NonNull
    private static ExecutorService chunkExecutor = Executors.newFixedThreadPool(10);


    private static GlideExecutor dealBmpExecutor = GlideExecutor.newDiskCacheExecutor(2, "dealBmpExecutor", new GlideExecutor.UncaughtThrowableStrategy() {
        @Override
        public void handle(Throwable t) {
            t.printStackTrace();
        }
    });

    private static GlideExecutor resizeExecutor = GlideExecutor.newSourceExecutor(2, "resizeExecutor", new GlideExecutor.UncaughtThrowableStrategy() {
        @Override
        public void handle(Throwable t) {
            t.printStackTrace();
        }
    });

    public static ExecutorService clientSend() {
        return sendExecutor;
    }

    public static ExecutorService execute() {
        return executorService;
    }

    public static ExecutorService download() {
        return downloadExecutor;
    }

    public static ExecutorService loadGiftBmp() {
        return giftBmpExecutor;
    }

    static GlideExecutor glideDealBmp() {
        return dealBmpExecutor;
    }

    static GlideExecutor glideResizeBmp() {
        return resizeExecutor;
    }

    @NonNull
    public static ExecutorService getChunk(){
        return chunkExecutor;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static String curThreadName(){
        return Thread.currentThread().getName();
    }

}
