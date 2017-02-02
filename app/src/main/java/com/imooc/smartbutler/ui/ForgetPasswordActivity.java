package com.imooc.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.entity.MyUser;
import com.imooc.smartbutler.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  ForgetPasswordActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/300:59
 * 描述：    忘记密码
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_forget_password;
    private EditText et_email;
    private EditText et_now;
    private EditText et_new;
    private EditText et_new_password;
    private Button btn_update_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initView();
    }

    private void initView() {
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(this);
        et_email = (EditText) findViewById(R.id.et_email);
        et_now = (EditText) findViewById(R.id.et_now);
        et_new = (EditText) findViewById(R.id.et_new);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);
        btn_update_password.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forget_password:
                //1.获取输入框内的邮箱
                final String email = et_email.getText().toString().trim();
                //2.判断是否为空
                if (!TextUtils.isEmpty(email)) {
                    //3.发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                L.e("email" + e);
                                Toast.makeText(ForgetPasswordActivity.this, getString(R.string.email_sent) + email, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, R.string.email_send_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, R.string.edit_can_not_be_null, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update_password:
                //1.获取输入框的值
                String now = et_now.getText().toString();
                String NEW = et_new.getText().toString();
                String new_password = et_new_password.getText().toString();
                //2.判断是否为空
                if (!TextUtils.isEmpty(now) && !TextUtils.isEmpty(NEW) && !TextUtils.isEmpty(new_password)) {
                    //3.判断两次新密码是否一致
                    if (NEW.equals(new_password)) {
                        //4.重置密码
                        MyUser.updateCurrentUserPassword(now, NEW, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(ForgetPasswordActivity.this, R.string.pass_reset, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, R.string.pass_reset_fail, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, R.string.twice_different, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.edit_can_not_be_null, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
