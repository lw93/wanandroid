package com.xygit.note.notebook.main.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;

import com.xygit.note.notebook.adapter.ViewPagerAdapter;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public class HomePagerAdapter extends ViewPagerAdapter {

    public HomePagerAdapter(FragmentManager fm, SparseArray<Fragment> fragmentSparse) {
        super(fm, fragmentSparse);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (0 == position) {
            return "推荐";
        } else if (1 == position) {
            return "体系";
        } else if (2 == position) {
            return "大牛";
        }
        return super.getPageTitle(position);
    }
}
