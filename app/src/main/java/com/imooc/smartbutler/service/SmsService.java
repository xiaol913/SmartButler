package com.imooc.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.utils.L;
import com.imooc.smartbutler.utils.StaticClass;
import com.imooc.smartbutler.view.DispatchLinearLayout;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.service
 * 文件名：  SmsService
 * 创建者：  Shawn Gao
 * 创建时间：2017/2/12:51
 * 描述：    短信监听服务
 */

public class SmsService extends Service implements View.OnClickListener {
    private SmsReceiver smsReceiver;
    //发件人号码
    private String smsPhone;
    //短信内容
    private String smsContent;
    //窗口管理器
    private WindowManager wm;
    //布局参数
    private WindowManager.LayoutParams layoutParam;
    //View
    private DispatchLinearLayout mView;

    private TextView tv_phone, tv_content;
    private Button btn_send_sms, btn_close;

    private HomeWatchReceiver mHomeWatchReceiver;

    public static final String SYSTEM_DIALOGS_RESON_KEY = "reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY = "homekey";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    //初始化
    private void init() {
        L.i("init service");
        //动态注册
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //添加Action
        intentFilter.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver, intentFilter);

        mHomeWatchReceiver = new HomeWatchReceiver();
        IntentFilter intentHome = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeWatchReceiver, intentHome);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");
        //注销
        unregisterReceiver(smsReceiver);
        unregisterReceiver(mHomeWatchReceiver);
    }

    //短信广播
    public class SmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StaticClass.SMS_ACTION.equals(action)) {
                L.i(getString(R.string.txt_sms_comes));
                //获取短信内容，返回的是一个Object数组
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for (Object obj : objs) {
                    //把数组元素转化成短信对象
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                    //发件人
                    smsPhone = smsMessage.getOriginatingAddress();
                    //内容
                    smsContent = smsMessage.getMessageBody();
                    L.i(getString(R.string.txt_sms_content) + smsPhone + ":" + smsContent);
                    showWindow();
                }
            }
        }
    }

    //窗口提示
    private void showWindow() {
        //获取系统服务
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        layoutParam = new WindowManager.LayoutParams();
        //定义宽高
        layoutParam.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParam.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        layoutParam.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        layoutParam.format = PixelFormat.TRANSLUCENT;
        //定义类型
        layoutParam.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);

        tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
        tv_content = (TextView) mView.findViewById(R.id.tv_content);
        //设置数据
        tv_phone.setText(getString(R.string.txt_sms_send_by) + smsPhone);
        tv_content.setText(smsContent);

        btn_send_sms = (Button) mView.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(this);
        btn_close = (Button) mView.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        //添加View到窗口
        wm.addView(mView, layoutParam);

        mView.setDispatchKeyEventListener(mDispatchKeyEventListener);
    }

    private DispatchLinearLayout.DispatchKeyEventListener mDispatchKeyEventListener = new DispatchLinearLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否是按返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                L.i(getString(R.string.txt_press_back));
                if (mView.getParent() != null) {
                    wm.removeView(mView);
                }
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_sms:
                sendSms();
                //消失窗口
                if (mView.getParent() != null) {
                    wm.removeView(mView);
                }
                break;
            case R.id.btn_close:
                //消失窗口
                if (mView.getParent() != null) {
                    wm.removeView(mView);
                }
                break;
        }
    }

    //回复短信
    private void sendSms() {
        Uri uri = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    //监听Home键的广播
    class HomeWatchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_RESON_KEY);
                if (SYSTEM_DIALOGS_HOME_KEY.equals(reason)) {
                    L.i(getString(R.string.txt_press_home));
                    if (mView.getParent() != null) {
                        wm.removeView(mView);
                    }
                }
            }
        }
    }
}
