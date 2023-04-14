package com.example.lottiedemo.rich.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.lottiedemo.GlideApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

public class ImageHelper {

    private static final String TAG = ImageHelper.class.getSimpleName();

    /**
     * 清除 Cancel any pending loads Glide may have for the view and free any resources that may have been
     * * loaded for the view.
     *
     * @param activity
     * @param view
     */
    public static void clear(Activity activity, View view) {
        try {
            Glide.with(activity).clear(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Activity context, String urlString, ImageView imageView, int defaultRes) {
        try {
            buildLoadImage(GlideApp.with(context), urlString, imageView, defaultRes, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Activity context, String urlString, ImageView imageView) {
        try {
            buildLoadImage(GlideApp.with(context), urlString, imageView, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, String urlString, ImageView imageView) {
        try {
            buildLoadImage(GlideApp.with(context), urlString, imageView, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, byte[] bytes, ImageView imageView) {
        try {
            buildLoadImage(GlideApp.with(context), bytes, imageView, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Fragment context, String urlString, ImageView imageView, int defaultRes) {
        try {
            buildLoadImage(GlideApp.with(context), urlString, imageView, defaultRes, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, String urlString, ImageView imageView, int defaultRes) {
        try {
            buildLoadImage(GlideApp.with(context), urlString, imageView, defaultRes, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageAsBitmap(Context context, String urlString, ImageView imageView, int defaultRes) {
        try {
            buildLoadImageAsBitmap(GlideApp.with(context), urlString, imageView, defaultRes, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, String urlString, ImageView imageView, int width, int height) {
        try {
            buildLoadImage(GlideApp.with(context), urlString, imageView, 0, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, String urlString, ImageView imageView, int defaultRes, int width, int height) {
        try {
            buildLoadImage(GlideApp.with(context), urlString, imageView, defaultRes, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // --------------------------------------

    // load file----------------------------
    public static void loadImage(Context context, File file, ImageView imageView, int defaultRes) {
        try {
            buildLoadImage(GlideApp.with(context), file, imageView, defaultRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Activity context, File file, ImageView imageView, int defaultRes) {
        try {
            buildLoadImage(GlideApp.with(context), file, imageView, defaultRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Fragment context, File file, ImageView imageView, int defaultRes) {
        try {
            buildLoadImage(GlideApp.with(context), file, imageView, defaultRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buildLoadImage(RequestManager requestManager, File file, ImageView imageView, int defaultRes) {
        try {
            buildLoadImage(requestManager, file, imageView, defaultRes, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------
    private static void buildLoadImage(RequestManager requestManager
            , String url, ImageView imageView, int defaultRes, int width, int height, Transformation... transformations) {
        try {
            requestManager.load(url)
                    .apply(defaultOptions(defaultRes, width, height, transformations))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buildLoadImageAsBitmap(RequestManager requestManager
            , String url, ImageView imageView, int defaultRes, int width, int height, Transformation... transformations) {
        try {
            requestManager
                    .asBitmap()
                    .load(url)
                    .apply(defaultOptions(defaultRes, width, height, transformations))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buildLoadImage(RequestManager requestManager
            , File file, ImageView imageView, int defaultRes, int width, int height, Transformation... transformations) {
        try {
            requestManager
                    .load(file)
                    .apply(defaultOptions(defaultRes, width, height, transformations))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buildLoadImage(RequestManager requestManager
            , byte[] bytes, ImageView imageView, int defaultRes, int width, int height, Transformation... transformations) {
        try {
            requestManager.load(bytes)
                    .apply(defaultOptions(defaultRes, width, height, transformations))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static RequestOptions defaultOptions(int defaultRes, int width, int height, Transformation... transformations) {
        try {
            RequestOptions options = new RequestOptions();
//            options.dontAnimate();
            options.disallowHardwareConfig();
            if (defaultRes != 0) {
                options.placeholder(defaultRes);
                options.error(defaultRes);
            }
            if (width != 0
                    && height != 0) {
                options.override(width, height);
            }
            if (transformations != null && transformations.length > 0) {
                options.transform(new MultiTransformation(transformations));
            }
            return options;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadImageV2SimpleTarget(Context context, final String urlString, final ImageView imageView, int defaultRes) {
        final String url = urlString;
        if (TextUtils.isEmpty(url)
                || imageView == null) {
            return;
        }

        if (defaultRes != 0) {
            imageView.setImageResource(defaultRes);
        }
        imageView.setTag(url);

        GlideApp.with(context)
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Object tag = imageView.getTag();
                        if (tag != null && url.equals(tag)) {
                            imageView.setImageDrawable(resource);
                            if (transition != null)
                                transition.transition(resource, new Transition.ViewAdapter() {
                                    @Override
                                    public View getView() {
                                        return imageView;
                                    }

                                    @Override
                                    public Drawable getCurrentDrawable() {
                                        return imageView.getDrawable();
                                    }

                                    @Override
                                    public void setDrawable(Drawable drawable) {
                                        imageView.setImageDrawable(drawable);
                                    }
                                });
                        }
                    }
                });
    }

    public static void loadImageToTextView(Context context, final String urlString, final TextView textView, int defaultRes) {
        final String url = urlString;
        if (context == null || TextUtils.isEmpty(url) || textView == null) {
            return;
        }
        if (defaultRes != 0) {
            textView.setBackgroundResource(defaultRes);
        }
        textView.setTag(url);
        GlideApp.with(context)
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Object tag = textView.getTag();
                        if (url.equals(tag)) {
                            textView.setBackground(resource);
                        }
                    }
                });
    }

    public static void asynRequestDrawable(Context context, final String urlString, LoadImageListener listener) {
        final String url = urlString;
        if (context == null || TextUtils.isEmpty(url)) {
            return;
        }
        final LoadImageListener loadImageListener = listener;
        GlideApp.with(context)
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (loadImageListener != null) {
                            loadImageListener.onResourceReady(url, resource, transition);
                        }
                    }
                });
    }

    public static void loadImage(Context context, final String urlString, int defaultRes, final ImageLoadingListener listener) {
        try {
            String url = urlString;
            RequestOptions options = new RequestOptions()
                    .error(defaultRes);
            GlideApp.with(context).asBitmap().load(url).apply(options).into(new ListenerSimpleTarget(url, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ---------------------加载bitmap start---------------------------
    public static void loadBitmap(Context context, final String urlString, final ImageLoadingListener listener) {
        try {
            String url = urlString;
            GlideApp.with(context).asBitmap().load(url).into(new ListenerSimpleTarget(url, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBitmap(Context context, final String urlString, int width, int height, final ImageLoadingListener listener) {
        try {
            String url = urlString;
            GlideApp.with(context).asBitmap().centerCrop().override(width, height).load(url).into(new ListenerSimpleTarget(url, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBitmap(Activity context, final String urlString, final ImageLoadingListener listener) {
        try {
            String url = urlString;
            GlideApp.with(context).asBitmap().load(url).into(new ListenerSimpleTarget(url, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBitmap(Fragment context, final String urlString, final ImageLoadingListener listener) {
        try {
            String url = urlString;
            GlideApp.with(context).asBitmap().load(url).into(new ListenerSimpleTarget(url, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadOnly(Context context, final String urlString, final ImageLoadingListener listener, boolean bitmapFlag) {
        //在子线程中调用
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalArgumentException("You can't call this method on the main thread");
        }

        final String url = urlString;
        File file = null;
        try {
            file = GlideApp.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (listener != null) {
                if (file != null)
                    listener.onLoadingComplete(file.getAbsolutePath());
                Bitmap bmp = null;
                if (bitmapFlag && file != null) {
                    try {
                        FileInputStream is = new FileInputStream(file);
                        bmp = BitmapFactory.decodeStream(is);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                listener.onLoadingComplete(url, null, bmp);
            }
        }
    }
    // ---------------------加载bitmap end---------------------------

    public static void loadImageNotCache(Context context, final String urlString, final ImageView imageView, int defaultRes, final ImageLoadingListener listener) {
        try {
            final String url = urlString;
            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(defaultRes);
            GlideApp.with(context).asBitmap().load(url).apply(options).into(new ListenerViewTarget(url, imageView, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageSkipMemoryCache(Context context, final String urlString, final ImageView imageView, int defaultRes, final ImageLoadingListener listener) {
        try {
            final String url = urlString;
            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .error(defaultRes);
            GlideApp.with(context).asBitmap().load(url).apply(options).into(new ListenerViewTarget(url, imageView, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageNotCache(Context context, final String urlString, final ImageView imageView, int defaultRes) {
        try {
            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(defaultRes);
            GlideApp.with(context).load(urlString).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageNotCache(Activity context, final String urlString, final ImageView imageView, int defaultRes) {
        try {
            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(defaultRes);
            GlideApp.with(context).load(urlString).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageNotCache(Fragment context, final String urlString, final ImageView imageView, int defaultRes) {
        try {
            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(defaultRes);
            GlideApp.with(context).load(urlString).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageSkipMemoryCache(Context context, final String urlString, final ImageView imageView, int defaultRes) {
        try {
            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .error(defaultRes);
            GlideApp.with(context).load(urlString).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载圆形图片
     *
     * @param urlString
     * @param imageView
     * @param defaultRes
     */
    public static void loadCircleImage(Context context, String urlString, final ImageView imageView, final int defaultRes) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .placeholder(defaultRes)
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCircleImage(Context context, String urlString, final ImageView imageView, final int defaultRes, int width, int heigh) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .override(width, heigh)
                    .placeholder(defaultRes)
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆形图片
     *
     * @param urlString
     * @param imageView
     * @param defaultRes
     */
    public static void loadCircleImage(Context context, String urlString, final ImageView imageView, final int defaultRes, boolean isCacheAll) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .diskCacheStrategy(isCacheAll ? DiskCacheStrategy.ALL : DiskCacheStrategy.AUTOMATIC)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载到监听
     *
     * @param context
     * @param urlString
     * @param listener
     * @param defaultRes
     */
    public static void loadCircleImage(Context context, final String urlString, final ImageLoadingListener listener, final int defaultRes) {
        try {
            final String url = urlString;
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .error(defaultRes);
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(options)
                    .into(new ListenerSimpleTarget(url, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCircleImage(Activity context, String urlString, final ImageView imageView, final int defaultRes) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCircleImage(Activity context, String urlString, final ImageView imageView, final int defaultRes, final int holderRes) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .placeholder(holderRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCircleImage(Context context, String urlString, final ImageView imageView, final int defaultRes, final int holderRes) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .placeholder(holderRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCircleImage(Activity context, String urlString, final ImageView imageView, final int defaultRes, final int holderRes, final ImageLoadingListener listener) {

        try {
            final String url = urlString;
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .placeholder(holderRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(new ListenerViewTarget(url, imageView, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCircleImageAsBitmap(Context context, String urlString, final int defaultRes, final int holderRes, final ImageLoadingListener listener) {

        try {
            final String url = urlString;
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .placeholder(holderRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(new ListenerSimpleTarget(url, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCircleImage(Fragment context, String urlString, final ImageView imageView, final int defaultRes) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new CircleCrop())
                    .placeholder(defaultRes)
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage(Context context, String urlString, final ImageView imageView, final int defaultRes, final int radius) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new RoundedCorners(radius)))//Glide 4.7 RoundedCorners与CenterCrop冲突.这样调用才行
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadRoundImageAsBitmap(Context context, String urlString, final int defaultRes, final int radius, ImageLoadingListener listener) {

        try {
            final String url = urlString;
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new RoundedCorners(radius)))//Glide 4.7 RoundedCorners与CenterCrop冲突.这样调用才行
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(options)
                    .into(new ListenerSimpleTarget(url, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadRoundImage(Context context, String urlString, final ImageView imageView, final int defaultRes, final int radius, int widht, int heigh) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new RoundedCorners(radius)))//Glide 4.7 RoundedCorners与CenterCrop冲突.这样调用才行
                    .override(widht, heigh)
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage4CenterCrop(Context context, Uri uri, final ImageView imageView, final int defaultRes, final int radius) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(radius)))//Glide 4.7 RoundedCorners与CenterCrop冲突.这样调用才行
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(uri)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage4CenterCrop(Context context, String urlString, final ImageView imageView, final int defaultRes, final int radius) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(radius)))//Glide 4.7 RoundedCorners与CenterCrop冲突.这样调用才行
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage4Location(Context context, String path, final ImageView imageView, final int defaultRes, final int radius) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(radius)))//Glide 4.7 RoundedCorners与CenterCrop冲突.这样调用才行
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(Uri.fromFile(new File(path)))
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage4ResId(Context context, int resId, final ImageView imageView, final int defaultRes, final int radius) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(radius)))//Glide 4.7 RoundedCorners与CenterCrop冲突.这样调用才行
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(resId)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadRoundImage4CenterCrop(Activity context, String urlString, final ImageView imageView, final int defaultRes, final int radius) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(radius)))
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadGranularRoundImage(Context context, Bitmap bitmap, final ImageView imageView, final int defaultRes, float topLeft, float topRight, float bottomRight, float bottomLeft) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new GranularRoundedCorners(topLeft, topRight, bottomRight, bottomLeft)))
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(bitmap)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadRoundImage4CenterCrop(Fragment context, String urlString, final ImageView imageView, final int defaultRes, final int radius) {

        try {
            RequestOptions options = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(radius)))
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(urlString)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearMemory(Context context) {
        try {
            if (context == null)
                return;
            Glide.get(context).clearMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void clearDiskCache(Context context) {
        try {
            if (context == null)
                return;
            Glide.get(context).clearDiskCache();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Drawable getDrawableFromCache(Context context, String imgUrl){
        try {
            return GlideApp.with(context)
                    .asDrawable()
                    .onlyRetrieveFromCache(true)
                    .load(imgUrl)
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getDrawable(Context context, String imgUrl) {
        try {
            return GlideApp.with(context).asDrawable().load(imgUrl).submit().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getDrawable(Context context, File file, int size, int defaultResId) {
        try {
            RequestOptions options = new RequestOptions().error(defaultResId);
            return GlideApp.with(context).asDrawable().apply(options).load(file).into(size, size).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getDrawable(Context context, String imgUrl, int size, int defaultResId) {
        return getDrawable(context, imgUrl, size, size, defaultResId);
    }

    public static Drawable getDrawable(Context context, String imgUrl, int width, int height, int defaultResId) {
        try {
            RequestOptions options = new RequestOptions().error(defaultResId);
            return GlideApp.with(context).asDrawable().apply(options).load(imgUrl).into(width, height).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, String imgUrl, int sizeW, int sizeH) {
        try {
            return GlideApp.with(context).asBitmap().load(imgUrl).into(sizeW, sizeH).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] getBitmapByte(Context context, String imgUrl, int sizeW, int sizeH) {
        try {
            return GlideApp.with(context).as(byte[].class).load(imgUrl).into(sizeW, sizeH).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static Bitmap decodeSampledBitmapFromResource2Scale(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inDensity = 320;
        //输出图片的宽高= 原图片的宽高 / inSampleSize * ( inTargetDensity / inDensity)
        options.inTargetDensity = reqWidth * options.inSampleSize * options.inDensity / options.outWidth;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromResource2(Resources res, int resId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // 长图，宽图比例阈值
    public static final int RATIO_OF_LARGE = 3;
    // 长图截取后的高宽比（宽图截取后的宽高比）
    public static int HW_RATIO = 3;


    public interface LoadImageListener {
        void onResourceReady(String url, Drawable resource, Transition<? super Drawable> transition);
    }

    private static class ListenerViewTarget extends ImageViewTarget<Bitmap> {


        private ImageLoadingListener loadingListener;
        private String url;

        public ListenerViewTarget(String url, ImageView view, ImageLoadingListener loadingListener) {
            super(view);
            this.url = url;
            this.loadingListener = loadingListener;
        }

        @Override
        protected void setResource(Bitmap resource) {
            getView().setImageBitmap(resource);
        }

        @Override
        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> glideAnimation) {
            super.onResourceReady(resource, glideAnimation);

            if (loadingListener != null)
                loadingListener.onLoadingComplete(url, getView(), resource);

        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            super.onLoadFailed(errorDrawable);
            if (loadingListener != null)
                loadingListener.onLoadingComplete(url, getView(), null);
        }
    }

    private static class ListenerSimpleTarget extends CustomTarget<Bitmap> {

        private ImageLoadingListener loadingListener;
        private String url;

        public ListenerSimpleTarget(String url, ImageLoadingListener loadingListener) {
            this.url = url;
            this.loadingListener = loadingListener;
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            super.onLoadFailed(errorDrawable);
            if (loadingListener != null)
                loadingListener.onLoadingComplete(url, null, null);
        }

        @Override
        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> glideAnimation) {

            // 由于bitmap很可能被引用然后继续使用，但是如果这时被glide回收掉的话会报错
            // 这里复制一份返回，以保证独立的实例
            if (loadingListener != null) {
                try {
                    loadingListener.onLoadingComplete(url, null
                            , resource.copy(resource.getConfig(), true));
                } catch (Exception e) {
                    e.printStackTrace();
                    loadingListener.onException(e, "");
                }
            }

        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {

        }
    }

    public static class ImageLoadingListener implements RequestListener<BitmapDrawable> {
        public boolean onException(Exception e, String model) {
            return false;
        }

        public boolean onResourceReady(BitmapDrawable resource, String model) {
            return false;
        }

        public void onLoadingComplete(String var1, View var2, Bitmap var3) {

        }

        public void onLoadingComplete(String filePath) {

        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<BitmapDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(BitmapDrawable resource, Object model, Target<BitmapDrawable> target, DataSource dataSource, boolean isFirstResource) {
            return onResourceReady(resource, (String) model);
        }
    }


    //获取视频截图
    @SuppressLint("CheckResult")
    public static void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros, int defaultRes) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(VideoDecoder.FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }

    //获取视频截图
    @SuppressLint("CheckResult")
    public static void loadRoundVideoScreenShot(final Context context, String uri, ImageView imageView, long frameTimeMicros, int defaultRes, int radius) {
        RequestOptions options = new RequestOptions()
                .frameOf(frameTimeMicros)
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(radius)))//Glide 4.7 RoundedCorners与CenterCrop冲突.这样调用才行
                .placeholder(defaultRes)
                .dontAnimate()
                .error(defaultRes);
        options.set(VideoDecoder.FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        Glide.with(context).load(uri).apply(options).into(imageView);
    }

    public static void loadImage(Context context, Uri uri, final ImageView imageView, final int defaultRes) {

        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(defaultRes)
                    .dontAnimate()
                    .error(defaultRes);
            GlideApp.with(context)
                    .load(uri)
                    .apply(options)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Activity context, String url, final ImageView imageView, final RequestListener<Drawable> listener) {

        try {
            GlideApp.with(context)
                    .load(url)
                    .listener(listener)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
