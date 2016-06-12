package com.df.Fragment;

import android.support.v4.app.Fragment;

/**
 * Created by asus88 on 2016/5/15.
 */
public class FragmentInfo {
    private Fragment fragment;
    private String title;


    public FragmentInfo(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
