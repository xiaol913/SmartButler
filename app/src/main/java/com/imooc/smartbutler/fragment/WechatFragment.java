package com.imooc.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.adapter.WeChatAdapter;
import com.imooc.smartbutler.entity.WeChatData;
import com.imooc.smartbutler.ui.WebViewActivity;
import com.imooc.smartbutler.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.imooc.smartbutler.utils.StaticClass.WECHAT_APP_ID;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.fragment
 * 文件名：  WechatFragment
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2818:24
 * 描述：    微信精选
 */

public class WechatFragment extends Fragment {

    private ListView mListView;
    private List<WeChatData> mList = new ArrayList<>();
    private List<String> mListTitle = new ArrayList<>();
    private List<String> mListUrl = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mListView = (ListView) view.findViewById(R.id.mListView);
        //解析接口
        String url = "http://v.juhe.cn/weixin/query?key=" + WECHAT_APP_ID + "&ps=100";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("json:" + t);
                parsingJson(t);
            }
        });
        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                L.i("position:" + i);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", mListTitle.get(i));
                intent.putExtra("url", mListUrl.get(i));
                startActivity(intent);
            }
        });
    }

    //解析Json
    private void parsingJson(String t) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(t);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                WeChatData data = new WeChatData();

                String title = json.getString("title");
                String url = json.getString("url");

                data.setTitle(title);
                data.setSource(json.getString("source"));
                data.setFirstImg(json.getString("firstImg"));

                mList.add(data);

                mListTitle.add(title);
                mListUrl.add(url);
            }
            WeChatAdapter adapter = new WeChatAdapter(getActivity(), mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
