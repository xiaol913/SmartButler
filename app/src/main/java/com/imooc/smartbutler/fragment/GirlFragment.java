package com.imooc.smartbutler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.adapter.GirlAdapter;
import com.imooc.smartbutler.entity.GirlData;
import com.imooc.smartbutler.utils.L;
import com.imooc.smartbutler.utils.PicassoUtils;
import com.imooc.smartbutler.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.fragment
 * 文件名：  GirlFragment
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2818:24
 * 描述：    美女社区
 */

public class GirlFragment extends Fragment {
    //列表
    private GridView mGridView;
    //数据
    private List<GirlData> mList = new ArrayList<>();
    //适配器
    private GirlAdapter mAdapter;
    //提示框
    private CustomDialog dialog;
    //预览图片
    private ImageView iv_img;
    //图片地址
    private List<String> mListUrl = new ArrayList<>();
    //PhotoView
    private PhotoViewAttacher mAttacher;


    /**
     * 1.监听点击事件
     * 2.提示框
     * 3.加载图片
     * 4.PhotoView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //初始化提示框
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, R.layout.dialog_girl, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        iv_img = (ImageView) dialog.findViewById(R.id.iv_img);

        String welfare = null;
        try {
            //Gank升級 需要转码
            welfare = URLEncoder.encode("福利", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://gank.io/api/search/query/listview/category/" + welfare + "/count/50/page/1";
        //解析
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("Girl" + t);
                parsingJson(t);
            }
        });
        //监听点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //解析图片
                PicassoUtils.loadImageView(getActivity(), mListUrl.get(i), iv_img);
                //缩放
                mAttacher = new PhotoViewAttacher(iv_img);
                //刷新
                mAttacher.update();
                dialog.show();
            }
        });
    }

    //解析JSON
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String url = jsonObject1.getString("url");
                mListUrl.add(url);
                GirlData data = new GirlData();
                data.setImgUrl(url);
                mList.add(data);
            }
            mAdapter = new GirlAdapter(getActivity(), mList);
            mGridView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
