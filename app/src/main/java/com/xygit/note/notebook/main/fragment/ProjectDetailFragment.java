package com.xygit.note.notebook.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerAdapter;
import com.xygit.note.notebook.api.vo.CommResponse;
import com.xygit.note.notebook.api.vo.BasePage;
import com.xygit.note.notebook.api.vo.Children;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.base.BaseFragment;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.login.LoginActivity;
import com.xygit.note.notebook.main.activity.CollectionActivity;
import com.xygit.note.notebook.main.adapter.HomeArticleAdapter;
import com.xygit.note.notebook.manager.evenbus.CollectAction;
import com.xygit.note.notebook.manager.evenbus.LoginAction;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.web.WebActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Iterator;

/**
 * 体系页
 *
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public class ProjectDetailFragment extends BaseFragment implements View.OnClickListener, OnRefreshListener,
        HttpCallBack, BaseRecyclerAdapter.OnItemClickListener, OnLoadMoreListener, HomeArticleAdapter.OnCheckedChangeListener {
    private static final String PARMA_CHILDREN = "children";
    private RecyclerView rvFragmentProjectDetail;
    private SmartRefreshLayout refreshFragmentProjectDetail;
    private View emptyLayout;
    private HomeArticleAdapter projectDetailAdapter;
    private Children children;
    private int currentPage = 1;
//    private CommonData collectItem;

    public static ProjectDetailFragment newInstance(Children children) {
        ProjectDetailFragment projectDetailFragment = new ProjectDetailFragment();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(PARMA_CHILDREN, children);
        projectDetailFragment.setArguments(bundle);
        return projectDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            children = (Children) bundle.getSerializable(PARMA_CHILDREN);
        }
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_project_detail;
    }


    @Override
    public void initView() {
        rvFragmentProjectDetail = (RecyclerView) rootView.findViewById(R.id.rv_fragment_prject_detail);
        refreshFragmentProjectDetail = (SmartRefreshLayout) rootView.findViewById(R.id.refresh_fragment_prject_detail);
        emptyLayout = rootView.findViewById(R.id.cl_container_layout_empty);
    }

    @Override
    public void initAction() {
        emptyLayout.setOnClickListener(this);
        refreshFragmentProjectDetail.setEnableLoadMore(true);
        refreshFragmentProjectDetail.setRefreshHeader(new ClassicsHeader(refreshFragmentProjectDetail.getContext()));
        refreshFragmentProjectDetail.setOnRefreshListener(this);
        refreshFragmentProjectDetail.setOnLoadMoreListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        emptyLayout.setVisibility(View.GONE);
        projectDetailAdapter = new HomeArticleAdapter(null, rvFragmentProjectDetail);
        projectDetailAdapter.setOnItemClickListener(this);
        projectDetailAdapter.setOnCheckedChangeListener(this);
        rvFragmentProjectDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFragmentProjectDetail.addItemDecoration(new DividerItemDecoration(rvFragmentProjectDetail.getContext(), LinearLayout.VERTICAL));
        rvFragmentProjectDetail.setAdapter(projectDetailAdapter);
        refreshFragmentProjectDetail.autoRefresh();
    }

    @Override
    public void clearData() {
        if (projectDetailAdapter != null) {
            projectDetailAdapter.clear();
            projectDetailAdapter = null;
        }
        if (children != null) {
            children = null;
        }
//        if (collectItem != null) {
//            collectItem = null;
//        }
        EventBus.getDefault().unregister(this);
    }

    private void emptyLayout() {
        if (projectDetailAdapter == null) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else if (projectDetailAdapter.getItemCount() < 1) {
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
            refreshFragmentProjectDetail.autoRefresh();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 1;
        quryProjectList();
    }

    private void quryProjectList() {
        String cid = "";
        if (children != null) {
            cid = String.valueOf(children.getId());
        }
        HttpManager.getInstance().qurySingleProjectCategory(this, String.valueOf(currentPage), cid, new CommSubscriber<BasePage<CommonData>>(getActivity()) {
            @Override
            protected void onSucess(CommResponse<BasePage<CommonData>> basePageCommResponse) {
                if (basePageCommResponse != null) {
                    BasePage<CommonData> commonDataBasePage = basePageCommResponse.getData();
                    if (currentPage == 1) {
                        projectDetailAdapter.clear();
                    }
                    projectDetailAdapter.addItems(commonDataBasePage.getDatas());
                    hideProgress();
                }
            }
        });
    }


    @Override
    public void showProgress() {
        if (refreshFragmentProjectDetail.getState() != RefreshState.Refreshing) {
            refreshFragmentProjectDetail.autoRefresh();
        }
    }

    @Override
    public void hideProgress() {
        if (currentPage == 1) {
            refreshFragmentProjectDetail.finishRefresh(true);
        } else {
            refreshFragmentProjectDetail.finishLoadMore(true);
        }
        emptyLayout();
    }


    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {
        CommonData data = projectDetailAdapter.getList().get(position);
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(NoteBookConst.INETENT_PARAM_URL, data.getLink());
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, data);
        ActivityUtil.startActivity(getActivity(), intent);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        quryProjectList();
    }


    @Override
    public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked, CommonData item, final int position) {
        buttonView.setChecked(item.isCollect());
//        collectItem = item;
        onCollectionChanged(item, position);
    }

    private void onCollectionChanged(final CommonData item, final int position) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra(NoteBookConst.INETENT_START_TYPE, LoginActivity.START_FROM_RECOMMEND);
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, item);
        if (!item.isCollect()) {
            HttpManager.getInstance().collectInnerArticle(this, String.valueOf(item.getId()), new CommSubscriber<Object>(getActivity(), intent) {

                @Override
                protected void onSucess(CommResponse<Object> objectCommResponse) {
                    if (NoteBookConst.RESPONSE_SUCCESS == objectCommResponse.getErrorCode()) {
                        item.setCollect(true);
                        projectDetailAdapter.notifyItemChanged(position);
                    }
                }
            });
            return;
        }
        HttpManager.getInstance().cancelCollectArticle(this, String.valueOf(item.getId()), new CommSubscriber<Object>(getActivity(), intent) {

            @Override
            protected void onSucess(CommResponse<Object> objectCommResponse) {
                if (NoteBookConst.RESPONSE_SUCCESS == objectCommResponse.getErrorCode()) {
//                    collectItem.setCollect(false);
                    item.setCollect(false);
                    projectDetailAdapter.notifyItemChanged(position);
                }
            }
        });
    }

    @Subscribe
    public void collectionAction(CollectAction collectAction) {
        if (null != collectAction) {
            CommonData commonData = collectAction.getData();
            int position = 0;
            Iterator<CommonData> iterator = projectDetailAdapter.getList().iterator();
            while (iterator.hasNext()) {
                CommonData realData = iterator.next();
                if (CollectionActivity.START_FROM_COLLECTION == collectAction.getType()) {
                    if (String.valueOf(realData.getId()).equals(commonData.getOriginId()) && realData.getTitle().equals(commonData.getTitle())) {
                        commonData = realData;
                        position = projectDetailAdapter.getList().indexOf(realData);
                        break;
                    }
                } else {
                    if (realData.getId() == commonData.getId() && realData.getTitle().equals(commonData.getTitle())) {
                        commonData = realData;
                        position = projectDetailAdapter.getList().indexOf(realData);
                        break;
                    }
                }

            }
            onCollectionChanged(commonData, position);
        }
    }

    @Subscribe
    public void loginAction(LoginAction loginAction) {
        refreshFragmentProjectDetail.autoRefresh();
    }
}
