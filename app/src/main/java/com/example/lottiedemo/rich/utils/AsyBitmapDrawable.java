package com.example.lottiedemo.rich.utils;


import static com.example.lottiedemo.rich.utils.CallbackView.addIfLackView;
import static com.example.lottiedemo.rich.utils.CallbackView.checkSize;
import static com.example.lottiedemo.rich.utils.CallbackView.iteratorView;
import static com.example.lottiedemo.rich.utils.CallbackView.removeIfContainsView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.example.lottiedemo.DobyApp;
import com.example.lottiedemo.rich.other.HandleManager;
import com.example.lottiedemo.rich.other.ImageHelper;
import com.example.lottiedemo.rich.other.ObjectUtils;
import com.example.lottiedemo.rich.other.ViewHelper;
import com.example.lottiedemo.utils.CallBack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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



/**
 * 仅仅只能用于RichTextView结合使用，其他类似ImageView的使用ImageHelper
 * 这个drawable可能会全局缓存
 */
public class AsyBitmapDrawable extends Drawable implements IAsyBitmapDrawable {

    private static final String TAG = "BitmapAsy";

    private static final int DEFAULT_PAINT_FLAGS =
            Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG;
    private BitmapState mBitmapState;
    private Bitmap mBitmap;
    private int mTargetDensity;

    private final Rect mDstRect = new Rect();   // Gravity.apply() sets this

    private boolean mApplyGravity;
    private boolean mMutated;

    // These are scaled to match the target density.
    private int mBitmapWidth;
    private int mBitmapHeight;

    private boolean isLoadCompleted;
    private String url;

    private List<ICallBack<Drawable>> loadCallbacks = new LinkedList<>();
    private final Set<WeakReference<View>> viewSet = new HashSet<>();

    private String name;

    /**
     * 占位图延时，超过这个时间显示
     */
    private long placeDelay = 0;
    /**
     * 加载时间
     */
    private long loadTime;
    /**
     * 延时刷新
     */
    private Runnable mRunnable;

    private Builder builder;

    /**
     * 创建器，自行创建
     */
    public interface ICreator{
        @WorkerThread
        void create(ICallBack<DrawableWrapper> callBack, AsyBitmapDrawable drawable);
    }

    public static class Builder{
        Bitmap bitmap;
        Drawable drawable;
        String url;
        long placeDelay = 0;
        String name;
        ICreator creator;

        public AsyBitmapDrawable build(){
            return new AsyBitmapDrawable(this);
        }

        public Builder bitmap(Bitmap bitmap){
            this.bitmap = bitmap;
            return this;
        }

        @Nullable
        public Bitmap getBitmap(){
            if (bitmap != null)
                return bitmap;
            if (drawable instanceof BitmapDrawable)
                return ((BitmapDrawable) drawable).getBitmap();
            return null;
        }

        public Builder drawable(Drawable drawable){
            this.drawable = drawable;
            return this;
        }

        private void setBounds(AsyBitmapDrawable d){
            if (d != null && drawable != null){
                d.setBounds(drawable.getBounds());
            }
        }

        public Builder placeDelay(long placeDelay){
            this.placeDelay = placeDelay;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder creator(ICreator creator){
            this.creator = creator;
            return this;
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }
    }

    /**
     * Create drawable from a bitmap, setting initial target density based on
     * the display metrics of the resources.
     */
    public AsyBitmapDrawable(@NonNull Builder builder) {
        this(new BitmapState(builder.getBitmap(), builder), DobyApp.app().getResources(), builder);
        init(DobyApp.app(), builder.creator);
    }

    /**
     * Returns the paint used to render this drawable.
     */
    public final Paint getPaint() {
        return mBitmapState.mPaint;
    }

    /**
     * Returns the bitmap used by this drawable to render. May be null.
     */
    public final Bitmap getBitmap() {
        return mBitmap;
    }

    private void computeBitmapSize() {
        checkCallback();
        mBitmapWidth = mBitmap.getScaledWidth(mTargetDensity);
        mBitmapHeight = mBitmap.getScaledHeight(mTargetDensity);
    }

