package com.imooc.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.imooc.smartbutler.R;
import com.imooc.smartbutler.adapter.ChatListAdapter;
import com.imooc.smartbutler.entity.ChatListData;
import com.imooc.smartbutler.utils.L;
import com.imooc.smartbutler.utils.ShareUtils;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.imooc.smartbutler.utils.StaticClass.JQR_APP_ID;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.fragment
 * 文件名：  ButlerFragment
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2818:27
 * 描述：    服务管家
 */

public class ButlerFragment extends Fragment implements View.OnClickListener {
    private ListView mChatListView;
    private EditText et_text;
    private Button btn_send;
    private ChatListAdapter adapter;

    //TTS
    private SpeechSynthesizer mTts;

    private List<ChatListData> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");


        mChatListView = (ListView) view.findViewById(R.id.mChatListView);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        et_text = (EditText) view.findViewById(R.id.et_text);

        //设置适配器
        adapter = new ChatListAdapter(getActivity(), mList);
        mChatListView.setAdapter(adapter);

        addLeftItem(getString(R.string.txt_xiaoyou));
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                /**
                 * 1.获取输入框内容
                 * 2.判断是否为空
                 * 3.判断长度不能大于30
                 * 4.清空输入框
                 * 5.添加你输入的内容到right
                 * 6.发送给机器人请求返回内容
                 * 7.得到返回值添加在leftItem
                 */
                String oldText = et_text.getText().toString().trim();
                //转码
                String text = null;
                try {
                    text = URLEncoder.encode(oldText, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(oldText)) {
                    if (oldText.length() > 30) {
                        Toast.makeText(getActivity(), R.string.text_too_long, Toast.LENGTH_SHORT).show();
                    } else {
                        et_text.setText("");
                        //显示转码前的字符
                        addRightItem(oldText);
                        String url = "http://op.juhe.cn/robot/index?info=" + text + "&key=" + JQR_APP_ID;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                L.i("Json" + t);
                                parsingJson(t);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.edit_can_not_be_null, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            String result = jsonObject1.getString("text");
            addLeftItem(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text) {
        boolean isSpeak = ShareUtils.getBoolean(getActivity(), "isSpeak", false);
        if (isSpeak) {
            startSpeak(text);
        }
        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //添加右边文本
    private void addRightItem(String text) {
        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //开始说话
    private void startSpeak(String text) {
        //开始合成
        mTts.startSpeaking(text, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
}
