package com.imooc.smartbutler.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  AboutActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/2/119:42
 * 描述：    关于本软件
 */

public class AboutActivity extends BaseActivity {

    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setElevation(0);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);

        mList.add(getString(R.string.text_app_name) + getString(R.string.app_name));
        mList.add(getString(R.string.text_version) + UtilTools.getVersion(this));
        mList.add(getString(R.string.txt_web));
        mList.add(getString(R.string.text_about_email));

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        //设置适配器
        mListView.setAdapter(mAdapter);
    }
}
