package com.xygit.note.notebook.main.adapter;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerAdapter;
import com.xygit.note.notebook.adapter.BaseRecyclerHolder;
import com.xygit.note.notebook.api.vo.Children;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/17
 */

public class KnowledgeSystemAdapter extends BaseRecyclerAdapter<Children, BaseRecyclerHolder> {

    private OnTagClickListener onTagClickListener;

    public KnowledgeSystemAdapter(@Nullable List<Children> data, RecyclerView adapterView) {
        super(R.layout.item_list_knowledge_system, data, adapterView);
    }

    public KnowledgeSystemAdapter(int layoutResId, @Nullable List<Children> data, RecyclerView adapterView) {
        super(layoutResId, data, adapterView);
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    @Override
    protected void convert(final BaseRecyclerHolder holder, final Children item, final int position) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.setText(R.id.tv_title_system, item.getName());
        final TagFlowLayout flowLayout = (TagFlowLayout) holder.getView(R.id.fl_tag_system);
        flowLayout.setAdapter(new TagAdapter<Children>(item.getChildren()) {
            @Override
            public View getView(FlowLayout parent, int position, Children children) {
                ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.item_tag_list, null, false);
                TextView textView = constraintLayout.findViewById(R.id.tv_tag_item);
                if (null != children) {
                    textView.setText(children.getName());
                }
                return constraintLayout;
            }
        });
        final int parentPosition = position;
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (onTagClickListener != null) {
                    onTagClickListener.onTagClick(item, parentPosition, position, flowLayout, view);
                }
                return true;
            }
        });
    }


    public interface OnTagClickListener {
        void onTagClick(Children parent, int parentPostion, int childPosition, TagFlowLayout flowLayout, View childView);
    }

    static class TagViewHolder {
        private TextView textView;

        public TagViewHolder(View itemView) {
            textView = itemView.findViewById(R.id.tv_tag_item);
            textView.setText("");
        }
    }
}
