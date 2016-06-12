package com.df.dianping;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.df.Fragment.FragmentInfo;
import com.df.adapter.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {
    public static final String AppKey = "2cdc69b8257c5b4f65cc5b241a5a3e73";

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainFragment mMainFragment;
    private MainPersonalFragment mPersonalFragment;
    private MyFragmentAdapter fragmentAdapter;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);


        Bmob.initialize(MainActivity.this, AppKey);

        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        List<FragmentInfo> fragmentList = new ArrayList<>();
        mMainFragment = new MainFragment();
        mPersonalFragment = new MainPersonalFragment();

        fragmentList.add(new FragmentInfo(mMainFragment, "首页"));
        fragmentList.add(new FragmentInfo(mPersonalFragment, "个人中心"));
        fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList, this);
        mViewPager.setAdapter(fragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyUp(keyCode, event);
    }

}