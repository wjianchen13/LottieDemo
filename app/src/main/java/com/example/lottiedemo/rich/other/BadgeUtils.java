package com.example.lottiedemo.rich.other;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.example.lottiedemo.DobyApp;
import com.example.lottiedemo.R;
import com.example.lottiedemo.rich.ems.EmResManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 徽章操作类
 */
public class BadgeUtils {

    /**
     *
     * @param context
     * @param str
     * @return
     */
    public static SpannableString getBadgeString(Context context, CharSequence str) {
        return getBadgeString(new BadgeUtils.BadgeOptions(context, str));
    }

    /**
     * 解析表情
     * @param options
     * @return
     */
    public static SpannableString getBadgeString(@NonNull BadgeUtils.BadgeOptions options) {
        if (TextUtils.isEmpty(options.getContent()))
            return new SpannableString("");
        SpannableString spannableString = new SpannableString(options.getContent());
        //String zhengze = "\\[[^\\]]+\\]";
        String zhengze = "\\[em:(\\d+)\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(spannableString, sinaPatten, 0, options);
        } catch (Exception e) {
            return new SpannableString("");
        }
        return spannableString;
    }

    /**
     * 递归处理
     * @param spannableString
     * @param patten
     * @param start
     * @param options
     */
    private static void dealExpression(SpannableString spannableString, Pattern patten, int start, @NonNull BadgeUtils.BadgeOptions options){

        Matcher matcher = patten.matcher(spannableString);

        while (matcher.find()) {

            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start
                    || !EmResManager.getInstance().hasEntity(key)) {
                continue;
            }

            int end = matcher.start() + key.length();

            // 将该图片替换字符串中规定的位置中
            spannableString.setSpan(EmResManager.with(options.getContext())
                    .id(key)
                    .normalHeight(options.getNormalHeight())
                    .expandHeight(options.getExpandHeight())
                    .isHolder(options.isHolder())
                    .isNotExpand(options.isNotExpand())
                    .isSkipCache(options.isSkipCache())
                    .defTxtColor(options.getDefTxtColor())
                    .defTxtBold(options.isDefTxtBold())
                    .cacheKey(options.getCacheKey())
                    .placeDelay(options.getPlaceDelay())
                    .isPauseAnim(options.isPauseAnim())
                    .request()
                    .span(), matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (end < spannableString.length()) {
                // 如果整个字符串还未验证完，则继续。。
                dealExpression(spannableString, patten, end, options);
            }

            break;
        }
    }

    public static class BadgeOptions{
        protected Context context;
        protected CharSequence content;
        /**
         * 默认字体颜色
         */
        protected @ColorInt
        int defTxtColor = Color.WHITE;
        /**
         * 默认字体粗体
         */
        protected boolean defTxtBold;
        /**
         * 不扩大，统一大小
         */
        protected boolean isNotExpand;
        /**
         * 是否跳过缓存
         */
        protected boolean isSkipCache;
        /**
         * 缓存字段
         */
        protected String cacheKey;
        /**
         * 使用默认holder图填充
         */
        protected boolean isHolder;
        /**
         * 延时显示占位图
         */
        protected long placeDelay = 500;
        /**
         * 暂停代替stop
         */
        protected boolean isPauseAnim = true;

        protected int normalHeight;
        protected int expandHeight;

        public BadgeOptions(Context context, CharSequence content){
            this.context = context;
            this.content = content;
            normalHeight = (int) DobyApp.app().getResources().getDimension(R.dimen.face_chat_h);
            expandHeight = normalHeight;
        }

        @NonNull
        public Context getContext(){
//            if (context == null)
//                return ActivityStatusManager.getInstance().getStackTopActivity();
            return context;
        }

        @NonNull
        public CharSequence getContent() {
            if (content == null)
                return "";
            return content;
        }

        public boolean isNotExpand() {
            return isNotExpand;
        }

        public boolean isSkipCache() {
            return isSkipCache;
        }

        public BadgeOptions setNotExpand(boolean notExpand) {
            isNotExpand = notExpand;
            return this;
        }

        public BadgeOptions setNormalHeight(int normalHeight) {
            this.normalHeight = normalHeight;
            return this;
        }

        public int getNormalHeight() {
            return normalHeight;
        }

        public int getExpandHeight() {
            return expandHeight;
        }

        public BadgeOptions setExpandHeight(int expandHeight) {
            this.expandHeight = expandHeight;
            return this;
        }

        public BadgeOptions isSkipCache(boolean isSkipCache){
            this.isSkipCache = isSkipCache;
            return this;
        }

        public int getDefTxtColor() {
            return defTxtColor;
        }

        public BadgeOptions setDefTxtColor(int defTxtColor) {
            this.defTxtColor = defTxtColor;
            return this;
        }

        public boolean isDefTxtBold() {
            return defTxtBold;
        }

        public BadgeOptions setDefTxtBold(boolean defTxtBold) {
            this.defTxtBold = defTxtBold;
            return this;
        }

        public boolean isHolder() {
            return isHolder;
        }

        public BadgeOptions setHolder(boolean holder) {
            isHolder = holder;
            return this;
        }

        public long getPlaceDelay() {
            return placeDelay;
        }

        public BadgeOptions setPlaceDelay(long placeDelay) {
            this.placeDelay = placeDelay;
            return this;
        }

        public boolean isPauseAnim() {
            return isPauseAnim;
        }

        public BadgeOptions setPauseAnim(boolean pauseAnim) {
            this.isPauseAnim = pauseAnim;
            return this;
        }

        public String getCacheKey() {
            return cacheKey;
        }

        public BadgeOptions setCacheKey(String cacheKey) {
            this.cacheKey = cacheKey;
            return this;
        }
    }


}
