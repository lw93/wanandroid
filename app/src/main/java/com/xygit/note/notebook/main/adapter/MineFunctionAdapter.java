package com.xygit.note.notebook.main.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerAdapter;
import com.xygit.note.notebook.adapter.BaseRecyclerHolder;
import com.xygit.note.notebook.entity.MineFunction;

import java.util.List;

/**
 * @author Created by xiuyaun
 * @time on 2019/4/9
 */

public class MineFunctionAdapter extends BaseRecyclerAdapter<MineFunction, BaseRecyclerHolder> {

    public MineFunctionAdapter(int layoutResId, @Nullable List<MineFunction> data, RecyclerView adapterView) {
        super(layoutResId, data, adapterView);
    }

    public MineFunctionAdapter(@Nullable List<MineFunction> data, RecyclerView adapterView) {
        this(R.layout.item_fuction_list_mine, data, adapterView);
    }

    @Override
    protected void convert(final BaseRecyclerHolder holder, MineFunction item, final int position) {
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(MineFunctionAdapter.this, holder.itemView, position);
                }
            });
        }
        holder.setImageResource(R.id.iv_function_icon, item.getDrawbleRes());
        holder.setText(R.id.tv_function_name, item.getTitle());
    }
}
