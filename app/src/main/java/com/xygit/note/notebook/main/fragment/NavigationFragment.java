package com.xygit.note.notebook.main.fragment;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.api.vo.CommResponse;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.api.vo.Navigation;
import com.xygit.note.notebook.base.BaseFragment;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.main.adapter.NavigationAdapter;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.web.WebActivity;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * 导航页
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends BaseFragment implements View.OnClickListener, OnRefreshListener, HttpCallBack, NavigationAdapter.OnTagClickListener {
    private RecyclerView rvFragmentNavigation;
    private SmartRefreshLayout refreshFragmentNavigation;
    private View emptyLayout;
    private NavigationAdapter navigationAdapter;
    private TextView tvTitile;

    @Override
    public int layoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    public void initView() {
        tvTitile = rootView.findViewById(R.id.tv_title);
        rvFragmentNavigation = (RecyclerView) rootView.findViewById(R.id.rv_fragment_navigation);
        refreshFragmentNavigation = (SmartRefreshLayout) rootView.findViewById(R.id.refresh_fragment_navigation);
        emptyLayout = rootView.findViewById(R.id.cl_container_layout_empty);
    }

    @Override
    public void initAction() {
        emptyLayout.setOnClickListener(this);
        refreshFragmentNavigation.setRefreshHeader(new ClassicsHeader(refreshFragmentNavigation.getContext()));
        refreshFragmentNavigation.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        tvTitile.setText("导航");
        emptyLayout.setVisibility(View.GONE);
        navigationAdapter = new NavigationAdapter(null, rvFragmentNavigation);
        navigationAdapter.setOnTagClickListener(this);
        rvFragmentNavigation.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFragmentNavigation.addItemDecoration(new DividerItemDecoration(rvFragmentNavigation.getContext(), LinearLayout.VERTICAL));
        rvFragmentNavigation.setAdapter(navigationAdapter);
        refreshFragmentNavigation.autoRefresh();
    }

    @Override
    public void clearData() {
        if (navigationAdapter != null) {
            navigationAdapter.clear();
            navigationAdapter = null;
        }
    }

    private void emptyLayout() {
        if (navigationAdapter != null && navigationAdapter.getItemCount() < 1) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_container_layout_empty) {
            emptyLayout.setVisibility(View.GONE);
            refreshFragmentNavigation.autoRefresh();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        HttpManager.getInstance().quryNavigationDatas(this, new CommSubscriber<List<Navigation>>(getActivity()) {
            @Override
            protected void onSucess(CommResponse<List<Navigation>> listCommResponse) {
                if (listCommResponse != null) {
                    List<Navigation> childrenList = listCommResponse.getData();
                    navigationAdapter.clear();
                    navigationAdapter.addItems(childrenList);
                    hideProgress();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgress();
            }
        });
    }


    @Override
    public void showProgress() {
        if (refreshFragmentNavigation.getState() != RefreshState.Refreshing) {
            refreshFragmentNavigation.autoRefresh();
        }
    }

    @Override
    public void hideProgress() {
        refreshFragmentNavigation.finishRefresh(true);
        emptyLayout();
    }

    @Override
    public void onTagClick(Navigation parent, int parentPostion, int childPosition, TagFlowLayout flowLayout, View childView) {
        if (parent == null) return;
        List<CommonData> datas = parent.getArticles();
        if (datas == null) return;
        CommonData data = datas.get(childPosition);
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(NoteBookConst.INETENT_PARAM_URL, data.getLink());
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, data);
        ActivityUtil.startActivity(getActivity(), intent);
    }
}
