package com.imooc.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.entity.MyUser;
import com.imooc.smartbutler.ui.ChangePassActivity;
import com.imooc.smartbutler.ui.CourierActivity;
import com.imooc.smartbutler.ui.LoginActivity;
import com.imooc.smartbutler.ui.PhoneActivity;
import com.imooc.smartbutler.utils.L;
import com.imooc.smartbutler.utils.UtilTools;
import com.imooc.smartbutler.view.CustomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.fragment
 * 文件名：  UserFragment
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2818:23
 * 描述：    个人中心
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private Button btn_exit_user;
    private TextView edit_user;

    private EditText et_username;
    //性别
    private RadioButton rb_boy, rb_girl;
    private EditText et_age;
    private EditText et_desc;
    //头像url路径
    private String faceUrl;
    //更新按钮
    private Button btn_update_ok;
    //圆形头像
    private CircleImageView profile_image;

    private Button btn_camera, btn_picture, btn_cancel;

    private CustomDialog dialog;
    private CustomDialog upDialog;
    //修改密码
    private Button ll_edit_pass;
    //物流查询
    private TextView tv_courier;
    //归属地查询
    private TextView tv_phone;

    //图片默认文件名
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    //返回值
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    //初始化View
    private void findView(View view) {
        btn_exit_user = (Button) view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user = (TextView) view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        et_username = (EditText) view.findViewById(R.id.et_username);
        //性别
        rb_boy = (RadioButton) view.findViewById(R.id.rb_boy);
        rb_girl = (RadioButton) view.findViewById(R.id.rb_girl);

        et_age = (EditText) view.findViewById(R.id.et_age);
        et_desc = (EditText) view.findViewById(R.id.et_desc);
        tv_courier = (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);

        btn_update_ok = (Button) view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);

        ll_edit_pass = (Button) view.findViewById(R.id.ll_edit_pass);
        ll_edit_pass.setOnClickListener(this);

        //初始化Dialog
        dialog = new CustomDialog(getActivity(), 0, 0, R.layout.dialog_photo, R.style.Theme_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        upDialog = new CustomDialog(getActivity(), 100, 100, R.layout.dialog_uploading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //提示框以外点击无效
        dialog.setCancelable(false);
        upDialog.setCancelable(false);
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);


        //默认不可点击/不可输入
        setEnabled(false);
        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        String newAge = userInfo.getAge() + "";
        et_age.setText(newAge);
        et_desc.setText(userInfo.getDesc());
        faceUrl = userInfo.getUserFaceUrl();
        //获取性别
        if (userInfo.isSex())
            rb_boy.setChecked(true);
        else
            rb_girl.setChecked(true);
        //加载用户头像
        UtilTools.setUserFace(getActivity(), faceUrl, profile_image);
    }

    //控制焦点
    private void setEnabled(boolean is) {
        et_username.setEnabled(is);
        rb_boy.setEnabled(is);
        rb_girl.setEnabled(is);
        et_age.setEnabled(is);
        et_desc.setEnabled(is);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            //退出登录
            case R.id.btn_exit_user:
                //清除缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            //编辑资料
            case R.id.edit_user:
                setEnabled(true);
                btn_update_ok.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_update_ok:
                //1.获得输入框的值
                String username = et_username.getText().toString();
                String age = et_age.getText().toString();
                String desc = et_desc.getText().toString();
                //2.判断是否为空
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(age)) {
                    //3.更新属性
                    final MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    //判断性别
                    if (rb_boy.isChecked())
                        user.setSex(true);
                    else
                        user.setSex(false);
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc(getString(R.string.txt_nothing_typed));
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //修改成功
                                setEnabled(false);
                                btn_update_ok.setVisibility(view.GONE);
                                Toast.makeText(getActivity(), R.string.edit_success, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.edit_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), R.string.txt_cannot_null, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
            case R.id.ll_edit_pass:
                startActivity(new Intent(getActivity(), ChangePassActivity.class));
                break;
        }
    }

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                    }
                    break;
            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("url == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        //输出Uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PHOTO_IMAGE_FILE_NAME);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(final Intent data) {
        final Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            saveBitmap(bitmap);
            upLoadFace();
        }
    }

    //保存图片
    private void saveBitmap(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            L.e(getString(R.string.txt_file_not_exist));
        } catch (IOException e) {
            L.e(getString(R.string.txt_balabala));
        }
    }

    //上传图片
    private void upLoadFace() {
        //获取当前用户id
        upDialog.show();
        final MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        //保存当前用户原始头像URL
        final String oldUrl = userInfo.getUserFaceUrl();
        //获取修改后图片
        final BmobFile bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //判断上传成功后，修改用户图片
                    MyUser user = new MyUser();
                    faceUrl = bmobFile.getFileUrl();
                    user.setUserFaceUrl(faceUrl);
                    user.setUsername(userInfo.getUsername());
                    user.setAge(userInfo.getAge());
                    user.setDesc(userInfo.getDesc());
                    user.setSex(userInfo.isSex());
                    user.update(userInfo.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                upDialog.dismiss();
                                //删除原来的图片
                                UtilTools.delFileWithUrl(oldUrl);
                                //设置当前界面用户头像
                                UtilTools.setUserFace(getActivity(), faceUrl, profile_image);
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                Toast.makeText(getActivity(), R.string.edit_success, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.edit_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), R.string.edit_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
