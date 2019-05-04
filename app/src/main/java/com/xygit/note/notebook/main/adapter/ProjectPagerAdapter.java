package com.xygit.note.notebook.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xygit.note.notebook.api.vo.Children;
import com.xygit.note.notebook.main.fragment.ProjectDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目分类
 *
 * @author Created by xiuyaun
 * @time on 2019/4/9
 */

public class ProjectPagerAdapter extends FragmentStatePagerAdapter {

    private List<Children> childrenList;
    private List<Fragment> fragmentList;

    public ProjectPagerAdapter(FragmentManager fm) {
        this(fm, null);
    }

    public ProjectPagerAdapter(FragmentManager fm, List<Children> childrenList) {
        super(fm);
        this.childrenList = childrenList;
        fragmentList = new ArrayList<>(7);
        initFragMent(childrenList);
    }

    private void initFragMent(List<Children> childrenList) {
        if (childrenList != null) {
            fragmentList.clear();
            for (Children children : childrenList) {
                fragmentList.add(ProjectDetailFragment.newInstance(children));
            }
        }
    }


    public void setData(List<Children> childrenList) {
        this.childrenList = childrenList;
        initFragMent(childrenList);
        notifyDataSetChanged();
    }

    public void clear() {
        if (childrenList != null) {
            childrenList.clear();
            childrenList = null;
        }
        fragmentList.clear();
        fragmentList = null;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return null != fragmentList ? fragmentList.get(position) : null;
    }

    @Override
    public int getCount() {
        return null != childrenList ? childrenList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (childrenList != null) {
            Children children = childrenList.get(position);
            return children.getName();
        }
        return super.getPageTitle(position);
    }
}
