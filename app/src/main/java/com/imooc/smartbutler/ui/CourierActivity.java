package com.imooc.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.adapter.CourierAdapter;
import com.imooc.smartbutler.entity.CourierData;
import com.imooc.smartbutler.utils.L;
import com.imooc.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  CourierActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/3111:36
 * 描述：    快递查询
 */

public class CourierActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_number;
    private Button btn_get_courier;
    private ListView mListView;

    private List<CourierData> mList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_number = (EditText) findViewById(R.id.et_number);
        btn_get_courier = (Button) findViewById(R.id.btn_get_courier);
        btn_get_courier.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.mListView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_courier:
                /**
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.拿到数据区请求数据（Json）
                 * 4.解析Json
                 * 5.listView适配器
                 * 6.实体类（item）
                 * 7.设置数据/显示效果
                 */
                //1.获取输入框的内容
                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();
                //拼接url
                String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.JUHE_APP_ID + "&com=" + name + "&no=" + number;
                //2.判断是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                    //3.拿到数据区请求数据（Json）
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            L.i("Json:" + t);
                            //4.解析Json
                            parsingJson(t);
                        }
                    });
                } else {
                    Toast.makeText(this, getString(R.string.txt_no_result), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);

                CourierData data = new CourierData();
                data.setRemark(json.getString("remark"));
                data.setZone(json.getString("zone"));
                data.setDatetime(json.getString("datetime"));
                mList.add(data);
            }
            //倒序
            Collections.reverse(mList);
            CourierAdapter adapter = new CourierAdapter(this, mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
