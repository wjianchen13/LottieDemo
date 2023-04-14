package com.example.lottiedemo.rich.utils;

/**
 * 通用回调
 */
public interface ICallBack<T> {
    void onSuccess(T obj);
    void onFail(int error, String msg);
    default void onFail(){
        onFail(0, "");
    }
}
