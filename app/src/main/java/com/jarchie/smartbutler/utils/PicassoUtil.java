package com.jarchie.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 项目名:   SmartButler
 * 包名:     com.jarchie.smartbutler.utils
 * 文件名:   PicassoUtil
 * 创建者:   Jarchie
 * 创建时间: 17/1/23 下午12:40
 * 描述:     Picasso加载图片的工具类
 */

public class PicassoUtil {
    //默认加载图片
    public static void loadImageView(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(url).into(imageView);
    }

    //加载指定大小的图片
    public static void loadImageViewSize(Context context, String url, ImageView imageView, int width, int height) {
        Picasso.with(context)
                .load(url)
                .resize(width, height)
                .centerCrop()
                .into(imageView);
    }

    //加载有默认图片的图片
    public static void loadImageDefault(Context context, String url, ImageView imageView, int defaultImg, int errorImg) {
        Picasso.with(context)
                .load(url)
                .placeholder(defaultImg)
                .error(errorImg)
                .into(imageView);
    }

    //裁剪图片
    public static void loadImageViewCrop(Context context, String url, ImageView imageView){
        Picasso.with(context)
                .load(url)
                .transform(new CropSquareTransformation())
                .into(imageView);
    }

    //按比例裁剪矩形(官方文档给出的裁剪方法)
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                //回收
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "archie";
        }
    }

}
