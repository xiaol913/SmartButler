package com.imooc.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.utils.ShareUtils;
import com.imooc.smartbutler.utils.StaticClass;
import com.imooc.smartbutler.utils.UtilTools;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  SplashActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/293:33
 * 描述：    闪屏页
 */

public class SplashActivity extends AppCompatActivity {

    /**
     * 1.延迟2000ms
     * 2.判断程序是否第一次运行
     * 3.自定义字体
     * 4.Activity全屏主题
     *
     * @param savedInstanceState
     */
    private TextView tv_splash;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否第一次运行
                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    private void initView() {
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        tv_splash = (TextView) findViewById(R.id.tv_splash);
        //设置字体
        UtilTools.setFont(this, tv_splash);
    }

    //判断程序是否第一次运行
    public boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        if (isFirst) {
            ShareUtils.putBoolean(this, StaticClass.SHARE_IS_FIRST, false);
            //是第一次运行
            return true;
        } else {
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