    public void setBitmap(Drawable drawable) {
        checkCallback();
        if (drawable instanceof BitmapDrawable)
            setBitmap(((BitmapDrawable) drawable).getBitmap());
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != mBitmap) {
            mBitmap = bitmap;
            if (bitmap != null) {
                computeBitmapSize();
            } else {
                mBitmapWidth = mBitmapHeight = -1;
            }
            invalidateSelf();
        }
    }

    /**
     * Set the density scale at which this drawable will be rendered. This
     * method assumes the drawable will be rendered at the same density as the
     * specified canvas.
     *
     * @param canvas The Canvas from which the density scale must be obtained.
     * @see Bitmap#setDensity(int)
     * @see Bitmap#getDensity()
     */
    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    /**
     * Set the density scale at which this drawable will be rendered.
     *
     * @param metrics The DisplayMetrics indicating the density scale for this drawable.
     * @see Bitmap#setDensity(int)
     * @see Bitmap#getDensity()
     */
    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    /**
     * Set the density at which this drawable will be rendered.
     *
     * @param density The density scale for this drawable.
     * @see Bitmap#setDensity(int)
     * @see Bitmap#getDensity()
     */
    public void setTargetDensity(int density) {
        if (mTargetDensity != density) {
            mTargetDensity = density == 0 ? DisplayMetrics.DENSITY_DEFAULT : density;
            if (mBitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    /**
     * Get the gravity used to position/stretch the bitmap within its bounds.
     * See android.view.Gravity
     *
     * @return the gravity applied to the bitmap
     */
    public int getGravity() {
        return mBitmapState.mGravity;
    }

    /**
     * Set the gravity used to position/stretch the bitmap within its bounds.
     * See android.view.Gravity
     *
     * @param gravity the gravity
     */
    public void setGravity(int gravity) {
        if (mBitmapState.mGravity != gravity) {
            mBitmapState.mGravity = gravity;
            mApplyGravity = true;
            invalidateSelf();
        }
    }

    /**
     * Enables or disables anti-aliasing for this drawable. Anti-aliasing affects
     * the edges of the bitmap only so it applies only when the drawable is rotated.
     *
     * @param aa True if the bitmap should be anti-aliased, false otherwise.
     */
    public void setAntiAlias(boolean aa) {
        mBitmapState.mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mBitmapState.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mBitmapState.mPaint.setDither(dither);
        invalidateSelf();
    }

    /**
     * Indicates the repeat behavior of this drawable on the X axis.
     *
     * @return {@link Shader.TileMode#CLAMP} if the bitmap does not repeat,
     * {@link Shader.TileMode#REPEAT} or {@link Shader.TileMode#MIRROR} otherwise.
     */
    public Shader.TileMode getTileModeX() {
        return mBitmapState.mTileModeX;
    }

    /**
     * Indicates the repeat behavior of this drawable on the Y axis.
     *
     * @return {@link Shader.TileMode#CLAMP} if the bitmap does not repeat,
     * {@link Shader.TileMode#REPEAT} or {@link Shader.TileMode#MIRROR} otherwise.
     */
    public Shader.TileMode getTileModeY() {
        return mBitmapState.mTileModeY;
    }

    /**
     * Sets the repeat behavior of this drawable on the X axis. By default, the drawable
     * does not repeat its bitmap. Using {@link Shader.TileMode#REPEAT} or
     * {@link Shader.TileMode#MIRROR} the bitmap can be repeated (or tiled) if the bitmap
     * is smaller than this drawable.
     *
     * @param mode The repeat mode for this drawable.
     * @see #setTileModeY(Shader.TileMode)
     * @see #setTileModeXY(Shader.TileMode, Shader.TileMode)
     */
    public void setTileModeX(Shader.TileMode mode) {
        setTileModeXY(mode, mBitmapState.mTileModeY);
    }

    /**
     * Sets the repeat behavior of this drawable on the Y axis. By default, the drawable
     * does not repeat its bitmap. Using {@link Shader.TileMode#REPEAT} or
     * {@link Shader.TileMode#MIRROR} the bitmap can be repeated (or tiled) if the bitmap
     * is smaller than this drawable.
     *
     * @param mode The repeat mode for this drawable.
     * @see #setTileModeX(Shader.TileMode)
     * @see #setTileModeXY(Shader.TileMode, Shader.TileMode)
     */
    public final void setTileModeY(Shader.TileMode mode) {
        setTileModeXY(mBitmapState.mTileModeX, mode);
    }

    /**
     * Sets the repeat behavior of this drawable on both axis. By default, the drawable
     * does not repeat its bitmap. Using {@link Shader.TileMode#REPEAT} or
     * {@link Shader.TileMode#MIRROR} the bitmap can be repeated (or tiled) if the bitmap
     * is smaller than this drawable.
     *
     * @param xmode The X repeat mode for this drawable.
     * @param ymode The Y repeat mode for this drawable.
     * @see #setTileModeX(Shader.TileMode)
     * @see #setTileModeY(Shader.TileMode)
     */
    public void setTileModeXY(Shader.TileMode xmode, Shader.TileMode ymode) {
        final BitmapState state = mBitmapState;
        if (state.mTileModeX != xmode || state.mTileModeY != ymode) {
            state.mTileModeX = xmode;
            state.mTileModeY = ymode;
            state.mRebuildShader = true;
            invalidateSelf();
        }
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mBitmapState.mChangingConfigurations;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mApplyGravity = true;
    }

    @Override
    public void draw(Canvas canvas) {

        ViewHelper.checkRichCallback(getCallback(), viewSet);

        long pastTime = SystemClock.elapsedRealtime() - loadTime;

        log("no_shape_asy_draw", "prepare to draw it,isLoadCompleted=" + isLoadCompleted
                + ",placeDelay=" + placeDelay
                + ",pastTime=" + pastTime
                + ",callback=" + getCallback());

        if (!isLoadCompleted
                && placeDelay > 0
                && pastTime < placeDelay) {
            log("no_shape", "it's not time to draw!");
            return;
        }

        Bitmap bitmap = mBitmap;
        if (bitmap != null) {
            final BitmapState state = mBitmapState;
            if (state.mRebuildShader) {
                Shader.TileMode tmx = state.mTileModeX;
                Shader.TileMode tmy = state.mTileModeY;

                if (tmx == null && tmy == null) {
                    state.mPaint.setShader(null);
                } else {
                    state.mPaint.setShader(new BitmapShader(bitmap,
                            tmx == null ? Shader.TileMode.CLAMP : tmx,
                            tmy == null ? Shader.TileMode.CLAMP : tmy));
                }
                state.mRebuildShader = false;
                copyBounds(mDstRect);
            }

            Shader shader = state.mPaint.getShader();
            if (shader == null) {
                if (mApplyGravity) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        final int layoutDirection = 0;//getResolvedLayoutDirectionSelf();
                        Gravity.apply(state.mGravity, mBitmapWidth, mBitmapHeight,
                                getBounds(), mDstRect, layoutDirection);
                    }
                    mApplyGravity = false;
                }
                canvas.drawBitmap(bitmap, null, mDstRect, state.mPaint);
            } else {
                if (mApplyGravity) {
                    copyBounds(mDstRect);
                    mApplyGravity = false;
                }
                canvas.drawRect(mDstRect, state.mPaint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        int oldAlpha = mBitmapState.mPaint.getAlpha();
        if (alpha != oldAlpha) {
            mBitmapState.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapState.mPaint.setColorFilter(cf);
        invalidateSelf();
    }

    /**
     * A mutable BitmapDrawable still shares its Bitmap with any other Drawable
     * that comes from the same resource.
     *
     * @return This drawable.
     */
    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mBitmapState = new BitmapState(mBitmapState, builder);
            mMutated = true;
        }
        return this;
    }

    @Deprecated
    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs)
            throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs);

        /*TypedArray a = r.obtainAttributes(attrs, com.android.internal.R.styleable.BitmapDrawable);

        final int id = a.getResourceId(com.android.internal.R.styleable.BitmapDrawable_src, 0);
        if (id == 0) {
            throw new XmlPullParserException(parser.getPositionDescription() +
                    ": <bitmap> requires a valid src attribute");
        }
        final Bitmap bitmap = BitmapFactory.decodeResource(r, id);
        if (bitmap == null) {
            throw new XmlPullParserException(parser.getPositionDescription() +
                    ": <bitmap> requires a valid src attribute");
        }
        mBitmapState.mBitmap = bitmap;
        setBitmap(bitmap);
        setTargetDensity(r.getDisplayMetrics());

        final Paint paint = mBitmapState.mPaint;
        paint.setAntiAlias(a.getBoolean(com.android.internal.R.styleable.BitmapDrawable_antialias,
                paint.isAntiAlias()));
        paint.setFilterBitmap(a.getBoolean(com.android.internal.R.styleable.BitmapDrawable_filter,
                paint.isFilterBitmap()));
        paint.setDither(a.getBoolean(com.android.internal.R.styleable.BitmapDrawable_dither,
                paint.isDither()));
        setGravity(a.getInt(com.android.internal.R.styleable.BitmapDrawable_gravity, Gravity.FILL));
        int tileMode = a.getInt(com.android.internal.R.styleable.BitmapDrawable_tileMode, -1);
        if (tileMode != -1) {
            switch (tileMode) {
                case 0:
                    setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    break;
                case 1:
                    setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    break;
                case 2:
                    setTileModeXY(Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                    break;
            }
        }

        a.recycle();*/
    }

