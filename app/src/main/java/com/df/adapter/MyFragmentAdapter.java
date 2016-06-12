package com.df.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.df.Fragment.FragmentInfo;

import java.util.List;

/**
 * Created by asus88 on 2016/3/22.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<FragmentInfo> mData;
    private Context context;

    private View[] mTabViews;

    public MyFragmentAdapter(FragmentManager fm, List<FragmentInfo> list, Context context) {
        super(fm);
        this.mData = list;
        this.context = context;

        this.mTabViews = new View[list.size()];
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    public View getTabView(int position) {
        View view = mTabViews[position];
        if (view == null) {
//            view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
//            RedTipTextView textView = (RedTipTextView) view.findViewById(R.id.tab_textView);
//            textView.setText(getPageTitle(position));
//            mTabViews[position] = view;
        }
        return view;
    }

}
