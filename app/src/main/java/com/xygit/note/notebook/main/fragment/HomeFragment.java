package com.xygit.note.notebook.main.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.xygit.note.notebook.R;
import com.xygit.note.notebook.main.adapter.HomePagerAdapter;
import com.xygit.note.notebook.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {


    private TabLayout tbTabFragmentHome;
    private ViewPager vpContainerFragmentHome;
    private SparseArray<Fragment> sparseArray;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        tbTabFragmentHome = rootView.findViewById(R.id.tb_tab_fragment_home);
        vpContainerFragmentHome = rootView.findViewById(R.id.vp_container_fragment_home);
    }

    @Override
    public void initAction() {
        tbTabFragmentHome.addOnTabSelectedListener(this);
        vpContainerFragmentHome.addOnPageChangeListener(this);
    }

    @Override
    public void initData() {
        sparseArray = new SparseArray<>(3);
        sparseArray.append(0, new RecommendFragment());
        sparseArray.append(1, new KnowledgeSytemFragment());
        sparseArray.append(2, new SeniorFragment());
        vpContainerFragmentHome.setAdapter(new HomePagerAdapter(getChildFragmentManager(), sparseArray));
        tbTabFragmentHome.setupWithViewPager(vpContainerFragmentHome);
        vpContainerFragmentHome.setOffscreenPageLimit(2);
    }

    @Override
    public void clearData() {
        sparseArray.clear();
        sparseArray = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabLayout.Tab tab = tbTabFragmentHome.getTabAt(position);
        if (null != tab) {
            tab.select();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpContainerFragmentHome.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