    @Override
    public int getOpacity() {
        if (mBitmapState.mGravity != Gravity.FILL) {
            return PixelFormat.TRANSLUCENT;
        }
        Bitmap bm = mBitmap;
        return (bm == null || bm.hasAlpha() || mBitmapState.mPaint.getAlpha() < 255) ?
                PixelFormat.TRANSLUCENT : PixelFormat.OPAQUE;
    }

    @Override
    public final ConstantState getConstantState() {
        mBitmapState.mChangingConfigurations = getChangingConfigurations();
        return mBitmapState;
    }

    final static class BitmapState extends ConstantState {
        Bitmap mBitmap;
        int mChangingConfigurations;
        int mGravity = Gravity.FILL;
        Paint mPaint = new Paint(DEFAULT_PAINT_FLAGS);
        Shader.TileMode mTileModeX = null;
        Shader.TileMode mTileModeY = null;
        int mTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        boolean mRebuildShader;
        Builder builder;

        BitmapState(Bitmap bitmap, Builder builder) {
            mBitmap = bitmap;
            this.builder = builder;
        }

        BitmapState(BitmapState bitmapState, Builder builder) {
            this(bitmapState.mBitmap, builder);
            mChangingConfigurations = bitmapState.mChangingConfigurations;
            mGravity = bitmapState.mGravity;
            mTileModeX = bitmapState.mTileModeX;
            mTileModeY = bitmapState.mTileModeY;
            mTargetDensity = bitmapState.mTargetDensity;
            mPaint = new Paint(bitmapState.mPaint);
            mRebuildShader = bitmapState.mRebuildShader;
        }

