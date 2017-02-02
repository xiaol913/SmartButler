package com.imooc.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.entity.ChatListData;
import com.imooc.smartbutler.entity.MyUser;
import com.imooc.smartbutler.utils.UtilTools;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.adapter
 * 文件名：  ChatListAdapter
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/3114:55
 * 描述：    对话Adapter
 */

public class ChatListAdapter extends BaseAdapter {

    //左边TYPE
    public static final int VALUE_LEFT_TEXT = 1;
    //右边TYPE
    public static final int VALUE_RIGHT_TEXT = 2;

    private Context mContext;
    private LayoutInflater inflater;
    private ChatListData data;
    private List<ChatListData> mList;

    public ChatListAdapter(Context mContext, List<ChatListData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderLeftText viewHolderLeftText = null;
        ViewHolderRightText viewHolderRightText = null;
        //获取当前要显示的type 根据type区分数据的加载
        int type = getItemViewType(i);
        if (view == null) {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = new ViewHolderLeftText();
                    view = inflater.inflate(R.layout.left_item, null);
                    viewHolderLeftText.tv_left_text = (TextView) view.findViewById(R.id.tv_left_text);
                    view.setTag(viewHolderLeftText);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = new ViewHolderRightText();
                    view = inflater.inflate(R.layout.right_item, null);
                    viewHolderRightText.tv_right_text = (TextView) view.findViewById(R.id.tv_right_text);
                    viewHolderRightText.iv_user_face = (ImageView) view.findViewById(R.id.iv_user_face);
                    view.setTag(viewHolderRightText);
                    break;
            }
        } else {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = (ViewHolderLeftText) view.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = (ViewHolderRightText) view.getTag();
                    break;
            }
        }
        //赋值
        ChatListData data = mList.get(i);
        switch (type) {
            case VALUE_LEFT_TEXT:
                viewHolderLeftText.tv_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
                //读取用户头像
                MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
                UtilTools.setUserFace(view.getContext(), userInfo.getUserFaceUrl(), viewHolderRightText.iv_user_face);
                viewHolderRightText.tv_right_text.setText(data.getText());
                break;
        }
        return view;
    }

    //根据数据源的position来返回要显示的item
    @Override
    public int getItemViewType(int position) {
        ChatListData data = mList.get(position);
        int type = data.getType();
        return type;
    }

    //返回所有的layout数量
    @Override
    public int getViewTypeCount() {
        return 3;//mList.size()+1
    }

    //左边的文本
    class ViewHolderLeftText {
        private TextView tv_left_text;
    }

    //右边的文本
    class ViewHolderRightText {
        private ImageView iv_user_face;
        private TextView tv_right_text;
    }
}
