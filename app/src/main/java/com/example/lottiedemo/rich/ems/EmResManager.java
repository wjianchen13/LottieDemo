package com.example.lottiedemo.rich.ems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieListener;
import com.example.lottiedemo.DobyApp;
import com.example.lottiedemo.R;
import com.example.lottiedemo.rich.other.ImageHelper;
import com.example.lottiedemo.rich.other.ThreadUtils;
import com.example.lottiedemo.rich.utils.AsyAnimDrawableSpan;
import com.example.lottiedemo.rich.utils.AsyBitmapDrawable;
import com.example.lottiedemo.rich.utils.AsyBitmapDrawableSpan;
import com.example.lottiedemo.rich.utils.AsyLottieDrawable;
import com.example.lottiedemo.rich.utils.IAsyAnimDrawable;
import com.example.lottiedemo.rich.utils.IAsyBitmapDrawable;
import com.example.lottiedemo.rich.utils.ICallBack;
import com.example.lottiedemo.rich.utils.ImageSpanC;
import com.example.lottiedemo.rich.utils.RichUtils;
import com.example.lottiedemo.utils.CallBack;
import com.example.lottiedemo.utils.ScreenUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by rgy on 2021/9/7 0007.
 * 表情资源管理
 */
public class EmResManager {

    private static final String TAG = EmResManager.class.getSimpleName();

    private static EmResManager INSTANCE = null;

    private SparseArray<ExpressionBean> mAllBeans;
    /**
     * 用于列表展示
     */
    private List<ExpressionBean> mNormalBeans;
    /**
     * 用于列表展示
     */
    private List<ExpressionBean> mVipBeans;
    /**
     * 正在请求网络
     */
    private boolean isChecking;
    /**
     * 低配不使用动画
     */
    private boolean isUseAnim;
    /**
     * 超过这个阈值将会被清空缓存
     */
    private static final int CACHE_SIZE = 100;
    /**
     * 所有缓存
     */
    private Map<String, WeakReference<Drawable>> mCaches = new HashMap<>();  // = new HashMap<>()

