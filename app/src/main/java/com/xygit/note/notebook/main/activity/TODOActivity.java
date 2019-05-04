package com.xygit.note.notebook.main.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.vo.BasePage;
import com.xygit.note.notebook.api.vo.TodoDesc;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.main.adapter.ToDoAdapter;
import com.xygit.note.notebook.manager.evenbus.TodoAction;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.other.TodoDescComparator;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * TODO页
 *
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class TODOActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener, HttpCallBack,
        ToDoAdapter.OnRemoveClickListener, ToDoAdapter.OnCheckedChangeListener, ToDoAdapter.OnChildClickListener {
    private static final int PAGENUM = 1;
    private Toolbar toolbar;
    private RecyclerView rvTodo;
    private SmartRefreshLayout refreshTodo;
    private TextView emptyLayout;
    private TextView tvTodo;
    private TextView tvDone;
    private int currentPage = PAGENUM;
    private ToDoAdapter toDoAdapter;
    private FloatingActionButton btnFloating;

    @Override
    public int layoutId() {
        return R.layout.activity_todo;
    }


    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvTodo = (RecyclerView) findViewById(R.id.rv_todo);
        refreshTodo = (SmartRefreshLayout) findViewById(R.id.refresh_todo);
        emptyLayout = (TextView) findViewById(R.id.cl_container_layout_empty);
        tvTodo = (TextView) findViewById(R.id.tv_todo);
        tvDone = (TextView) findViewById(R.id.tv_done);
        btnFloating = (FloatingActionButton) findViewById(R.id.fb_add_todo);
    }

    @Override
    public void initAction() {
        emptyLayout.setVisibility(View.GONE);
        emptyLayout.setOnClickListener(this);
        refreshTodo.setOnRefreshListener(this);
        refreshTodo.setOnLoadMoreListener(this);
        refreshTodo.setRefreshHeader(new ClassicsHeader(refreshTodo.getContext()));
        toDoAdapter = new ToDoAdapter(null, rvTodo);
        toDoAdapter.setOnCheckedChangeListener(this);
        toDoAdapter.setOnChildClickListener(this);
        toDoAdapter.setOnRemoveClickListener(this);
        tvTodo.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        btnFloating.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("TODO");
        rvTodo.setLayoutManager(new LinearLayoutManager(this));
        rvTodo.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvTodo.setAdapter(toDoAdapter);
        refreshTodo.autoRefresh();
    }

    @Override
    public void clearData() {
        if (toDoAdapter != null) {
            toDoAdapter.setTodoData(null);
            toDoAdapter = null;
        }
        EventBus.getDefault().unregister(this);
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
            refreshTodo.autoRefresh();
        } else if (R.id.fb_add_todo == id) {
            Intent intent = new Intent(this, UpdateTODOActivity.class);
            intent.putExtra(NoteBookConst.INETENT_START_TYPE, UpdateTODOActivity.START_TYPE_CREATE);
            ActivityUtil.startActivity(this, intent);
        } else {
            setBottomSelected(id);
        }
    }

    private void emptyLayout() {
        if (null != toDoAdapter) {
            if (toDoAdapter.getItemCount() < PAGENUM) {
                emptyLayout.setVisibility(View.VISIBLE);
                return;
            }
            emptyLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    private void queryToDoList() {
        if (toDoAdapter.getType() == ToDoAdapter.DEFUALT_TODO) {
            HttpManager.getInstance().quryTODOList(this, String.valueOf(currentPage), NoteBookConst.TODO_STATUS_NOT_DONE, null, null, null, new CommSubscriber<BasePage<TodoDesc>>(this) {
                @Override
                protected void onSucess(CommResponse<BasePage<TodoDesc>> basePageCommResponse) {
                    if (basePageCommResponse != null) {
                        BasePage<TodoDesc> todoDescBasePage = basePageCommResponse.getData();
                        if (todoDescBasePage != null) {
                            if (currentPage == PAGENUM) {
                                toDoAdapter.setTodoData(null);
                            }
                            addTODO(todoDescBasePage.getDatas());
                        }
                    }
                }
            });
            return;
        }
        HttpManager.getInstance().quryTODOList(this, String.valueOf(currentPage), NoteBookConst.TODO_STATUS_DONE, null, null, null, new CommSubscriber<BasePage<TodoDesc>>(this) {
            @Override
            protected void onSucess(CommResponse<BasePage<TodoDesc>> basePageCommResponse) {
                if (basePageCommResponse != null) {
                    BasePage<TodoDesc> todoDescBasePage = basePageCommResponse.getData();
                    if (todoDescBasePage != null) {
                        if (currentPage == PAGENUM) {
                            toDoAdapter.setTodoData(null);
                        }
                        addTODO(todoDescBasePage.getDatas());
                    }
                }
            }
        });
    }

    private void addTODO(List<TodoDesc> datas) {
        Map<TodoDesc, List<TodoDesc>> mapData = toDoAdapter.getTodoData();
        if (mapData == null) {
            mapData = new TreeMap<>(new TodoDescComparator());
        }
        List<TodoDesc> currentData;
        for (TodoDesc todoDesc : datas) {
//            String dateStr = todoDesc.getDateStr();
            if (mapData.containsKey(todoDesc)) {
                currentData = mapData.get(todoDesc);
            } else {
                currentData = new ArrayList<>(7);
            }
            currentData.add(todoDesc);
            mapData.put(todoDesc, currentData);
        }
//        List<TodoDesc> sortKeys = new ArrayList<>(mapData.keySet());
//        Collections.sort(sortKeys, new TodoDescComparator());
//        for (TodoDesc todoDesc : sortKeys) {
//            mapData.put(todoDesc, mapData.get(todoDesc));
//        }
//        Map<TodoDesc, List<TodoDesc>> sortedMap = new TreeMap<TodoDesc, List<TodoDesc>>(new TodoDescComparator());
//        sortedMap.putAll(mapData);
        toDoAdapter.setTodoData(mapData);
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = PAGENUM;
        queryToDoList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        queryToDoList();
    }

    @Override
    public void showProgress() {
        if (PAGENUM == currentPage) {
            if (refreshTodo.getState() != RefreshState.Refreshing) {
                refreshTodo.autoRefresh();
            }
        } else {
            if (refreshTodo.getState() != RefreshState.Loading) {
                refreshTodo.autoLoadMore();
            }
        }
    }

    @Override
    public void hideProgress() {
        if (PAGENUM == currentPage) {
            refreshTodo.finishRefresh(true);
        } else {
            refreshTodo.finishLoadMore(true);
        }
        emptyLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLayout();
            }
        }, 1500);
    }

    public void setBottomSelected(int bottomSelected) {
        RefreshState refreshState = refreshTodo.getState();
        if (refreshState == RefreshState.Refreshing || refreshState == RefreshState.Loading
                || refreshState == RefreshState.RefreshFinish || refreshState == RefreshState.LoadFinish) {
            return;
        }
        if (bottomSelected == R.id.tv_todo) {
            tvTodo.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvTodo.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_event_note_primary_24dp), null, null);
            tvDone.setTextColor(getResources().getColor(R.color.divider_color));
            tvDone.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_event_available_grey_24dp), null, null);
            btnFloating.setVisibility(View.VISIBLE);
            changeToDoStatus(ToDoAdapter.DEFUALT_TODO);
        } else {
            tvTodo.setTextColor(getResources().getColor(R.color.divider_color));
            tvTodo.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_event_note_grey_24dp), null, null);
            tvDone.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvDone.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_event_available_primary_24dp), null, null);
            btnFloating.setVisibility(View.GONE);
            changeToDoStatus(ToDoAdapter.DEFUALT_DONE);
        }
    }

    private void changeToDoStatus(int defualtTodo) {
        currentPage = PAGENUM;
        toDoAdapter.clear();
        toDoAdapter.setType(defualtTodo);
        refreshTodo.autoRefresh();
    }

    @Override
    public void onRemoveClick(View view, TodoDesc item, int childPosition) {
        removeDataFromList(item, false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, TodoDesc item, int childPosition) {
        changeToDoStatus(item);
    }

    private void changeToDoStatus(TodoDesc item) {
        HttpManager.getInstance().updateTODOStatus(this, String.valueOf(item.getId()),
                0 == item.getStatus() ? NoteBookConst.TODO_STATUS_DONE : NoteBookConst.TODO_STATUS_NOT_DONE,
                new CommSubscriber<TodoDesc>(this) {
                    @Override
                    protected void onSucess(CommResponse<TodoDesc> todoDescCommResponse) {
                        if (todoDescCommResponse != null) {
                            TodoDesc data = todoDescCommResponse.getData();
                            removeDataFromList(data, true);
                        }
                    }
                });
    }

    private void removeDataFromList(final TodoDesc data, boolean deleteFromLocal) {
        final Map<TodoDesc, List<TodoDesc>> map = toDoAdapter.getTodoData();
        if (map != null) {
            if (map.containsKey(data)) {
                if (deleteFromLocal) {
                    List<TodoDesc> childList = map.get(data);
                    childList.remove(data);
                    if (childList.size() < 1) {
                        map.remove(data);
                    }
                    toDoAdapter.notifyDataSetChanged();
                    return;
                }
                HttpManager.getInstance().deleteTODO(this, String.valueOf(data.getId()), new CommSubscriber<Object>(this) {
                    @Override
                    protected void onSucess(CommResponse<Object> objectCommResponse) {
                        List<TodoDesc> childList = map.get(data);
                        childList.remove(data);
                        if (childList.size() < 1) {
                            map.remove(data);
                        }
                        toDoAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public void onChildClick(View view, TodoDesc item, int childPosition, int parentPosition) {
        Intent intent = new Intent(this, UpdateTODOActivity.class);
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, item);
        intent.putExtra(NoteBookConst.INETENT_START_TYPE, UpdateTODOActivity.START_TYPE_UPDATE);
        ActivityUtil.startActivity(this, intent);
    }

    @Subscribe
    public void updateToDoAction(TodoAction todoAction) {
        if (todoAction != null) {
            if (UpdateTODOActivity.START_TYPE_CREATE == todoAction.getType()) {
                addTODO(new ArrayList<TodoDesc>(Collections.singletonList(todoAction.getData())));
            }
        }
    }
}
