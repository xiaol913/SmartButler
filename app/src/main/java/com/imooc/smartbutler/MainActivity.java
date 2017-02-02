package com.imooc.smartbutler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.imooc.smartbutler.fragment.ButlerFragment;
import com.imooc.smartbutler.fragment.GirlFragment;
import com.imooc.smartbutler.fragment.UserFragment;
import com.imooc.smartbutler.fragment.WechatFragment;
import com.imooc.smartbutler.ui.SettingActivity;

import java.util.ArrayList;
import java.util.List;
/**
 * 项目名：  SmartButler
 * 包名：    com.imooc.smartbutler.ui
 * 文件名：  MainActivity
 * 创建者：  Shawn Gao
 * 创建时间：2017/1/2818:23
 * 描述：    主页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //Title
    private List<String> mTitile;
    //Fragment
    private List<Fragment> mFragment;
    //悬浮窗
    private FloatingActionButton fab_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去掉阴影
        getSupportActionBar().setElevation(0);
        initData();
        initView();
    }

    //初始化数据
    private void initData() {
        mTitile = new ArrayList<>();
        mTitile.add(getString(R.string.title_1));
        mTitile.add(getString(R.string.title_2));
        mTitile.add(getString(R.string.title_3));
        mTitile.add(getString(R.string.title_4));
        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());
    }

    //初始化View
    private void initView() {
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);
        //默认隐藏
        fab_setting.setVisibility(View.GONE);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
        //mViewPager滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab_setting.setVisibility(View.GONE);
                } else {
                    fab_setting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return null != mFragment ? mFragment.size() : 0;
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitile.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
