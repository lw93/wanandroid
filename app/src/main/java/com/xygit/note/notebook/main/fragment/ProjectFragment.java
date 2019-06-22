package com.xygit.note.notebook.main.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xygit.note.notebook.R;
import com.xygit.note.notebook.api.vo.CommResponse;
import com.xygit.note.notebook.api.vo.Children;
import com.xygit.note.notebook.base.BaseFragment;
import com.xygit.note.notebook.main.adapter.ProjectPagerAdapter;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;

import java.util.List;

/**
 * 项目页
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends BaseFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private TabLayout tbFragmentProject;
    private ViewPager vpContainerFragmentProject;
    private View emptyLayout;
    private ProjectPagerAdapter projectPagerAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_project;
    }

    @Override
    public void initView() {
        tbFragmentProject = rootView.findViewById(R.id.tb_fragment_project);
        vpContainerFragmentProject = rootView.findViewById(R.id.vp_container_fragment_project);
        emptyLayout = rootView.findViewById(R.id.cl_container_layout_empty);
    }

    @Override
    public void initAction() {
        emptyLayout.setOnClickListener(this);
        tbFragmentProject.addOnTabSelectedListener(this);
        vpContainerFragmentProject.addOnPageChangeListener(this);
    }

    @Override
    public void initData() {
        projectPagerAdapter = new ProjectPagerAdapter(getChildFragmentManager());
        vpContainerFragmentProject.setAdapter(projectPagerAdapter);
        tbFragmentProject.setupWithViewPager(vpContainerFragmentProject);
        quryProjects();
    }

    private void quryProjects() {
        HttpManager.getInstance().quryProjectCategory(null, new CommSubscriber<List<Children>>(getActivity()) {
            @Override
            protected void onSucess(CommResponse<List<Children>> listCommResponse) {
                if (listCommResponse != null) {
                    projectPagerAdapter.setData(listCommResponse.getData());
                }
                emptyLayout();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                emptyLayout();
            }
        });
    }

    @Override
    public void clearData() {
        if (projectPagerAdapter != null) {
            projectPagerAdapter.clear();
            projectPagerAdapter = null;
        }
    }

    @Override
    public void onClick(View v) {
        emptyLayout.setVisibility(View.GONE);
        quryProjects();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpContainerFragmentProject.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabLayout.Tab tab = tbFragmentProject.getTabAt(position);
        if (null != tab) {
            tab.select();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void emptyLayout() {
        if (projectPagerAdapter.getCount() < 1) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.GONE);
        }
    }
}
