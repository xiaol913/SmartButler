package com.imooc.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.smartbutler.MainActivity;
import com.imooc.smartbutler.R;
import com.imooc.smartbutler.entity.MyUser;
import com.imooc.smartbutler.utils.ShareUtils;
import com.imooc.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  LoginActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2818:23
 * 描述：    登录页
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox keep_password;
    private EditText et_name, et_password;
    //登录按钮
    private Button btn_login;
    //注册按钮
    private Button btn_registered;
    //忘记密码
    private TextView tv_forget;
    //Dialog
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        keep_password = (CheckBox) findViewById(R.id.keep_password);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
        //初始化Dialog
        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏外点击无效
        dialog.setCancelable(false);
        //设置选中的状态
        boolean isCheck = ShareUtils.getBoolean(this, "keepPass", false);
        keep_password.setChecked(isCheck);
        if (isCheck) {
            //设置密码
            et_name.setText(ShareUtils.getString(this, "name", ""));
            et_password.setText(ShareUtils.getString(this, "password", ""));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_registered:
                startActivity(new Intent(this, RegisteredActivity.class));
                break;
            case R.id.btn_login:
                //1.获取输入框的值
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //2.判断是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    dialog.show();
                    //登录
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            dialog.dismiss();
                            //判断结果
                            if (e == null) {
                                //判断邮箱是否验证
                                if (user.getEmailVerified()) {
                                    //跳转
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.to_email_verify, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.login_fail) + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, R.string.txt_cannot_null, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }

    //假设我输入用户名密码，但是我不点击登录，而是直接退出了
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this, "keepPass", keep_password.isChecked());
        //是否记住密码
        if (keep_password.isChecked()) {
            //记住用户名和密码
            ShareUtils.putString(this, "name", et_name.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        } else {
            ShareUtils.deleShare(this, "name");
            ShareUtils.deleShare(this, "password");
        }
    }
}
