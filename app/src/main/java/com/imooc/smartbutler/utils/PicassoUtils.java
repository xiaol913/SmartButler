package com.imooc.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.utils
 * 文件名：  PicassoUtils
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/3122:59
 * 描述：    Picasso封装
 */

public class PicassoUtils {
    /**
     * 默认加载图片
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadImageView(Context mContext, String url, ImageView imageView) {
        Picasso.with(mContext).load(url).into(imageView);
    }

    /**
     * 默认加载图片 指定大小
     *
     * @param mContext  上下文
     * @param url       url地址
     * @param imageView ImageView对象
     * @param width     宽
     * @param height    高
     */
    public static void loadImageView(Context mContext, String url, ImageView imageView, int width, int height) {
        Picasso.with(mContext).load(url).resize(width, height).centerCrop().into(imageView);
    }

    /**
     * 加载默认图片
     *
     * @param mContext
     * @param url
     * @param imageView
     * @param imageId
     */
    public static void loadImageView(Context mContext, String url, ImageView imageView, int imageId) {
        Picasso.with(mContext).load(url).placeholder(imageId).error(imageId).into(imageView);
    }

    /**
     * 裁剪图片的方法
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadImageViewCrop(Context mContext, String url, ImageView imageView) {
        Picasso.with(mContext).load(url).transform(new CropSquareTransformation()).into(imageView);
    }

    // 按比例裁剪
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "square()";
        }
    }
}
