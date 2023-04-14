package com.example.lottiedemo.rich.utils;

import static com.example.lottiedemo.rich.utils.CallbackView.addIfLackView;
import static com.example.lottiedemo.rich.utils.CallbackView.checkSize;
import static com.example.lottiedemo.rich.utils.CallbackView.isCheckNullEmpty;
import static com.example.lottiedemo.rich.utils.CallbackView.iteratorView;
import static com.example.lottiedemo.rich.utils.CallbackView.removeIfContainsView;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.example.lottiedemo.DobyApp;
import com.example.lottiedemo.rich.other.HandleManager;
import com.example.lottiedemo.rich.other.ObjectUtils;
import com.example.lottiedemo.rich.other.ViewHelper;
import com.example.lottiedemo.utils.CallBack;


import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.example.lottiedemo.rich.utils.IAsyAnimDrawable.getTag;
import static com.example.lottiedemo.rich.utils.IAsyAnimDrawable.setTag;
import static com.example.lottiedemo.rich.utils.RichUtils.isViewNotAlive;
import static com.example.lottiedemo.rich.utils.RichUtils.isVisibleRectShown;

/**
 * Created by rgy on 2021/4/9 0009.
 * 异步lottie-drawable，非span请勿使用
 * 这个drawable可能会全局缓存
 */
public class AsyLottieDrawable extends LottieDrawable implements IAsyAnimDrawable {

    private static final String TAG = AsyLottieDrawable.class.getSimpleName();

    @NonNull
    Bitmap mBitmap;
    String mFileName;
    String mImageDir;
    String mFileUrl;
    ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;

    private boolean isLoadCompleted;

    String progressKey;

    private final Set<WeakReference<View>> viewSet = new HashSet<>();
    private List<ICallBack<Drawable>> loadCallbacks = new LinkedList<>();

    /**
     * 用来标记log日志
     */
    private String name;

    /**
     * 暂停标记
     */
    private boolean isPause;

    /**
     * 占位图延时，超过这个时间显示
     */
    private long placeDelay = 0;

    /**
     * 加载时间
     */
    private long loadTime;

    /**
     * 暂停动画代替stop
     */
    private boolean isPauseAnim;

    /**
     * 用来判断view是否可见
     */
    private Rect mRect = new Rect();

    /**
     * 延时刷新
     */
    private Runnable mRunnable;

    public static class Builder {
        Bitmap bitmap;
        String fileName;
        String imageDir;
        String fileUrl;
        String name;
        long placeDelay;
        ICreator creator;
        String progressKey;
        boolean isPauseAnim;

        public AsyLottieDrawable build(){
            return new AsyLottieDrawable(this);
        }

        public Builder progressKey(String progressKey) {
            this.progressKey = progressKey;
            return this;
        }

        public Builder bitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder drawable(Drawable drawable) {
            if (drawable instanceof BitmapDrawable)
                return bitmap(((BitmapDrawable) drawable).getBitmap());
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder imageDir(String imageDir) {
            this.imageDir = imageDir;
            return this;
        }

        public Builder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder placeDelay(long placeDelay) {
            this.placeDelay = placeDelay;
            return this;
        }

        public Builder creator(ICreator creator) {
            this.creator = creator;
            return this;
        }

        public Builder isPauseAnim(boolean isPauseAnim){
            this.isPauseAnim = isPauseAnim;
            return this;
        }
    }

    /**
     * 创建器，自行创建
     */
    public interface ICreator{
        @WorkerThread
        void create(ICallBack<Composition> callBack, AsyLottieDrawable drawable);
    }

    public static class Composition{
        private @Nullable LottieComposition composition;
        private @Nullable Drawable drawable;

        public Composition() {
        }

        public Composition(LottieComposition composition) {
            this.composition = composition;
        }

        public Composition(Drawable drawable) {
            this.drawable = drawable;
        }

        public LottieComposition getComposition() {
            return composition;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public boolean hasContent(){
            return composition != null || drawable != null;
        }
    }

    public AsyLottieDrawable(@NonNull Builder builder){
        this.mBitmap = builder.bitmap;
        this.mFileName = builder.fileName;
        this.mImageDir = builder.imageDir;
        this.mFileUrl = builder.fileUrl;
        this.name = builder.name;
        this.placeDelay = builder.placeDelay;
        this.progressKey = builder.progressKey;
        this.isPauseAnim = builder.isPauseAnim;
        asyFile(builder.creator);
    }