    public static EmResManager getInstance() {
        if (INSTANCE == null) {
            synchronized (EmResManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EmResManager();
                }
            }
        } else {
            INSTANCE.check();
        }
        return INSTANCE;
    }

    private EmResManager(){

    }

    public static EmResOptions with(Context context){
        return new EmResOptions(context);
    }


    public void check(){
        if (!isChecking){
            isChecking = true;
            if (mAllBeans == null)
                mAllBeans = new SparseArray<>();
            mAllBeans.clear();
            isUseAnim = true;

            ExpressionBean bean1 = new ExpressionBean();
            bean1.setId(0);
            bean1.setImage("http://img.vrzb.com/upload/down/emo/0/emo_0.png");
            bean1.setJson("http://img.vrzb.com/upload/down/emo/0/0.zip?v=1");
            bean1.setName("财迷");
            bean1.setSvga("http://img.vrzb.com/upload/down/emo/0/emo_0.svga?v=1");
            mAllBeans.put(bean1.getId(), bean1);


            ExpressionBean bean2 = new ExpressionBean();
            bean2.setId(1);
            bean2.setImage("http://img.vrzb.com/upload/down/emo/1/emo_1.png");
            bean2.setJson("http://img.vrzb.com/upload/down/emo/1/1.zip?v=1");
            bean2.setName("好声音");
            bean2.setSvga("http://img.vrzb.com/upload/down/emo/1/emo_1.svga?v=1");
            mAllBeans.put(bean2.getId(), bean2);



            ExpressionBean bean3 = new ExpressionBean();
            bean3.setId(2);
            bean3.setImage("http://img.vrzb.com/upload/down/emo/2/emo_2.png");
            bean3.setJson("http://img.vrzb.com/upload/down/emo/2/2.zip?v=1");
            bean3.setName("鄙视");
            bean3.setSvga("http://img.vrzb.com/upload/down/emo/2/emo_2.svga?v=1");
            mAllBeans.put(bean3.getId(), bean3);

        }
    }

    @NonNull
    DrawableProxy getDrawable(@NonNull EmResOptions options){

//        _95L.iTag("EmResManager_getDrawable", "get from " + options);

        ExpressionBean bean = null;

        if (mAllBeans != null)
            bean = mAllBeans.get(options.getId());

        if (bean == null)
            bean = ExpressionBean.empty();

        final ExpressionBean tempBean = bean;

        if (options.getHeight() == 0) {
            int nor = options.getNormalHeight() == 0 ? (int) DobyApp.app().getResources().getDimension(R.dimen.face_chat_h) : options.getNormalHeight();
            int expand = options.getExpandHeight() == 0 ? (int) DobyApp.app().getResources().getDimension(R.dimen.face_chat_h) : options.getExpandHeight();
            options.height(!options.isNotExpand() && isNeedExpand(bean) ? expand : nor);
        }

        RichUtils.clearAllNull(mCaches);

        boolean isFromCache = false;

        Drawable valueDrawable = null;

        final String key = options.key();

        final String name = "emRes-" + bean.getId() + "-" + bean.getName();

        /**
         * 获取缓存
         */
        if (!options.isSkipCache() && mCaches != null && mCaches.size() > 0) {

            WeakReference<Drawable> cache = mCaches.get(key);
//            _95L.iTag("em_adapter_request", "mAllBeans.size=" + (mAllBeans == null ? "null" : mAllBeans.size())
//                    + ",key=" + key
//                    + ",options=" + options);

            if (cache != null) {
                Drawable cDrawable = cache.get();
                if (isFromCache = (cDrawable != null)) {
                    String n = "";
                    if (cDrawable instanceof IAsyBitmapDrawable){
                        n = "_" + ((IAsyBitmapDrawable) cDrawable).getName();
                    }
//                    _95L.iTag(n + "_no_shape", "cacheDrawable is not null return " + cDrawable);
                }
                valueDrawable = cDrawable;
            }
        }

        Drawable defDrawable = null;

        /**
         * 无缓存，生成
         */
        if (valueDrawable == null) {

            Drawable holder = ResourcesCompat.getDrawable(DobyApp.app().getResources(), R.drawable.xemo_holder, null);

            if (holder == null)
                holder = defDrawable = createDefaultDrawable(options.getContext(), tempBean.getName(), options);

            final Drawable tempDefDrawable = defDrawable;

            if (!options.isIgnoreAnim() && isUseAnim && bean.hasJson()) {
                valueDrawable = new AsyLottieDrawable.Builder()
                        .bitmap(((BitmapDrawable) holder).getBitmap())
                        .name(name)
                        .placeDelay(options.getPlaceDelay())
                        .isPauseAnim(options.isPauseAnim())
                        .creator(new AsyLottieDrawable.ICreator() {
                            @Override
                            public void create(@NonNull ICallBack<AsyLottieDrawable.Composition> callBack, @NonNull AsyLottieDrawable drawable) {

                                try {
                                    LottieCompositionFactory.fromUrl(options.getContext(), tempBean.getJson())
                                            .addListener(new LottieListener<LottieComposition>() {
                                                @Override
                                                public void onResult(LottieComposition result) {
//                                                    _95L.iTag("3-3-3", "composition.onResult=" + result
//                                                            + ",time=" + (SystemClock.elapsedRealtime() - drawable.getLoadTime()));
                                                    if (result != null)
                                                        notifySuccess(callBack, result);
                                                    else
                                                        notifyFail(callBack);
//                                                    RP.Dispatchers.getLibsDispatcher().getHandler().postDelayed(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            notifyFail(callBack);
//                                                        }
//                                                    }, 3000);
                                                }
                                            });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    notifyFail(callBack);
                                }
                            }

                            private void notifyFail(@NonNull ICallBack<AsyLottieDrawable.Composition> callBack) {
                                ThreadUtils.execute().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        Drawable drawable = null;

                                        if (!TextUtils.isEmpty(tempBean.getImage())) {
                                            try {
                                                drawable = ImageHelper.getDrawable(options.getContext(), tempBean.getImage());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        if (drawable == null && !options.isHolder())
                                            drawable = tempDefDrawable == null ? createDefaultDrawable(options.getContext(), tempBean.getName(), options) : tempDefDrawable;

                                        callBack.onSuccess(new AsyLottieDrawable.Composition(drawable));
                                    }
                                });
                            }

                            private void notifySuccess(@NonNull ICallBack<AsyLottieDrawable.Composition> callBack, @NonNull LottieComposition com) {
                                callBack.onSuccess(new AsyLottieDrawable.Composition(com));
                            }

                        })
                        .build();
            } else if (bean.hasImage()) {
                valueDrawable = new AsyBitmapDrawable.Builder()
                        .drawable(holder)
                        .url(bean.getImage())
                        .name(name)
                        .placeDelay(options.getPlaceDelay())
                        .creator(new AsyBitmapDrawable.ICreator() {
                            @Override
                            public void create(ICallBack<AsyBitmapDrawable.DrawableWrapper> callBack, AsyBitmapDrawable drawable) {
                                Drawable d = null;

                                if (!TextUtils.isEmpty(tempBean.getImage())) {
                                    try {
                                        d = ImageHelper.getDrawable(options.getContext(), tempBean.getImage());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (d == null && !options.isHolder())
                                    d = createDefaultDrawable(options.getContext(), tempBean.getName(), options);

//                                _95L.iTag(drawable.getName() + "_no_shape", "load bitmap completed!");

                                callBack.onSuccess(new AsyBitmapDrawable.DrawableWrapper(d));
                            }
                        })
                        .build();
            }
        }

        if (valueDrawable == null) {
            valueDrawable = defDrawable == null ? createDefaultDrawable(options.getContext(), bean.getName(), options) : defDrawable;
//            _95L.iTag("em_adapter_request", "createDefaultDrawable as has no json or image");
        }

        final DrawableProxy proxy = new DrawableProxy(options, valueDrawable, isFromCache);
        final ICallBack<DrawableProxy> opCallback = options.getCallBack();

        if (valueDrawable instanceof IAsyBitmapDrawable){
            ((IAsyBitmapDrawable) valueDrawable).addLoadCallback(new CallBack<Drawable>(){
                @Override
                public void onResult(boolean isSuccess, @Nullable Drawable obj, int error, String msg) {
                    super.onResult(isSuccess, obj, error, msg);
                    if (opCallback != null)
                        opCallback.onSuccess(proxy);
                }
            });
        } else if (opCallback != null){
            opCallback.onSuccess(proxy);
        }

        if (!options.isSkipCache() && mCaches != null && valueDrawable != defDrawable) {
            if (mCaches.size() > CACHE_SIZE){
                mCaches.clear();
            }
            mCaches.put(key, new WeakReference<>(valueDrawable));
        }

//        _95L.iTag("em_adapter_request", "return valueDrawable=" + valueDrawable);

        return proxy;
    }

    @NonNull
    public List<ExpressionBean> getNormalBeans(){
        List<ExpressionBean> beans = new ArrayList<>();

        if (mNormalBeans == null)
            return beans;

        beans.addAll(mNormalBeans);

//        beans.add(mNormalBeans.get(0));
//        beans.add(mNormalBeans.get(1));
//        beans.add(mNormalBeans.get(2));
//        beans.add(mNormalBeans.get(3));

        return beans;
    }

    @NonNull
    public List<ExpressionBean> getNormalBeansWithoutCraps(){
        List<ExpressionBean> beans = new ArrayList<>();

        if (mNormalBeans == null)
            return beans;

        for (int i = 0; i < mNormalBeans.size(); i++) {
            ExpressionBean bean = mNormalBeans.get(i);
            if (bean != null && bean.getId() != 54)
                beans.add(bean);
        }

        return beans;
    }

    @NonNull
    public List<ExpressionBean> getVipBeans(){

        List<ExpressionBean> beans = new ArrayList<>();

        if (mVipBeans == null)
            return beans;

        beans.addAll(mVipBeans);

        return beans;
    }

    private boolean isNeedExpand(ExpressionBean bean) {
        return bean != null && bean.isVip();
    }

    public boolean hasEntity(int id){
        return mAllBeans != null && mAllBeans.get(id) != null;
    }

    public boolean hasEntity(String idStr){
        return hasEntity(EmResOptions.parseId(idStr));
    }

    /**
     * 是否包含vip表情
     * @param content
     * @return
     */
    public boolean hasVipExpression(String content){

        if (!TextUtils.isEmpty(content)) {
            List<ExpressionBean> vipBeans = getAllBeansBy(true, false);
            for (int i = 0; i < vipBeans.size(); i++) {
                ExpressionBean bean = vipBeans.get(i);
                if (bean != null && content.contains(String.format(Locale.CHINESE, "[em:%d]", bean.getId())))
                    return true;
            }
        }

        return false;
    }

    /**
     * 包括隐藏部分
     * @param isVip
     * @return
     */
    @NonNull
    public List<ExpressionBean> getAllBeansBy(boolean isVip, boolean isExcludeHide){
        List<ExpressionBean> beans = new ArrayList<>();

        if (mAllBeans == null)
            return beans;

        for (int i = 0; i < mAllBeans.size(); i++) {
            ExpressionBean bean = mAllBeans.valueAt(i);
            if (bean != null
                    && (!isVip || bean.isVip())
                    && (!isExcludeHide || bean.isHide()))
                beans.add(bean);
        }

        return beans;
    }

    private @NonNull Drawable createDefaultDrawable(Context context, String name, @NonNull EmResOptions options){
        TextView emTextView = new TextView(context);
        emTextView.setDrawingCacheEnabled(true);
        emTextView.setTextColor(options.getDefTxtColor());
        emTextView.getPaint().setFakeBoldText(options.getDefTxtBold());
        int textSize = ScreenUtils.dip2px(DobyApp.app(), 14);
        emTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        emTextView.setGravity(Gravity.CENTER);
//        int padding = ScreenUtils.dip2px(3);
//        emTextView.setPadding(padding, padding, padding, padding);
        emTextView.setIncludeFontPadding(false);
        String txt = String.format("[%s]", name);
        emTextView.setText(txt);

        emTextView.setHeight(options.getHeight());
        emTextView.setWidth(Float.valueOf(emTextView.getPaint().measureText(txt)).intValue() + ScreenUtils.dip2px(DobyApp.app(), 8));

        emTextView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        emTextView.layout(0, 0, emTextView.getMeasuredWidth(), emTextView.getMeasuredHeight());
        Bitmap levelCache = Bitmap.createBitmap(emTextView.getDrawingCache(), 0, 0, emTextView.getMeasuredWidth(), emTextView.getMeasuredHeight());
        emTextView.setDrawingCacheEnabled(false);
        emTextView.destroyDrawingCache();

        return new BitmapDrawable(context.getResources(), levelCache);
    }

    public static class DrawableProxy{
        @NonNull EmResOptions options;
        @NonNull Context context;
        @NonNull Drawable drawable;
        boolean isFromCache;

        private DrawableProxy(@NonNull EmResOptions options, @NonNull Drawable drawable, boolean isFromCache){
            this.options = options;
            this.context = options.getContext();
            this.drawable = drawable;
            this.isFromCache = isFromCache;

            if (!isFromCache)
                setDrawableBound(drawable, options.getHeight(), options.getScaleX(), options.getScaleY());
        }

        private DrawableProxy(@NonNull EmResOptions options, @NonNull Drawable drawable){
            this(options, drawable, false);
        }

        public static void setDrawableBound(Drawable drawable, int height, float scaleX, float scaleY){

            if (drawable == null)
                return;

            if (scaleX == 0) scaleX = 1;
            if (scaleY == 0) scaleY = 1;

            if (height > 0){
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                float factor = (float)height / intrinsicHeight;
                int right = Float.valueOf(intrinsicWidth * factor).intValue();
                drawable.setBounds(0, 0, right, height);
//                _95L.iTag("out_bound_size", "has height > 0, intrinsicWidth=" + intrinsicWidth
//                        + ",intrinsicHeight=" + intrinsicHeight
//                        + ",factor=" + factor
//                        + ",right=" + right
//                        + ",height=" + height
//                        + ",name=" + (drawable instanceof IAsyBitmapDrawable ? ((IAsyBitmapDrawable) drawable).getName() : ""));
            } else {
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                int right = Float.valueOf(drawable.getIntrinsicWidth() * scaleX).intValue();
                int bottom = Float.valueOf(drawable.getIntrinsicHeight() * scaleY).intValue();
                drawable.setBounds(0, 0, right, bottom);
//                _95L.iTag("out_bound_size", "height is 0, intrinsicWidth=" + intrinsicWidth
//                        + ",intrinsicHeight=" + intrinsicHeight
//                        + ",scaleX=" + scaleX
//                        + ",scaleY=" + scaleY
//                        + ",right=" + right
//                        + ",bottom=" + bottom
//                        + ",name=" + (drawable instanceof IAsyBitmapDrawable ? ((IAsyBitmapDrawable) drawable).getName() : ""));
            }
        }

        @NonNull
        public Drawable value(){
            return drawable;
        }

        @Nullable
        public Bitmap bitmap(){
            if (drawable instanceof BitmapDrawable){
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof IAsyBitmapDrawable){
                return ((IAsyBitmapDrawable) drawable).getBitmap();
            }
            return null;
        }

        @NonNull
        public ImageSpan span(){
            if (drawable instanceof IAsyAnimDrawable)
                return new AsyAnimDrawableSpan(context, (IAsyAnimDrawable) value(), options.getSpanAlign());
            if (drawable instanceof IAsyBitmapDrawable)
                return new AsyBitmapDrawableSpan((IAsyBitmapDrawable) value(), options.getSpanAlign());
            return new ImageSpanC(value(), options.getSpanAlign());
        }

        @NonNull
        public SpannableString string(){
            SpannableString spanStr = new SpannableString("img ");
            spanStr.setSpan(span(), 0, spanStr.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spanStr;
        }

        public boolean isAlive(){
//            if (context instanceof CommonActivity)
//                return ((CommonActivity) context).isAlive();
//            return false;
            return true;
        }

    }
}
