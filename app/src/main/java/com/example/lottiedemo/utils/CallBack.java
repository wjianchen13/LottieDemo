package com.example.lottiedemo.utils;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import com.example.lottiedemo.rich.utils.ICallBack;

/**
 * 通用callback
 * 直接继承ICallBack即可
 * @param <T>
 */
public class CallBack<T> implements ICallBack<T> {

    @CallSuper
    public void onSuccess(T obj){
//        _95L.iTag("ICallBack.onSuccess", "obj=" + obj);
        onResult(true, obj, 0, "");
    }

    @CallSuper
    public void onFail(int error, String msg){
//        _95L.iTag("ICallBack.onFail", "error=" + error + ",msg=" + msg);
        onResult(false, null, error, msg);
    }

    /**
     * 此方法只能被重写不能外界调用
     * @param isSuccess
     * @param obj
     * @param error
     * @param msg
     */
    @CallSuper
    protected void onResult(boolean isSuccess, @Nullable T obj, int error, String msg) {
//        _95L.iTag("ICallBack.onResult", "isSuccess=" + isSuccess + ",obj=" + obj + ",error=" + error + ",msg=" + msg);
//        if (!isSuccess && !TextUtils.isEmpty(msg))
//            ApplicationUtil.showToast(msg);
    }

}
