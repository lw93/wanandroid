package com.xygit.note.notebook.main.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.xygit.note.notebook.api.vo.Children;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.api.vo.SimpleData;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.login.LoginActivity;
import com.xygit.note.notebook.main.adapter.HomeArticleAdapter;
import com.xygit.note.notebook.main.fragment.SeniorFragment;
import com.xygit.note.notebook.manager.evenbus.CollectAction;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.web.WebActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 搜索页
 *
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class SearchDetailActivity extends BaseActivity implements View.OnClickListener, HttpCallBack, OnRefreshListener, OnLoadMoreListener, HomeArticleAdapter.OnCheckedChangeListener, BaseRecyclerAdapter.OnItemClickListener {
    public static final int START_FROM_SEARCH = 0x004;
    public static final int START_FROM_SEARCH_DEATAIL = 0x005;
    private Toolbar toolbar;
    private View emptyLayout;
    private int currentPage = 0;
    private HomeArticleAdapter articleAdapter;
    private int startType;
    private SimpleData searchData;
    private Children children;
    private RecyclerView rvSearchDetail;
    private SmartRefreshLayout refreshSearchDetail;
    private CommonData collectItem;

    @Override
    public int layoutId() {
        return R.layout.activity_search_detail;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvSearchDetail = (RecyclerView) findViewById(R.id.rv_search_detail);
        refreshSearchDetail = (SmartRefreshLayout) findViewById(R.id.refresh_search_detail);
        emptyLayout = findViewById(R.id.cl_container_layout_empty);
    }

    @Override
    public void initAction() {
        emptyLayout.setVisibility(View.GONE);
        emptyLayout.setOnClickListener(this);
        refreshSearchDetail.setOnRefreshListener(this);
        refreshSearchDetail.setOnLoadMoreListener(this);
        refreshSearchDetail.setRefreshHeader(new ClassicsHeader(refreshSearchDetail.getContext()));
        articleAdapter = new HomeArticleAdapter(null, rvSearchDetail);
        articleAdapter.setOnCheckedChangeListener(this);
        articleAdapter.setOnItemClickListener(this);
        rvSearchDetail.setLayoutManager(new LinearLayoutManager(this));
        rvSearchDetail.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvSearchDetail.setAdapter(articleAdapter);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("搜索");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTransitionName("tagTextView");
        }
        Intent intent = getIntent();
        if (null != intent) {
            startType = intent.getIntExtra(NoteBookConst.INETENT_START_TYPE, 0);
            searchData = (SimpleData) intent.getSerializableExtra(NoteBookConst.INETENT_PARAM_DATA);
            children = (Children) intent.getSerializableExtra(NoteBookConst.INETENT_PARAM_ENTITY);
            setTitleByStartType();
        }
        refreshSearchDetail.autoRefresh();
    }

    private void setTitleByStartType() {
        if (startType == START_FROM_SEARCH) {
            if (searchData != null) {
                toolbar.setTitle(searchData.getName());
            }
        } else if (startType == SeniorFragment.START_FROM_SENIOR) {
            if (children != null) {
                toolbar.setTitle(children.getName());
            }
        }
    }

    private String prepareParam() {
        if (startType == START_FROM_SEARCH) {
            if (searchData != null) {
                return searchData.getName();
            }
        } else if (startType == SeniorFragment.START_FROM_SENIOR) {
            if (children != null) {
                return children.getName();
            }
        }
        return null;
    }

    private void queryData() {
        String searchKey = prepareParam();
        if (TextUtils.isEmpty(searchKey)) {
            emptyLayout();
            return;
        }
        HttpManager.getInstance().quryArticleByKey(this,String.valueOf(currentPage), searchKey, new CommSubscriber<BasePage<CommonData>>(this) {
            @Override
            protected void onSucess(CommResponse<BasePage<CommonData>> basePageCommResponse) {
                if (basePageCommResponse != null) {
                    BasePage<CommonData> dataBasePage = basePageCommResponse.getData();
                    if (dataBasePage != null) {
                        if (currentPage == 0) {
                            articleAdapter.clear();
                        }
                        articleAdapter.addItems(dataBasePage.getDatas());
                    }
                }
            }
        });
    }


    @Override
    public void clearData() {
        if (articleAdapter != null) {
            articleAdapter.clear();
            articleAdapter = null;
        }
        if (collectItem != null) {
            collectItem = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                supportFinishAfterTransition();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_container_layout_empty) {
            emptyLayout.setVisibility(View.GONE);
            refreshSearchDetail.autoRefresh();
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
    public void showProgress() {
        if (0 == currentPage) {
            if (refreshSearchDetail.getState() != RefreshState.Refreshing) {
                refreshSearchDetail.autoRefresh();
            }
        } else {
            if (refreshSearchDetail.getState() != RefreshState.Loading) {
                refreshSearchDetail.autoLoadMore();
            }
        }
    }

    @Override
    public void hideProgress() {
        if (0 == currentPage) {
            refreshSearchDetail.finishRefresh(true);
        } else {
            refreshSearchDetail.finishLoadMore(true);
        }
        emptyLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLayout();
            }
        }, 2000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 0;
        queryData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        queryData();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, CommonData item, int postion) {
        buttonView.setChecked(item.isCollect());
        collectItem = item;
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

    private void onCollectionChanged(CommonData item) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(NoteBookConst.INETENT_START_TYPE, SearchDetailActivity.START_FROM_SEARCH_DEATAIL);
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, item);
        if (!item.isCollect()) {
            HttpManager.getInstance().collectInnerArticle(this,String.valueOf(item.getId()), new CommSubscriber<Object>(this, intent) {

                @Override
                protected void onSucess(CommResponse<Object> objectCommResponse) {
                    if (NoteBookConst.RESPONSE_SUCCESS == objectCommResponse.getErrorCode()) {
                        collectItem.setCollect(true);
                        articleAdapter.notifyItemChanged(articleAdapter.getList().indexOf(collectItem));
                    }
                }
            });
            return;
        }
        HttpManager.getInstance().cancelCollectArticle(this,String.valueOf(item.getId()), new CommSubscriber<Object>(this, intent) {

            @Override
            protected void onSucess(CommResponse<Object> objectCommResponse) {
                if (NoteBookConst.RESPONSE_SUCCESS == objectCommResponse.getErrorCode()) {
                    collectItem.setCollect(false);
                    articleAdapter.notifyItemChanged(articleAdapter.getList().indexOf(collectItem));
                }
            }
        });
    }

    @Subscribe
    public void collectionAction(CollectAction collectAction) {
        if (null != collectAction) {
            CommonData commonData = collectAction.getData();
            onCollectionChanged(commonData);
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
