package com.imooc.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import static com.imooc.smartbutler.utils.StaticClass.GSD_APP_ID;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  PhoneActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/31 13:05
 * 描述：    归属地验证
 */

public class PhoneActivity extends BaseActivity implements View.OnClickListener {
    //输入框
    private EditText et_number;
    //公司logo
    private ImageView iv_company;
    //结果
    private TextView tv_result;
    //按键
    private Button btn_check, btn_DEL, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0;

    //标记位
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        initView();
    }

    private void initView() {
        et_number = (EditText) findViewById(R.id.et_number);
        iv_company = (ImageView) findViewById(R.id.iv_company);
        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_DEL = (Button) findViewById(R.id.btn_DEL);
        btn_DEL.setOnClickListener(this);
        //长按
        btn_DEL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_number.setText("");
                return false;
            }
        });
        btn_check = (Button) findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /**
         * 逻辑
         * 1.获取输入框的内容
         * 2.判断是否为空
         * 3.网络请求
         * 4.解析Json
         * 5.结果显示
         * ----------
         * 键盘逻辑
         */
        //获取到输入框的内容
        String str = et_number.getText().toString();
        switch (view.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if (flag) {
                    str = "";
                    et_number.setText("");
                    flag = false;
                }
                et_number.setText(str + ((Button) view).getText());
                //移动光标
                et_number.setSelection(str.length() + 1);
                break;
            case R.id.btn_DEL:
                if (!TextUtils.isEmpty(str) && str.length() > 0) {
                    //每次结尾减去1
                    et_number.setText(str.substring(0, str.length() - 1));
                    //移动光标
                    et_number.setSelection(str.length() - 1);
                }
                break;
            case R.id.btn_check:
                if (!TextUtils.isEmpty(str)) {
                    getPhone(str);
                }
                break;
        }
    }

    private void getPhone(String str) {
        String url = "http://apis.juhe.cn/mobile/get?phone=" + str + "&key=" + GSD_APP_ID;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
//                Toast.makeText(PhoneActivity.this,"结果:"+ t, Toast.LENGTH_SHORT).show();
                L.i("phone:" + t);
                parsingJson(t);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            String province = jsonObject1.getString("province");
            String city = jsonObject1.getString("city");
            String areacode = jsonObject1.getString("areacode");
            String zip = jsonObject1.getString("zip");
            String company = jsonObject1.getString("company");
            String card = jsonObject1.getString("card");

            tv_result.setText(getString(R.string.txt_city) + province + city + "\n"
                    + getString(R.string.txt_tel_zip) + areacode + "\n"
                    + getString(R.string.txt_zip) + zip + "\n"
                    + getString(R.string.txt_tel_company) + company + "\n"
                    + getString(R.string.txt_type) + card + "\n");
            //图片显示
            switch (company) {
                case "移动":
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "联通":
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "电信":
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }
            flag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
