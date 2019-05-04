package com.xygit.note.notebook.main.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.login.LoginActivity;
import com.xygit.note.notebook.main.adapter.HomeArticleAdapter;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.web.WebActivity;

/**
 * 详情页
 *
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class KnowledgeSystemDetailActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener,
        OnLoadMoreListener, HomeArticleAdapter.OnCheckedChangeListener, BaseRecyclerAdapter.OnItemClickListener, HttpCallBack {

    public static final int START_FROM_KNOWLEDGE_SYSTEM = 0x003;
    private TextView tvTitle;
    private Toolbar toolbar;
    private RecyclerView rvKnowledgeDetail;
    private SmartRefreshLayout refreshKnowledgeDetail;
    private View emptyLayout;
    private int currentPage = 0;
    private HomeArticleAdapter articleAdapter;
    private Children children;
    private int startType;
    private CommonData collectItem;

    @Override
    public int layoutId() {
        return R.layout.activity_knowledge_system_detail;
    }


    @Override
    public void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvKnowledgeDetail = (RecyclerView) findViewById(R.id.rv_knowledge_detail);
        refreshKnowledgeDetail = (SmartRefreshLayout) findViewById(R.id.refresh_knowledge_detail);
        emptyLayout = findViewById(R.id.cl_container_layout_empty);
    }

    @Override
    public void initAction() {
        emptyLayout.setOnClickListener(this);
        refreshKnowledgeDetail.setRefreshHeader(new ClassicsHeader(refreshKnowledgeDetail.getContext()));
        refreshKnowledgeDetail.setOnRefreshListener(this);
        refreshKnowledgeDetail.setOnLoadMoreListener(this);
        articleAdapter = new HomeArticleAdapter(null, rvKnowledgeDetail);
        articleAdapter.setOnCheckedChangeListener(this);
        articleAdapter.setOnItemClickListener(this);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTransitionName("tagTextView");
        }
        Intent intent = getIntent();
        if (null != intent) {
            startType = intent.getIntExtra(NoteBookConst.INETENT_START_TYPE, 0);
            children = (Children) intent.getSerializableExtra(NoteBookConst.INETENT_PARAM_DATA);
            if (children != null) {
                toolbar.setTitle(children.getName());
            }
        }
        emptyLayout.setVisibility(View.GONE);
        rvKnowledgeDetail.setLayoutManager(new LinearLayoutManager(this));
        rvKnowledgeDetail.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvKnowledgeDetail.setAdapter(articleAdapter);
        refreshKnowledgeDetail.setEnableRefresh(true);//启用刷新
        refreshKnowledgeDetail.setEnableLoadMore(true);//启用加载
        refreshKnowledgeDetail.autoRefresh();
    }

    @Override
    public void clearData() {
        if (articleAdapter != null) {
            articleAdapter.clear();
            articleAdapter = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_tb, menu);
        return super.onCreateOptionsMenu(menu);
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
        } else {
            toSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toSearch() {
        ActivityUtil.startActivity(this, new Intent(this, SearchActivity.class));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_container_layout_empty) {
            emptyLayout.setVisibility(View.GONE);
            refreshKnowledgeDetail.autoRefresh();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 0;
        queryData();
    }

    private void queryData() {
        if (children == null) return;
        HttpManager.getInstance().quryKnowledgeTreeDatas(this,String.valueOf(currentPage), String.valueOf(children.getId()), new CommSubscriber<BasePage<CommonData>>(this) {
            @Override
            protected void onSucess(CommResponse<BasePage<CommonData>> basePageCommResponse) {
                if (null != basePageCommResponse) {
                    BasePage<CommonData> dataBasePage = basePageCommResponse.getData();
                    if (null != dataBasePage) {
                        if (currentPage == 0) {
                            articleAdapter.clear();
                        }
                        articleAdapter.addItems(dataBasePage.getDatas());
                    }
                }
                refreshKnowledgeDetail.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyLayout();
                    }
                }, 2000);
            }
        });
    }

    private void emptyLayout() {
        if (articleAdapter.getItemCount() < 1) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.GONE);
        }
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

    private void onCollectionChanged(CommonData item) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(NoteBookConst.INETENT_START_TYPE, KnowledgeSystemDetailActivity.START_FROM_KNOWLEDGE_SYSTEM);
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

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {
        CommonData data = articleAdapter.getList().get(position);
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(NoteBookConst.INETENT_PARAM_URL, data.getLink());
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, data);
        ActivityUtil.startActivity(this, intent);
    }

    @Override
    public void showProgress() {
        if (0 == currentPage) {
            if (refreshKnowledgeDetail.getState() != RefreshState.Refreshing) {
                refreshKnowledgeDetail.autoRefresh();
            }
        } else {
            if (refreshKnowledgeDetail.getState() != RefreshState.Loading) {
                refreshKnowledgeDetail.autoLoadMore();
            }
        }
    }

    @Override
    public void hideProgress() {
        if (0 == currentPage) {
            refreshKnowledgeDetail.finishRefresh(true);
        } else {
            refreshKnowledgeDetail.finishLoadMore(true);
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
