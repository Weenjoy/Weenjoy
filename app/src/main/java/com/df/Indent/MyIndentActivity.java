package com.df.Indent;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.df.Fragment.*;
import com.df.adapter.FragmentAdapter;
import com.df.dianping.R;

public class MyIndentActivity extends FragmentActivity {

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    private ViewPager mPageVp;
    /**
     * Tab显示内容TextView
     */
    private TextView mTabAllTv, mTabPayingTv, mTabPayedTv;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv;
    /**
     * 返回键
     */
    private ImageView mIndentBackIv;
    /**
     * Fragment
     */
    private AllFragment mAllFg;
    private PayingFragment mPayingFg;
    private PayedFragment mPayedFg;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivity_indent);
        findById();
        init();
        initTabLineWidth();
        back();
    }

    private void back() {
        mIndentBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 3;
        mTabLineIv.setLayoutParams(lp);
    }

    private void init() {
        mAllFg = new AllFragment();
        mPayedFg = new PayedFragment();
        mPayingFg = new PayingFragment();
        mFragmentList.add(mAllFg);
        mFragmentList.add(mPayingFg);
        mFragmentList.add(mPayedFg);

        mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);

        mPageVp.addOnPageChangeListener(new OnPageChangeListener() {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv.getLayoutParams();
            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset, int positionOffsetPixels) {

                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTabAllTv.setTextColor(getResources().getColor(R.color.orange));
                        break;
                    case 1:
                        mTabPayingTv.setTextColor(getResources().getColor(R.color.orange));
                        break;
                    case 2:
                        mTabPayedTv.setTextColor(getResources().getColor(R.color.orange));
                        break;
                }
                currentIndex = position;
                lp.leftMargin = (int) (currentIndex * (screenWidth / 3));
                mTabLineIv.setLayoutParams(lp);
            }

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("margin",state+"");
            }
        });

    }

    private void findById() {
        mTabAllTv = (TextView) this.findViewById(R.id.tv_all);
        mTabPayedTv = (TextView) this.findViewById(R.id.tv_payed);
        mTabPayingTv = (TextView) this.findViewById(R.id.tv_paying);
        mTabLineIv = (ImageView) this.findViewById(R.id.iv_tab_line);
        mIndentBackIv = (ImageView) this.findViewById(R.id.iv_indent_back);
        mPageVp = (ViewPager) this.findViewById(R.id.vp_page);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mTabAllTv.setTextColor(Color.BLACK);
        mTabPayingTv.setTextColor(Color.BLACK);
        mTabPayedTv.setTextColor(Color.BLACK);
    }

}
