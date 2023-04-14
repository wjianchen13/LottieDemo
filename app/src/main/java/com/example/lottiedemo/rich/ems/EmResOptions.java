package com.example.lottiedemo.rich.ems;

import static com.example.lottiedemo.rich.utils.ImageSpanAlign.XIU_ALIGN_BOTTOM;
import static com.example.lottiedemo.rich.utils.ImageSpanAlign.XIU_ALIGN_CENTER;
import static com.example.lottiedemo.rich.utils.ImageSpanAlign.XIU_ALIGN_TILING;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.example.lottiedemo.rich.utils.ICallBack;

/**
 * Created by rgy on 2021/9/9 0009.
 * 表情请求
 */
public class EmResOptions {

    private Context context;

    private int id = -1;

    /**
     * 使用默认holder图填充
     */
    private boolean isHolder;

    /**
     * 默认文字颜色
     */
    @ColorInt
    private int defTxtColor = Color.WHITE;

    /**
     * 默认字体粗体
     */
    private boolean defTxtBold;

    /**
     * 忽略动画，只要图片
     */
    private boolean ignoreAnim;

    /**
     * 高度，会按照这个尺寸缩放
     */
    private int height;

    /**
     * 正常高度
     */
    private int normalHeight;

    /**
     * 放大高度，比如骰子，vip表情
     */
    private int expandHeight;

    /**
     * 禁止放大，比如骰子，vip表情
     */
    private boolean isNotExpand;

    /**
     * 跳过缓存
     */
    private boolean isSkipCache;

    /**
     * 缓存字段
     */
    private String cacheKey;

    /**
     * 延时显示占位图
     */
    private long placeDelay = 500;

    /**
     * span的对齐方式，正常是居中，使用单独textview展示动画时会平铺
     */
    private int spanAlign = XIU_ALIGN_CENTER;

    /**
     * 暂停动画代替stop，可以让动画平滑过渡，比如输入的editText
     */
    private boolean isPauseAnim = true;

    /**
     * 进行缩放
     */
    private float scaleX = 1f, scaleY = 1f;

    private ICallBack<EmResManager.DrawableProxy> callBack;

    EmResOptions(Context context){
        this.context = context;
    }

    public @NonNull EmResManager.DrawableProxy request(){
        return EmResManager.getInstance().getDrawable(this);
    }

    public EmResOptions id(int id){
        this.id = id;
        return this;
    }

    public EmResOptions id(String idStr){
        id = parseId(idStr);
        return this;
    }

    public static int parseId(String idStr){
        try {
            return Integer.parseInt(idStr.replaceAll("[^0-9]", ""));
        } catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }

    public int getId() {
        return id;
    }

    public EmResOptions isHolder(boolean isHolder){
        this.isHolder = isHolder;
        return this;
    }

    public boolean isHolder(){
        return isHolder;
    }

    public EmResOptions defTxtColor(@ColorInt int defTxtColor){
        this.defTxtColor = defTxtColor;
        return this;
    }

    public EmResOptions defTxtBold(boolean defTxtBold){
        this.defTxtBold = defTxtBold;
        return this;
    }

    public boolean isIgnoreAnim() {
        return ignoreAnim;
    }

    public EmResOptions ignoreAnim(boolean ignoreAnim) {
        this.ignoreAnim = ignoreAnim;
        return this;
    }

    public EmResOptions scaleX(float scaleX){
        this.scaleX = scaleX;
        return this;
    }

    public EmResOptions scaleY(float scaleY){
        this.scaleY = scaleY;
        return this;
    }

    EmResOptions height(int height){
        this.height = height;
        return this;
    }

    public EmResOptions normalHeight(int normalHeight){
        this.normalHeight = normalHeight;
        return this;
    }

    public EmResOptions expandHeight(int expandHeight){
        this.expandHeight = expandHeight;
        return this;
    }

    public EmResOptions isNotExpand(boolean isNotExpand){
        this.isNotExpand = isNotExpand;
        return this;
    }

