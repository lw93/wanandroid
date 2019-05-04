package com.xygit.note.notebook.main.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.vo.BasePage;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.login.LoginActivity;
import com.xygit.note.notebook.main.adapter.HomeArticleAdapter;
import com.xygit.note.notebook.manager.evenbus.CollectAction;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.web.WebActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 收藏页
 *
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class CollectionActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener, HomeArticleAdapter.OnCheckedChangeListener, BaseRecyclerAdapter.OnItemClickListener, HttpCallBack {
    public static final int START_FROM_COLLECTION = 0x008;
    private Toolbar toolbar;
    private RecyclerView rvColllection;
    private SmartRefreshLayout refreshCollection;
    private View emptyLayout;
    private HomeArticleAdapter articleAdapter;
    private int currentPage = 0;
//    private CommonData collectItem;


    @Override
    public int layoutId() {
        return R.layout.activity_collection;
    }


    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvColllection = (RecyclerView) findViewById(R.id.rv_collect);
        refreshCollection = (SmartRefreshLayout) findViewById(R.id.refresh_collect);
        emptyLayout = findViewById(R.id.cl_container_layout_empty);
    }

    @Override
    public void initAction() {
        emptyLayout.setVisibility(View.GONE);
        emptyLayout.setOnClickListener(this);
        refreshCollection.setOnRefreshListener(this);
        refreshCollection.setOnLoadMoreListener(this);
        refreshCollection.setRefreshHeader(new ClassicsHeader(refreshCollection.getContext()));
        articleAdapter = new HomeArticleAdapter(null, rvColllection);
        articleAdapter.setOnCheckedChangeListener(this);
        articleAdapter.setOnItemClickListener(this);
        rvColllection.setLayoutManager(new LinearLayoutManager(this));
        rvColllection.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvColllection.setAdapter(articleAdapter);
//        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("收藏");
        refreshCollection.autoRefresh();
    }

    @Override
    public void clearData() {
        if (articleAdapter != null) {
            articleAdapter.clear();
            articleAdapter = null;
        }
//        if (collectItem != null) {
//            collectItem = null;
//        }
//        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_container_layout_empty) {
            emptyLayout.setVisibility(View.GONE);
            refreshCollection.autoRefresh();
        }
    }

    private void emptyLayout() {
        if (null != articleAdapter) {
            if (articleAdapter.getItemCount() < 1) {
                emptyLayout.setVisibility(View.VISIBLE);
                return;
            }
            emptyLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 0;
        queryCollections();
    }

    private void queryCollections() {
        HttpManager.getInstance().quryCollectArticles(this, String.valueOf(currentPage), new CommSubscriber<BasePage<CommonData>>(this) {

            @Override
            protected void onSucess(CommResponse<BasePage<CommonData>> commonDataCommResponse) {
                if (commonDataCommResponse != null) {
                    BasePage<CommonData> dataBasePage = commonDataCommResponse.getData();
                    if (dataBasePage != null) {
                        if (currentPage == 0) {
                            articleAdapter.clear();
                        }
                        initCollects(dataBasePage.getDatas());
                    }
                }
            }
        });
    }

    private void initCollects(List<CommonData> datas) {
        for (CommonData commonData : datas) {
            if (!commonData.isCollect()) {
                commonData.setCollect(true);
            }
        }
        articleAdapter.addItems(datas);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        queryCollections();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, CommonData item, int postion) {
        CollectAction collectAction = new CollectAction();
        collectAction.setData(item);
        collectAction.setType(CollectionActivity.START_FROM_COLLECTION);
        EventBus.getDefault().post(collectAction);
        buttonView.setChecked(item.isCollect());
//        collectItem = item;
        onCollectionChanged(item);
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {
        CommonData data = articleAdapter.getList().get(position);
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(NoteBookConst.INETENT_PARAM_URL, data.getLink());
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, data);
        ActivityUtil.startActivity(this, intent);
    }

    private void onCollectionChanged(final CommonData item) {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(NoteBookConst.INETENT_START_TYPE, CollectionActivity.START_FROM_COLLECTION);
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, item);
        HttpManager.getInstance().cancelMyCollectArticle(this, String.valueOf(item.getId()), String.valueOf(item.getOriginId()),
                new CommSubscriber<Object>(this, intent) {

                    @Override
                    protected void onSucess(CommResponse<Object> objectCommResponse) {
                        if (NoteBookConst.RESPONSE_SUCCESS == objectCommResponse.getErrorCode()) {
//                            collectItem.setCollect(false);
                            item.setCollect(false);
                            articleAdapter.removeItem(articleAdapter.getList().indexOf(item));
                        }
                    }
                });
    }

    @Override
    public void showProgress() {
        if (0 == currentPage) {
            if (refreshCollection.getState() != RefreshState.Refreshing) {
                refreshCollection.autoRefresh();
            }
        } else {
            if (refreshCollection.getState() != RefreshState.Loading) {
                refreshCollection.autoLoadMore();
            }
        }
    }

    @Override
    public void hideProgress() {
        if (0 == currentPage) {
            refreshCollection.finishRefresh(true);
        } else {
            refreshCollection.finishLoadMore(true);
        }
        emptyLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLayout();
            }
        }, 2000);
    }

//    @Subscribe
//    public void collectionAction(CollectAction collectAction) {
//        if (null != collectAction) {
//            CommonData commonData = collectAction.getData();
//            onCollectionChanged(commonData);
//        }
//    }
}
