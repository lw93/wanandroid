package com.xygit.note.notebook.main.activity;

import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.vo.SimpleData;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.constant.PreferencesConst;
import com.xygit.note.notebook.manager.gson.GsonManager;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.PreferencesUtil;
import com.xygit.note.notebook.view.ProgressLoading;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 搜索页
 *
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener, HttpCallBack, SearchView.OnCloseListener, SearchView.OnQueryTextListener, TagFlowLayout.OnTagClickListener {
    private SearchView searchView;
    private Toolbar toolbar;
    private TextView tvSearchHistory;
    private TextView tvClearHistory;
    private TagFlowLayout tgSearchHistory;
    private Group groupSearch;
    private TextView tvHotSearch;
    private TagFlowLayout tgHotSearch;

    private View emptyLayout;
    private int startType;
    private boolean searchHasOpen;
    private TagAdapter<SimpleData> historyTagAdapter;
    private TagAdapter<SimpleData> hotTagAdapter;
    private ProgressLoading loading;
    private ArrayList<SimpleData> cacheHistoryTags;
    private Gson gson;
    private Set<SimpleData> tmpData;

    @Override
    public int layoutId() {
        return R.layout.activity_search;
    }


    @Override
    public void initView() {
        searchView = (SearchView) findViewById(R.id.search_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvSearchHistory = (TextView) findViewById(R.id.tv_search_history);
        tvClearHistory = (TextView) findViewById(R.id.tv_clear_history);
        tgSearchHistory = (TagFlowLayout) findViewById(R.id.tg_search_history);
        groupSearch = (Group) findViewById(R.id.group_search);
        tvHotSearch = (TextView) findViewById(R.id.tv_hot_search);
        tgHotSearch = (TagFlowLayout) findViewById(R.id.tg_hot_search);
        emptyLayout = findViewById(R.id.cl_container_layout_empty);
        Class searchViewClass = searchView.getClass();
        Field searchTextField = null;
        try {
            searchTextField = searchViewClass.getDeclaredField("mSearchSrcTextView");
            searchTextField.setAccessible(true);
            TextView tv = (TextView) searchTextField.get(searchView);
            Class autoText = searchTextField.getType();
            Class textViewClass = autoText.getSuperclass().getSuperclass().getSuperclass().getSuperclass();
            //光标颜色
            Field mCursorDrawableRes = textViewClass.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(tv, R.drawable.cursor_color);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initAction() {
        tvClearHistory.setOnClickListener(this);
        tgSearchHistory.setOnTagClickListener(this);
        tgHotSearch.setOnTagClickListener(this);
        emptyLayout.setOnClickListener(this);
        searchView.setOnSearchClickListener(this);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("搜索");
        Intent intent = getIntent();
        if (null != intent) {
            startType = intent.getIntExtra(NoteBookConst.INETENT_START_TYPE, 0);
        }
        loading = new ProgressLoading(this);
        String cacheTags = PreferencesUtil.getPreference(PreferencesConst.SEARCH_TAG_HISTORY, "");
        gson = GsonManager.getInstance().build();
        cacheHistoryTags = gson.fromJson(cacheTags, new TypeToken<List<SimpleData>>() {
        }.getType());
        if (null == cacheHistoryTags) {
            groupSearch.setVisibility(View.GONE);
        } else {
            setHistoryAdapter(cacheHistoryTags);
        }
        tmpData = new HashSet<>(7);
        queryHotData();
    }

    private void setHistoryAdapter(List<SimpleData> historyDatas) {
        historyTagAdapter = new TagAdapter<SimpleData>(historyDatas) {
            @Override
            public View getView(FlowLayout parent, int position, SimpleData simpleData) {
                ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_tag_list, null, false);
                TextView textView = constraintLayout.findViewById(R.id.tv_tag_item);
                if (null != simpleData) {
                    textView.setText(simpleData.getName());
                }
                return constraintLayout;
            }
        };
        tgSearchHistory.setAdapter(historyTagAdapter);
        if (historyTagAdapter.getCount() > 0) {
            groupSearch.setVisibility(View.VISIBLE);
        } else {
            groupSearch.setVisibility(View.GONE);
        }
    }

    private void setHotAdapter(List<SimpleData> hotDatas) {
        hotTagAdapter = new TagAdapter<SimpleData>(hotDatas) {
            @Override
            public View getView(FlowLayout parent, int position, SimpleData simpleData) {
                ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_tag_list, null, false);
                TextView textView = constraintLayout.findViewById(R.id.tv_tag_item);
                if (null != simpleData) {
                    textView.setText(simpleData.getName());
                }
                return constraintLayout;
            }
        };
        tgHotSearch.setAdapter(hotTagAdapter);
        emptyLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String cacheTags = gson.toJson(cacheHistoryTags);
        PreferencesUtil.addPreference(PreferencesConst.SEARCH_TAG_HISTORY, cacheTags);
    }

    @Override
    public void clearData() {
        if (historyTagAdapter != null) {
            historyTagAdapter = null;
        }
        if (hotTagAdapter != null) {
            hotTagAdapter = null;
        }
        if (null != cacheHistoryTags) {
            cacheHistoryTags.clear();
            cacheHistoryTags = null;
        }
        if (tmpData != null) {
            tmpData.clear();
            tmpData = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (searchHasOpen) {
                searchView.onActionViewCollapsed();
                toolbar.setTitle("搜索");
                return true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                supportFinishAfterTransition();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void toSearch(SimpleData simpleData, View view) {
        Intent intent = new Intent(this, SearchDetailActivity.class);
        intent.putExtra(NoteBookConst.INETENT_START_TYPE, SearchDetailActivity.START_FROM_SEARCH);
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, simpleData);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view, "tagTextView");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_container_layout_empty) {
            emptyLayout.setVisibility(View.GONE);
            queryHotData();
        } else if (id == R.id.tv_clear_history) {
            if (null != cacheHistoryTags) {
                cacheHistoryTags.clear();
            }
            setHistoryAdapter(cacheHistoryTags);
            groupSearch.setVisibility(View.GONE);
        } else {
            toolbar.setTitle("");
            searchHasOpen = true;
        }
    }


    private void queryHotData() {
        HttpManager.getInstance().quryHotKeys(this,new CommSubscriber<List<SimpleData>>(this) {
            @Override
            protected void onSucess(CommResponse<List<SimpleData>> basePageCommResponse) {
                if (null != basePageCommResponse) {
                    List<SimpleData> hotDatas = basePageCommResponse.getData();
                    setHotAdapter(hotDatas);
                }
            }
        });
    }

    private void emptyLayout() {
        if (null != hotTagAdapter && hotTagAdapter.getCount() > 0) {
            emptyLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void showProgress() {
        if (!loading.isShowing()) {
            loading.show();
        }
    }

    @Override
    public void hideProgress() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
    }

    @Override
    public boolean onClose() {
        toolbar.setTitle("搜索");
        searchHasOpen = false;
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        SimpleData customTag = new SimpleData();
        customTag.setName(query);
        addToCacheHistory(customTag);
        toSearch(customTag, null);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        if (parent == tgHotSearch) {
            if (hotTagAdapter != null) {
                SimpleData simpleData = hotTagAdapter.getItem(position);
                addToCacheHistory(simpleData);
                toSearch(simpleData, view);
            }
        } else {
            if (null != historyTagAdapter && historyTagAdapter.getCount() > 0) {
                SimpleData simpleData = historyTagAdapter.getItem(position);
                toSearch(simpleData, view);
            }
        }
        return true;
    }

    private void addToCacheHistory(SimpleData simpleData) {
        if (null == cacheHistoryTags) {
            cacheHistoryTags = new ArrayList<>(7);
        }
        tmpData.add(simpleData);
        tmpData.addAll(cacheHistoryTags);
        cacheHistoryTags = new ArrayList<>(tmpData);
        Collections.swap(cacheHistoryTags, 0, cacheHistoryTags.indexOf(simpleData));
        setHistoryAdapter(cacheHistoryTags);
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
