package com.imooc.smartbutler.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.service.SmsService;
import com.imooc.smartbutler.utils.L;
import com.imooc.smartbutler.utils.ShareUtils;
import com.imooc.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  SettingActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2818:23
 * 描述：    设置页
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //语音播报
    private Switch sw_speak;
    //短信提醒
    private Switch sw_sms;
    //版本更新
    private LinearLayout ll_update;
    private TextView tv_version;

    private String versionName;
    private int versionCode;

    private String url;
    //扫一扫
    private LinearLayout ll_scan;
    //扫描结果
    private TextView tv_scan_result;
    //生成二维码
    private LinearLayout ll_qr_code;
    //我的位置
    private LinearLayout ll_my_location;
    //关于软件
    private LinearLayout ll_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {

        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);

        boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);

        sw_sms = (Switch) findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);

        boolean isSms = ShareUtils.getBoolean(this, "isSms", false);
        sw_sms.setChecked(isSms);

        ll_update = (LinearLayout) findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);

        tv_version = (TextView) findViewById(R.id.tv_version);

        ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);

        tv_scan_result = (TextView) findViewById(R.id.tv_scan_result);

        ll_qr_code = (LinearLayout) findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);

        ll_my_location = (LinearLayout) findViewById(R.id.ll_my_location);
        ll_my_location.setOnClickListener(this);

        ll_about = (LinearLayout) findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);

        try {
            getVersionNameCode();
            tv_version.setText(getString(R.string.txt_check_version) + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText(R.string.txt_check_version);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sw_speak:
                //切换
                sw_speak.setSelected(!sw_speak.isSelected());
                //保存状态
                ShareUtils.putBoolean(this, "isSpeak", sw_speak.isChecked());
                break;
            case R.id.sw_sms:
                //切换相反
                sw_sms.setSelected(!sw_sms.isSelected());
                ShareUtils.putBoolean(this, "isSms", sw_sms.isChecked());
                if (sw_sms.isChecked()) {
                    startService(new Intent(this, SmsService.class));
                } else {
                    stopService(new Intent(this, SmsService.class));
                }
                break;
            case R.id.ll_update:
                /**
                 * 步骤
                 * 1.请求服务器的配置文件，拿到CODE
                 * 2.比较
                 * 3.diaLog提示
                 * 4.跳转到更新界面，并且把url传递过去
                 */
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i("config:" + t);
                        parsingJson(t);
                    }
                });
                break;
            case R.id.ll_scan:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(this, QrCodeActivity.class));
                break;
            case R.id.ll_my_location:
                startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            if (code > versionCode) {
                showUpdateDialog(jsonObject.getString("content"));
            } else {
                Toast.makeText(this, R.string.the_newest, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.need_update)
                .setMessage(content)
                .setPositiveButton(R.string.txt_update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SettingActivity.this, UpdateActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //什么都不做，也会执行dismiss方法
                    }
                })
                .show();
    }

    //获取版本号和Code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_scan_result.setText(scanResult);
        }
    }
}
