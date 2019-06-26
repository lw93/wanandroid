package com.xygit.note.notebook.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerHolder;
import com.xygit.note.notebook.api.vo.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/17
 */

public class HomeBannerAdapter extends RecyclerView.Adapter<BaseRecyclerHolder> {
    private Context context;
    private List<Banner> mList;
    private OnBannerItemClickListener mOnItemClickListener;

    public List<Banner> getList() {
        return mList;
    }

    public void setOnItemClickListener(@Nullable OnBannerItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public HomeBannerAdapter(@Nullable List<Banner> data, RecyclerView adapterView) {
        this.context = adapterView.getContext();
        this.mList = data == null ? new ArrayList<Banner>() : data;
    }

    protected void convert(final BaseRecyclerHolder holder, final Banner item, final int position) {
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onBannerItemClick(HomeBannerAdapter.this, holder.itemView, position);
                }
            });
        }
        ImageView imageView = holder.getView(R.id.iv_banner_item);
        Glide.with(context)
                .load(item.getImagePath())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_holder_empty)
                .into(imageView);
        TextView titleView = holder.getView(R.id.tv_banner_item);
        String title = item.getTitle();
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        } else {
            titleView.setVisibility(View.GONE);
        }
        titleView.setVisibility(View.GONE);

    }

    @NonNull
    @Override
    public BaseRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseRecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerHolder holder, int position) {
        int realPosition = position % mList.size();
        Banner bean = mList.get(realPosition);
        convert(holder, bean, position);
    }

    @Override
    public int getItemCount() {
        if (mList.size() > 0) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setList(List<Banner> noteBookBanner) {
        if (noteBookBanner == null) {
            clear();
        } else {
            mList = noteBookBanner;
            notifyDataSetChanged();
        }
    }

    public interface OnBannerItemClickListener {
        void onBannerItemClick(RecyclerView.Adapter adapter, View view, int position);
    }

}
