package com.imooc.smartbutler.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.smartbutler.R;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.utils
 * 文件名：  UtilTools
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2817:53
 * 描述：    工具统一类
 */

public class UtilTools {

    //设置字体
    public static void setFont(Context context, TextView textView) {
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "fonts/FONT.TTF");
        textView.setTypeface(fontFace);
    }

    //设置用户头像
    public static void setUserFace(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            PicassoUtils.loadImageView(context, url, imageView);
        } else {
            imageView.setImageResource(R.drawable.add_pic);
        }
    }

    //删除已上传的文件
    public static void delFileWithUrl(String url) {
        BmobFile file = new BmobFile();
        file.setUrl(url);
        file.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    L.e(String.valueOf(R.string.replace_completed));
                } else {
                    L.e(String.valueOf(R.string.replace_fail));
                }
            }
        });
    }

    //获取版本号
    public static String getVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return context.getString(R.string.txt_unknow);
        }
    }
}
