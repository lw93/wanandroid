package com.xygit.note.notebook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/15
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArray<Fragment> fragmentSparse;

    public SparseArray<Fragment> getFragmentSparse() {
        return fragmentSparse;
    }

    public void setFragmentSparse(SparseArray<Fragment> fragmentSparse) {
        this.fragmentSparse = fragmentSparse;
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(FragmentManager fm) {
        this(fm, null);
    }

    public ViewPagerAdapter(FragmentManager fm, SparseArray<Fragment> fragmentSparse) {
        super(fm);
        this.fragmentSparse = fragmentSparse;
    }

    @Override
    public Fragment getItem(int position) {
        return null != fragmentSparse ? fragmentSparse.get(position) : null;
    }

    @Override
    public int getCount() {
        return null != fragmentSparse ? fragmentSparse.size() : 0;
    }

}
