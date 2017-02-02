package com.imooc.smartbutler.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.imooc.smartbutler.R;

import java.util.List;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.view
 * 文件名：  CustomDialog
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/301:56
 * 描述：    自定义Dialog
 */

public class CustomDialog extends Dialog{
    //定义模板
    public CustomDialog(Context context,int layout, int style){
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,layout,style, Gravity.CENTER);
    }
    //定义属性
    public CustomDialog(Context context,int width, int height,int layout, int style, int gravity, int anim){
        super(context,style);
        //设置属性
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(anim);
    }
    //实例
    public CustomDialog(Context context,int width, int height,int layout, int style, int gravity){
        this(context,width,height,layout,style,gravity,R.style.pop_anim_style);

    }
}