        @Override
        public Drawable newDrawable() {
            return new AsyBitmapDrawable(this, null, builder);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new AsyBitmapDrawable(this, res, builder);
        }

        @Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }
    }

    private AsyBitmapDrawable(BitmapState state, Resources res, Builder builder) {
        if (builder != null){
            this.url = builder.url;
            this.name = builder.name;
            this.placeDelay = builder.placeDelay;
            builder.setBounds(this);
        }
        this.builder = builder;
        mBitmapState = state;
        if (res != null) {
            mTargetDensity = res.getDisplayMetrics().densityDpi;
        } else {
            mTargetDensity = state.mTargetDensity;
        }
        setBitmap(state != null ? state.mBitmap : null);
        mBitmapState.mTargetDensity = mTargetDensity;
    }

    /************************************************************************************************
     *
     * local
     *
     ***********************************************************************************************/
    @Override
    public int getIntrinsicWidth() {
        Rect bounds = getBounds();
        if (bounds != null && bounds.width() > 0)
            return bounds.width();
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        Rect bounds = getBounds();
        if (bounds != null && bounds.height() > 0)
            return bounds.height();
        return mBitmapHeight;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        log("bb_ss", "setBounds,left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom);
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);
        log("bb_ss", "setBounds,bounds=" + bounds);
    }

    private void init(Context context, ICreator creator) {

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

        Observable.create(new ObservableOnSubscribe<DrawableWrapper>() {

            @Override
            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<DrawableWrapper> emitter) {
                if (creator != null){
                    creator.create(new CallBack<DrawableWrapper>(){
                        @Override
                        public void onSuccess(DrawableWrapper obj) {
                            super.onSuccess(obj);
                            if (obj == null)
                                obj = new DrawableWrapper();

                            emitter.onNext(obj);
                        }
                    }, AsyBitmapDrawable.this);
                } else {

                    Drawable drawable = null;

                    if (!TextUtils.isEmpty(url))
                        drawable = ImageHelper.getDrawable(context, url);

                    // android.R.drawable.ic_delete
//                if (drawable == null)
//                    drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.alpha, null);

                    emitter.onNext(new DrawableWrapper(drawable));
                }
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DrawableWrapper>() {
                    @Override
                    public void accept(DrawableWrapper drawableWrapper) throws Exception {

                        isLoadCompleted = true;

                        checkCallback();

                        Drawable drawable = drawableWrapper.getDrawable();

                        log("no_shape", "accept,callback=" + getCallback() + ",drawable=" + drawable);

                        if (drawable != null) {

                            if (mRunnable != null){
                                HandleManager.getInstance().getHandler().removeCallbacks(mRunnable);
                                mRunnable = null;
                            }

                            Rect rect = getBounds();
                            int intrinsicWidth = drawable.getIntrinsicWidth();
                            int intrinsicHeight = drawable.getIntrinsicHeight();
                            float factor = (float) rect.height() / intrinsicHeight;
                            setBounds(0, 0, Float.valueOf(intrinsicWidth * factor).intValue(), rect.height());

                            log("no_shape", "accept, " + AsyBitmapDrawable.this
                                    + ",intrinsicWidth=" + intrinsicWidth
                                    + ",intrinsicHeight=" + intrinsicHeight
                                    + ",factor=" + factor);

                            setBitmap(drawable);

                            iteratorView(viewSet, new CallBack<View>(){
                                @Override
                                public void onSuccess(View obj) {
                                    super.onSuccess(obj);
                                    obj.invalidate();
                                }
                            });
                        }

                        Set<WeakReference<View>> views = new HashSet<>(viewSet);
                        iteratorView(views, new CallBack<View>(){
                            @Override
                            public void onSuccess(View v) {
                                super.onSuccess(v);
                                if (v instanceof IAsyBitmapView) {
                                    ((IAsyBitmapView) v).onDrawableLoadCompleted(AsyBitmapDrawable.this);
                                }
                            }
                        });

                        synchronized (AsyBitmapDrawable.this){

                            if (ObjectUtils.isNotEmpty(loadCallbacks)){
                                for (ICallBack<Drawable> loadCallback : loadCallbacks) {
                                    if (loadCallback != null) {
                                        if (drawable != null)
                                            loadCallback.onSuccess(AsyBitmapDrawable.this);
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

//    @Override
//    public boolean checkView() {
//        return isAllViewLegal(viewSet);
//    }

    @Override
    public boolean isLoadCompleted() {
        return isLoadCompleted;
    }

    @Override
    public Drawable getTarget() {
        return this;
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

    @Override
    public void attach(@NonNull View view) {
        addIfLackView(viewSet, view);
        checkCallback();
        log("_asy_draw","attach: view=" + view + ",viewSet.size=" + checkSize(viewSet) + ",callback=" + getCallback());
    }

    @Override
    public void detach(@NonNull View view) {
        removeIfContainsView(viewSet, view);
        log("_asy_draw","detach: view=" + view + ",viewSet.size=" + checkSize(viewSet));
    }

    private void checkCallback(){
        if (!(getCallback() instanceof CallbackView)) {
            setCallback(new CallbackView(viewSet));
        }
    }

    @Override
    public boolean isStateful() {
        Callback c = getCallback();
        log("callback_isStateful", "isStateful execute from " + this.getClass().getSimpleName()
                + ",callback=" + c);
        if (c != null && !(c instanceof CallbackView) && !(c instanceof IAsyBitmapView))
            throw new IllegalStateException("you must set " + this.getClass().getSimpleName() + " in a " + RichTextView.class.getSimpleName());
        return super.isStateful();
    }

    public long getLoadTime() {
        return loadTime;
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

    /**
     * 用于回调结果
     */
    public static class DrawableWrapper{
        private Drawable drawable;

        private DrawableWrapper(){

        }

        public DrawableWrapper(Drawable drawable) {
            this.drawable = drawable;
        }

        public Drawable getDrawable() {
            return drawable;
        }
    }
}
