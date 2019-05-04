package com.xygit.note.notebook.main.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.vo.Children;
import com.xygit.note.notebook.base.BaseFragment;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.main.activity.KnowledgeSystemDetailActivity;
import com.xygit.note.notebook.main.adapter.KnowledgeSystemAdapter;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * 体系页
 *
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public class KnowledgeSytemFragment extends BaseFragment implements View.OnClickListener, OnRefreshListener,
        HttpCallBack, KnowledgeSystemAdapter.OnTagClickListener {

    private RecyclerView rvFragmentSystem;
    private SmartRefreshLayout refreshFragmentSystem;
    private View emptyLayout;
    private KnowledgeSystemAdapter knowledgeSystemAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_knowledge_system;
    }

    @Override
    public void initView() {
        rvFragmentSystem = (RecyclerView) rootView.findViewById(R.id.rv_fragment_system);
        refreshFragmentSystem = (SmartRefreshLayout) rootView.findViewById(R.id.refresh_fragment_system);
        emptyLayout = rootView.findViewById(R.id.cl_container_layout_empty);
    }

    @Override
    public void initAction() {
        emptyLayout.setOnClickListener(this);
        refreshFragmentSystem.setRefreshHeader(new ClassicsHeader(refreshFragmentSystem.getContext()));
        refreshFragmentSystem.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        emptyLayout.setVisibility(View.GONE);
        knowledgeSystemAdapter = new KnowledgeSystemAdapter(null, rvFragmentSystem);
        knowledgeSystemAdapter.setOnTagClickListener(this);
        rvFragmentSystem.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFragmentSystem.addItemDecoration(new DividerItemDecoration(rvFragmentSystem.getContext(), LinearLayout.VERTICAL));
        rvFragmentSystem.setAdapter(knowledgeSystemAdapter);
        refreshFragmentSystem.autoRefresh();
    }

    @Override
    public void clearData() {
        if (knowledgeSystemAdapter != null) {
            knowledgeSystemAdapter.clear();
            knowledgeSystemAdapter = null;
        }
    }

    private void emptyLayout() {
        if (knowledgeSystemAdapter.getItemCount() < 1) {
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
            refreshFragmentSystem.autoRefresh();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        HttpManager.getInstance().quryTreeDatas(this,new CommSubscriber<List<Children>>(getActivity()) {
            @Override
            protected void onSucess(CommResponse<List<Children>> listCommResponse) {
                if (listCommResponse != null) {
                    List<Children> childrenList = listCommResponse.getData();
                    knowledgeSystemAdapter.clear();
                    knowledgeSystemAdapter.addItems(childrenList);
                    hideProgress();
                }
            }
        });
    }


    @Override
    public void showProgress() {
        if (refreshFragmentSystem.getState() != RefreshState.Refreshing) {
            refreshFragmentSystem.autoRefresh();
        }
    }

    @Override
    public void hideProgress() {
        refreshFragmentSystem.finishRefresh(true);
        emptyLayout();
    }

    @Override
    public void onTagClick(Children parent, int parentPostion, int childPosition, TagFlowLayout flowLayout, View childView) {
        if (parent == null) return;
        List<Children> childrens = parent.getChildren();
        if (null == childrens) return;
        Children children = childrens.get(childPosition);
        Intent intent = new Intent(getActivity(), KnowledgeSystemDetailActivity.class);
        intent.putExtra(NoteBookConst.INETENT_START_TYPE, KnowledgeSystemDetailActivity.START_FROM_KNOWLEDGE_SYSTEM);
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, children);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), childView, "tagTextView");
        startActivity(intent, options.toBundle());
    }
}
