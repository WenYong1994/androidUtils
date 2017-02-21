package com.yuyh.library.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yuyh.library.R;


/**
 * Created by arvin on 2016/5/24
 */
public class GlideUtils {
    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).placeholder(R.drawable.img_loading).error(R.drawable.img_loading).into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).placeholder(R.drawable.img_default_avatar).error(R.drawable.img_default_avatar).
                transform(new GlideCircleTransform(context)).into(imageView);
    }

    /**
     * 加载为圆形缓存图片（一般为头像加载）
     */
    public static void loadCircleCacheImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.img_default_avatar).error(R.drawable.img_default_avatar).
                transform(new GlideCircleTransform(context)).into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadCircleImage(Context context, Uri url, ImageView imageView) {
        Glide.with(context).load(url).placeholder(R.drawable.img_default_avatar).error(R.drawable.img_default_avatar).
                transform(new GlideCircleTransform(context)).into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadCircleImageFromWH(Context context, String url, ImageView imageView, int width, int height) {
        Glide.with(context).load(url).placeholder(R.drawable.img_default_avatar).override(width, height).error(R.drawable.img_default_avatar).
                transform(new GlideCircleTransform(context)).into(imageView);
    }

    /**
     * 加载本地图片（资源文件）
     */
    public static void loadLocalImage(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId).override(128, 128).into(imageView);
    }
}