    /**
     * ICreator很可能会引用外面的类，不要存放到字段，只使用一次，以免泄露
     * @param creator
     */
    private void asyFile(ICreator creator){

        loadTime = SystemClock.elapsedRealtime();

        if (mRunnable == null && placeDelay > 0) {
            HandleManager.getInstance().getHandler().postDelayed(mRunnable = new Runnable() {
                @Override
                public void run() {
                    iteratorView(viewSet, new CallBack<View>(){
                        @Override
                        public void onSuccess(View obj) {
                            super.onSuccess(obj);
                            obj.invalidate();
                        }
                    });
                    mRunnable = null;
                }
            }, placeDelay + 50);
        }

        Observable.create(new ObservableOnSubscribe<Composition>() {

            @Override
            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<Composition> emitter) {
                if (creator != null){
                    log("3-3-3", "start creator" + logLoadTime());
                    creator.create(new CallBack<Composition>(){
                        @Override
                        public void onSuccess(Composition obj) {
                            super.onSuccess(obj);
                            emitter.onNext(obj);
                        }

                        @Override
                        public void onFail(int error, String msg) {
                            super.onFail(error, msg);
                            emitter.onNext(new Composition());
                        }
                    }, AsyLottieDrawable.this);
                } else if (!TextUtils.isEmpty(mFileName)){
                    log("mFileName is not null");
                    emitter.onNext(new Composition(LottieCompositionFactory.fromAssetSync(DobyApp.app(), mFileName).getValue()));
                } else if (!TextUtils.isEmpty(mFileUrl)){
                    log("mFileUrl is not null");
                    emitter.onNext(new Composition(LottieCompositionFactory.fromUrlSync(DobyApp.app(), mFileUrl).getValue()));
                } else {
                    log("asyFile nothing load!");
                    emitter.onNext(new Composition());
                }

            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Composition>() {
                    @Override
                    public void accept(Composition composition) throws Exception {

                        isLoadCompleted = true;

                        log("_asy_draw", "accept,composition=" + composition + logLoadTime());

                        if (composition != null && composition.hasContent()){

                            LottieComposition com = composition.getComposition();
                            Drawable drawable = composition.getDrawable();

                            if (com != null) {
                                if (!TextUtils.isEmpty(mImageDir))
                                    setImagesAssetsFolder(mImageDir);
                                int height = getBounds().height();
                                int intrinsicWidth = com.getBounds().width();
                                int intrinsicHeight = com.getBounds().height();
                                float factor = (float) height / intrinsicHeight;
                                int right = Float.valueOf(intrinsicWidth * factor).intValue();
                                setBounds(0, 0, right, height);
                                setRepeatCount(-1);
//                                setScale(factor);

                                log("_asy_draw", "accept complete, intrinsicWidth=" + intrinsicWidth
                                        + ",intrinsicHeight=" + intrinsicHeight
                                        + ",factor=" + factor
                                        + ",right=" + right
                                        + ",height=" + height
                                        + ",isAnimating=" + isAnimating()
                                        + ",callback=" + getCallback());

                                setComposition(com);

                                playSpanAnimation();

                            } else if (drawable != null){

                                /**
                                 * 移除runnable
                                 */
                                if (mRunnable != null){
                                    HandleManager.getInstance().getHandler().removeCallbacks(mRunnable);
                                    mRunnable = null;
                                }

                                log("_asy_draw", "load Composition fail,set default bitmap");
                                mBitmap = ((BitmapDrawable)drawable).getBitmap();

                                // 更新bounds
                                int height = getBounds().height();
                                int intrinsicWidth = mBitmap.getWidth();
                                int intrinsicHeight = mBitmap.getHeight();
                                float factor = (float) height / intrinsicHeight;
                                setBounds(0, 0, Float.valueOf(intrinsicWidth * factor).intValue(), height);

                                iteratorView(viewSet, new CallBack<View>(){

                                    @Override
                                    public void onSuccess(View view) {
                                        super.onSuccess(view);
                                        view.invalidate();
                                    }
                                });
                            }
                        }

                        Set<WeakReference<View>> views = new HashSet<>(viewSet);
                        iteratorView(views, new CallBack<View>(){

                            @Override
                            public void onSuccess(View view) {
                                super.onSuccess(view);
                                if (view instanceof IAsyBitmapView) {
                                    ((IAsyBitmapView) view).onDrawableLoadCompleted(AsyLottieDrawable.this);
                                }
                            }
                        });

                        synchronized (AsyLottieDrawable.this){

                            if (ObjectUtils.isNotEmpty(loadCallbacks)){
                                for (ICallBack<Drawable> loadCallback : loadCallbacks) {
                                    if (loadCallback != null) {
                                        if (composition != null && composition.getComposition() != null)
                                            loadCallback.onSuccess(AsyLottieDrawable.this);
                                        else
                                            loadCallback.onFail(0 , "");
                                    }
                                }
                                loadCallbacks.clear();
                            }

                            /**
                             * 避免泄露，callback只会使用一次
                             */
                            loadCallbacks = null;
                        }

                    }
                });

    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        ViewHelper.checkRichCallback(getCallback(), viewSet);

        if (!isAnimating()) {

            playSpanAnimation();

            if (loadCallbacks == null
                    || placeDelay <= 0
                    || SystemClock.elapsedRealtime() - loadTime > placeDelay) {
                Matrix matrix = new Matrix();
                float scaleX = (float) getBounds().width() / mBitmap.getWidth();
                float scaleY = (float) getBounds().height() / mBitmap.getHeight();
                float scale = Math.min(scaleX, scaleY);
                int x = Float.valueOf((getBounds().width() - scale * mBitmap.getWidth()) / 2).intValue();
                int y = Float.valueOf((getBounds().height() - scale * mBitmap.getHeight()) / 2).intValue();
                matrix.postScale(scale, scale);
                matrix.postTranslate(x, y);

                canvas.drawBitmap(mBitmap
                        , matrix
                        , new Paint());
            } else {
                super.draw(canvas);
            }
        } else {
            super.draw(canvas);
        }
    }

    @Override
    public void attach(@NonNull View view) {
        addIfLackView(viewSet, view);
        checkCallback();
        playSpanAnimation();
        log("_asy_draw","attach:view=" + view + ",viewSet.size=" + checkSize(viewSet));
    }

    @Override
    public void detach(@NonNull View view) {
        if (getTag(view) == this)
            setTag(view);
        removeIfContainsView(viewSet, view);
        if (isCheckNullEmpty(viewSet)) {
            log("_asy_draw", "detach: stopAnimation");
            endSpanAnimation();
        }
        log("_asy_draw","detach:view=" + view + ",viewSet.size=" + checkSize(viewSet));
    }

    private void checkCallback(){
        if (!(getCallback() instanceof CallbackView)) {
            setCallback(new CallbackView(viewSet));
        }
    }

    private boolean playSpanAnimation() {

        log("_asy_draw", "try playSpanAnimation,size=" + viewSet.size()
                + ",isLoadCompleted=" + isLoadCompleted()
                + ",isAnimating()=" + isAnimating()
                + ",isCheckNullEmpty=" + isCheckNullEmpty(viewSet));

        if (!isLoadCompleted() || getComposition() == null || isAnimating()){
            return false;
        }

        endSpanAnimation();

        if (isCheckNullEmpty(viewSet)){
            return false;
        }

        if (mAnimatorUpdateListener == null) {
            addAnimatorUpdateListener(mAnimatorUpdateListener = valueAnimator -> {

//                log("anim_callback", "viewSet.size=" + checkSize(viewSet));

                if (isCheckNullEmpty(viewSet)){
                    endSpanAnimation();
                    log("_asy_draw", "animator.isCheckNullEmpty stop animation");
                    return;
                }

                iteratorView(viewSet, animatorCallback);

            });
        }

        /**
         * 移除runnable
         */
        if (mRunnable != null){
            HandleManager.getInstance().getHandler().removeCallbacks(mRunnable);
            mRunnable = null;
        }

        playAnimation();

        return true;
    }

    /**
     * 避免每次创建callback
     */
    private final ICallBack<View> animatorCallback = new CallBack<View>() {
        @Override
        public void onSuccess(View v) {
            super.onSuccess(v);

//                        if (v instanceof IAsyBitmapView){
//                            log("anim_callback", "v.name=" + ((IAsyBitmapView)v).name());
//                            if (((IAsyBitmapView)v).name().contains(RichEditText.class.getSimpleName()))
//                                log("I am Edit");
//                            else if (((IAsyBitmapView)v).name().contains(RichTextView.class.getSimpleName()))
//                                log("I am textView");
//                            else if (((IAsyBitmapView)v).name().contains(AsyAnimImageView.class.getSimpleName()))
//                                log("I am imageView");
//                            else
//                                log("I am nothing!");
//                        }

            if (getTag(v) == null || getTag(v) == AsyLottieDrawable.this) {

                boolean isVisibleRectShown = isVisibleRectShown(mRect, v);
                /**
                 * 实际上就是detachFromWindow
                 */
                boolean isViewNotAlive = isViewNotAlive(v);

                if (v.willNotDraw() || !v.isShown() || !isVisibleRectShown || isViewNotAlive) {
                    /*log("_asy_draw", "v.willNotDraw=" + v.willNotDraw()
                            + ",v.isShown()=" + v.isShown()
                            + ",isVisibleRectShown=" + isVisibleRectShown
                            + ",isViewNotAlive=" + isViewNotAlive);*/
                    setTag(v);
                    return;
                }

                v.invalidate();
                if (getTag(v) == null)
                    setTag(v, AsyLottieDrawable.this);

            }

        }
    };

    private void endSpanAnimation() {
        log("_asy_draw", "endSpanAnimation, isPause=" + isPause + ",isPauseAnim=" + isPauseAnim);
        if (!isPauseAnim){
            endAnimation();
        } else {
            pauseAnimation();
            isPause = true;
        }
        setCallback(null);
        if (mAnimatorUpdateListener != null) {
            removeAnimatorUpdateListener(mAnimatorUpdateListener);
            mAnimatorUpdateListener = null;
        }
    }

    @Override
    public void playAnimation() {

        log("_asy_draw", "playAnimation execute!isPause=" + isPause
                + ",isPauseAnim=" + isPauseAnim
                + ",isAnimating=" + isAnimating()
                + ",isLoadCompleted=" + isLoadCompleted());

        checkCallback();

        if (isPause){
            resumeAnimation();
            isPause = false;
        } else {
            super.playAnimation();
        }
    }

    public void setProgressKey(String progressKey) {
        this.progressKey = progressKey;
    }

    @Override
    public float getProgress() {
        return super.getProgress();
    }

    @Override
    public Drawable getTarget() {
        return this;
    }

    @Override
    public boolean isLoadCompleted() {
        return isLoadCompleted;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        log("onBoundsChange,bounds=" + bounds);
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);
        log("out_bound_size", "setBounds.Rect=" + bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        log("out_bound_size", "left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom);
    }

    public long getLoadTime() {
        return loadTime;
    }

    private String logLoadTime(){
        return ",time=" + (SystemClock.elapsedRealtime() - loadTime);
    }

    @Override
    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public int getIntrinsicWidth(){
        int intrinsicWidth = 0;
        if (getComposition() != null) {
            intrinsicWidth = super.getIntrinsicWidth();
//            log("out_bound_size", "getIntrinsicWidth getComposition not null,intrinsicWidth=" + intrinsicWidth);
            return intrinsicWidth;
        }
        intrinsicWidth = mBitmap.getWidth();
//        log("out_bound_size", "getIntrinsicWidth getComposition is null,intrinsicWidth=" + intrinsicWidth);
        return intrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        int intrinsicHeight = 0;
        if (getComposition() != null) {
            intrinsicHeight = super.getIntrinsicHeight();
//            log("out_bound_size", "getIntrinsicHeight getComposition not null,intrinsicHeight=" + intrinsicHeight);
            return intrinsicHeight;
        }
        intrinsicHeight = mBitmap.getHeight();
//        log("out_bound_size", "getIntrinsicHeight getComposition is null,intrinsicHeight=" + intrinsicHeight);
        return intrinsicHeight;
    }

    @Override
    public void addLoadCallback(ICallBack<Drawable> callback) {
        if (loadCallbacks == null){
            if (callback != null)
                callback.onSuccess(this);
            return;
        }
        synchronized (this){
            if (loadCallbacks != null){
                loadCallbacks.add(callback);
                return;
            }
            callback.onSuccess(this);
        }
    }

//    @Override
//    public boolean checkView() {
//        return isAllViewLegal(viewSet);
//    }

    @Override
    public boolean isStateful() {
        Callback c = getCallback();
        log("callback_isStateful", "isStateful execute from " + this.getClass().getSimpleName()
                + ",callback=" + c);
        if (c != null && !(c instanceof CallbackView) && !(c instanceof IAsyBitmapView))
            throw new IllegalStateException("you must set " + this.getClass().getSimpleName() + " in a " + RichTextView.class.getSimpleName());
        return super.isStateful();
    }

    @Override
    public String getName() {
        return TAG + "_" + flag();
    }

    private void log(String msg){
        log(TAG, msg);
    }

    private void log(String tag, String msg){
//        _95L.iTag(TAG + "_" + tag, flag() + "--->" + msg);
    }

    private String flag(){
        return TextUtils.isEmpty(name) ? "" : ("name=" + name + "(" + hashCode() + ")");
    }

    @Override
    public String toString(){
        return TextUtils.isEmpty(name) ? super.toString() : flag();
    }
}
