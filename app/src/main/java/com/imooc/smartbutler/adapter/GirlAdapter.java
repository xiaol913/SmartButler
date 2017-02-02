package com.imooc.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imooc.smartbutler.R;
import com.imooc.smartbutler.entity.GirlData;
import com.imooc.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.adapter
 * 文件名：  GirlAdapter
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/3123:34
 * 描述：    妹子适配器
 */

public class GirlAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<GirlData> mList;
    private GirlData data;
    private WindowManager wm;

    private int width;

    public GirlAdapter(Context mContext, List<GirlData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
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
        ViewHolder viewHolder =null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.gril_item,null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageview);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        data = mList.get(i);
        //解析图片
        String url = data.getImgUrl();
        PicassoUtils.loadImageView(mContext,url,viewHolder.imageView,width/2,500);
        return view;
    }

    class ViewHolder{
        private ImageView imageView;
    }
}
