package com.imooc.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.entity.MyUser;
import com.imooc.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  ChangePassActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/2/21:12
 * 描述：    修改密码
 */

public class ChangePassActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_now;
    private EditText et_new;
    private EditText et_new_password;
    private Button btn_update_password;
    //Dialog
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);


        initView();
    }

    private void initView() {
        et_now = (EditText) findViewById(R.id.et_now);
        et_new = (EditText) findViewById(R.id.et_new);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);
        btn_update_password.setOnClickListener(this);
        //初始化Dialog
        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_resetting, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏外点击无效
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_password:
                //1.获取输入框的值
                String now = et_now.getText().toString();
                String NEW = et_new.getText().toString();
                String new_password = et_new_password.getText().toString();
                //2.判断是否为空
                if (!TextUtils.isEmpty(now) && !TextUtils.isEmpty(NEW) && !TextUtils.isEmpty(new_password)) {
                    //3.判断两次新密码是否一致
                    if (NEW.equals(new_password)) {
                        dialog.show();
                        //4.重置密码
                        MyUser.updateCurrentUserPassword(now, NEW, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                dialog.dismiss();
                                if (e == null) {
                                    Toast.makeText(ChangePassActivity.this, R.string.pass_reset, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ChangePassActivity.this, R.string.pass_reset_fail, Toast.LENGTH_SHORT).show();
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
