package com.xygit.note.notebook.main.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerAdapter;
import com.xygit.note.notebook.adapter.BaseRecyclerHolder;
import com.xygit.note.notebook.api.vo.TodoDesc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * todo适配器
 *
 * @author Created by xiuyaun
 * @time on 2019/3/17
 */

public class ToDoAdapter extends BaseRecyclerAdapter<TodoDesc, BaseRecyclerHolder> {
    public static final int DEFUALT_TODO = 0x0010;
    public static final int DEFUALT_DONE = 0x0011;
    private Map<TodoDesc, List<TodoDesc>> todoData;
    private int type = DEFUALT_TODO;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnRemoveClickListener onRemoveClickListener;
    private OnChildClickListener onChildClickListener;

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setOnRemoveClickListener(OnRemoveClickListener onRemoveClickListener) {
        this.onRemoveClickListener = onRemoveClickListener;
    }

    public Map<TodoDesc, List<TodoDesc>> getTodoData() {
        return todoData;
    }

    public void setTodoData(Map<TodoDesc, List<TodoDesc>> todoData) {
        this.todoData = todoData;
        if (todoData != null) {
            setList(new ArrayList<TodoDesc>(this.todoData.keySet()));
        }
        notifyDataSetChanged();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public ToDoAdapter(@Nullable List<TodoDesc> data, RecyclerView adapterView) {
        super(R.layout.item_list_todo, data, adapterView);
    }

    public ToDoAdapter(int layoutResId, @Nullable List<TodoDesc> data, RecyclerView adapterView) {
        super(layoutResId, data, adapterView);
    }

    @Override
    protected void convert(final BaseRecyclerHolder holder, final TodoDesc item, final int position) {
        holder.setText(R.id.tv_title_todo, item.getDateStr());
        RecyclerView todoListView = holder.getView(R.id.rv_inner_todo);
        todoListView.setLayoutManager(new LinearLayoutManager(context));
        todoListView.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
        if (todoData != null) {
            List<TodoDesc> list = todoData.get(item);
            todoListView.setAdapter(new InnerToDoAdapter(list, todoListView, position));
        } else {
            todoListView.setAdapter(new InnerToDoAdapter(null, todoListView, position));
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked, TodoDesc item, int childPosition);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(View view, TodoDesc item, int childPosition);
    }

    public interface OnChildClickListener {
        void onChildClick(View view, TodoDesc item, int childPosition, int parentPosition);
    }

    private class InnerToDoAdapter extends BaseRecyclerAdapter<TodoDesc, BaseRecyclerHolder> {

        private int parentPosition;

        public InnerToDoAdapter(@Nullable List<TodoDesc> data, RecyclerView adapterView, int parentPosition) {
            super(R.layout.item_list_inner_todo, data, adapterView);
            this.parentPosition = parentPosition;
        }

        @Override
        protected void convert(final BaseRecyclerHolder holder, final TodoDesc item, final int position) {
            holder.setText(R.id.tv_title_inner_todo, item.getTitle());
            if (type == DEFUALT_TODO) {
                holder.setText(R.id.tv_detail_inner_todo, item.getContent());
                holder.setText(R.id.tv_change_done_inner, "完成");
            } else {
                holder.setText(R.id.tv_detail_inner_todo, "完成：" + item.getCompleteDateStr());
                holder.setText(R.id.tv_change_done_inner, "还原");
            }
            holder.setOnCheckedChangeListener(R.id.tv_change_done_inner, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (null != onCheckedChangeListener) {
                        onCheckedChangeListener.onCheckedChanged(buttonView, isChecked, item, position);
                    }
                }
            });
            holder.setOnClickListener(R.id.tv_delete_inner_todo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onRemoveClickListener) {
                        onRemoveClickListener.onRemoveClick(v, item, position);
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onChildClickListener) {
                        onChildClickListener.onChildClick(holder.itemView, item, position, parentPosition);
                    }
                }
            });
        }
    }
}