    public EmResOptions isSkipCache(boolean isSkipCache){
        this.isSkipCache = isSkipCache;
        return this;
    }

    public EmResOptions cacheKey(String cacheKey){
        this.cacheKey = cacheKey;
        return this;
    }

    public EmResOptions placeDelay(long placeDelay){
        this.placeDelay = placeDelay;
        return this;
    }

    public EmResOptions spanAlign(int spanAlign){
        this.spanAlign = spanAlign;
        return this;
    }

    public EmResOptions spanAlignCenter(){
        this.spanAlign = XIU_ALIGN_CENTER;
        return this;
    }

    public EmResOptions spanAlignBottom(){
        this.spanAlign = XIU_ALIGN_BOTTOM;
        return this;
    }

    public EmResOptions spanAlignTiling(){
        this.spanAlign = XIU_ALIGN_TILING;
        return this;
    }

    public EmResOptions isPauseAnim(boolean isPauseAnim){
        this.isPauseAnim = isPauseAnim;
        return this;
    }

    public EmResOptions callback(ICallBack<EmResManager.DrawableProxy> callBack){
        this.callBack = callBack;
        return this;
    }

    public ICallBack<EmResManager.DrawableProxy> getCallBack() {
        return callBack;
    }

    public @NonNull Context getContext() {
//        if (context instanceof Activity)
//            return context;
//        return ActivityStatusManager.getInstance().getStackTopActivity();
        return context;
    }

    public int getHeight() {
        return height;
    }

    public float getScaleX() {
        return Math.max(0, scaleX);
    }

    public float getScaleY() {
        return Math.max(0, scaleY);
    }

    public int getExpandHeight() {
        return expandHeight;
    }

    public int getNormalHeight() {
        return normalHeight;
    }

    public boolean isNotExpand() {
        return isNotExpand;
    }

    public boolean isSkipCache() {
        return isSkipCache;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    @NonNull
    public String getCacheKeyFully() {
        return TextUtils.isEmpty(cacheKey) ? "" : cacheKey;
    }

    @ColorInt
    public int getDefTxtColor() {
        return defTxtColor;
    }

    public boolean getDefTxtBold(){
        return defTxtBold;
    }

    public long getPlaceDelay() {
        return placeDelay;
    }

    public int getSpanAlign() {
        return spanAlign;
    }

    public boolean isPauseAnim() {
        return isPauseAnim;
    }

    public String key() {
        StringBuilder sb = new StringBuilder(getCacheKeyFully());
        sb.append("-").append(getId())
                .append("-").append(getHeight())
                .append("-").append(isHolder())
                .append("-").append(getPlaceDelay())
                .append("-").append(getScaleX())
                .append("-").append(getScaleY())
                .append("-").append(isPauseAnim())
                .append("-").append(getSpanAlign())
                .append("-").append(isNotExpand())
                .append("-").append(getExpandHeight())
                .append("-").append(getNormalHeight())
                .append("-").append(isIgnoreAnim())
                .append("-").append(getDefTxtBold())
                .append("-").append(getDefTxtColor());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "EmResOptions{" +
                "context=" + context +
                ", id=" + id +
                ", isHolder=" + isHolder +
                ", defTxtColor=" + defTxtColor +
                ", defTxtBold=" + defTxtBold +
                ", ignoreAnim=" + ignoreAnim +
                ", height=" + height +
                ", normalHeight=" + normalHeight +
                ", expandHeight=" + expandHeight +
                ", isNotExpand=" + isNotExpand +
                ", isSkipCache=" + isSkipCache +
                ", cacheKey='" + cacheKey + '\'' +
                ", placeDelay=" + placeDelay +
                ", spanAlign=" + spanAlign +
                ", isPauseAnim=" + isPauseAnim +
                ", scaleX=" + scaleX +
                ", scaleY=" + scaleY +
                ", callBack=" + callBack +
                '}';
    }
}
