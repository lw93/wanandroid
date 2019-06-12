package com.xygit.note.notebook.main.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerAdapter;
import com.xygit.note.notebook.adapter.BaseRecyclerHolder;
import com.xygit.note.notebook.adv.MiAdvType;
import com.xygit.note.notebook.api.vo.CommonData;

import java.util.List;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/17
 */

public class HomeArticleAdapter extends BaseRecyclerAdapter<CommonData, BaseRecyclerHolder> {

    private OnCheckedChangeListener onCheckedChangeListener;
    private IAdWorker advInfoList;
    private int mAdSize;
    private int showedAdSize;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public HomeArticleAdapter(@Nullable List<CommonData> data, RecyclerView adapterView) {
        super(R.layout.item_list_comm, data, adapterView);
    }

    public HomeArticleAdapter(int layoutResId, @Nullable List<CommonData> data, RecyclerView adapterView) {
        super(layoutResId, data, adapterView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        initAdv(recyclerView);
    }


    private void initAdv(RecyclerView adapterView) {
        try {
            advInfoList = AdWorkerFactory.getAdWorker(adapterView.getContext(), null, new MimoAdListener() {
                @Override
                public void onAdPresent() {
                }

                @Override
                public void onAdClick() {
                }

                @Override
                public void onAdDismissed() {
                }

                @Override
                public void onAdFailed(String s) {
                }

                @Override
                public void onAdLoaded(int i) {
                    mAdSize = i;
                }

                @Override
                public void onStimulateSuccess() {
                }
            }, AdType.AD_STANDARD_NEWSFEED);
            advInfoList.load(MiAdvType.informationFlowSmallMap.getAdvId(), 10);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        try {
            if (advInfoList != null) {
                advInfoList.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void convert(final BaseRecyclerHolder holder, final CommonData item, final int position) {
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(HomeArticleAdapter.this, holder.itemView, position);
                }
            });
        }
        holder.itemView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(item.getEnvelopePic())) {
            holder.setGone(R.id.iv_img, true);
            Glide.with(context)
                    .load(item.getEnvelopePic())
                    .error(R.drawable.ic_holder_empty)
                    .placeholder(R.drawable.ic_holder_empty)
                    .into((ImageView) holder.getView(R.id.iv_img));
        } else {
            holder.setGone(R.id.iv_img, false);
        }
        holder.setText(R.id.tv_title, item.getTitle());

        if (item.isTop()) {
            holder.setGone(R.id.tv_tag, true);
            holder.setText(R.id.tv_tag, "置顶");
            holder.getView(R.id.tv_auther).setPadding(context.getResources().getDimensionPixelSize(R.dimen.padding_8), 0, 0, 0);
        } else {
            if (item.isFresh()) {
                holder.setGone(R.id.tv_tag, true);
                holder.getView(R.id.tv_auther).setPadding(context.getResources().getDimensionPixelSize(R.dimen.padding_8), 0, 0, 0);
            } else {
                holder.setGone(R.id.tv_tag, false);
                holder.getView(R.id.tv_auther).setPadding(0, 0, 0, 0);
            }
        }
        holder.setText(R.id.tv_auther, item.getAuthor());
        String superChapterName = item.getSuperChapterName();
        if (TextUtils.isEmpty(superChapterName)) {
            holder.setText(R.id.tv_type, item.getChapterName());
        } else {
            holder.setText(R.id.tv_type, superChapterName + "/" + item.getChapterName());
        }
        holder.setText(R.id.tv_time, item.getNiceDate());
        //在初始化checkBox状态和设置状态变化监听事件之前先把状态变化监听事件设置为null
        holder.setOnCheckedChangeListener(R.id.cb_collection, null);
        holder.setChecked(R.id.cb_collection, item.isCollect());
        holder.setOnCheckedChangeListener(R.id.cb_collection, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null != onCheckedChangeListener) {
                    onCheckedChangeListener.onCheckedChanged(buttonView, isChecked, item, position);
                }
            }
        });
        if (position != 0 && position % 5 == 0) {
            holder.setGone(R.id.fl_ad_info, true);
            ViewGroup container = holder.getView(R.id.fl_ad_info);
            if (mAdSize > 0) {
                try {
                    container.addView(advInfoList.updateAdView(null, showedAdSize));
                    showedAdSize++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    holder.setGone(R.id.fl_ad_info, false);
                }
            } else {
                holder.setGone(R.id.fl_ad_info, false);
            }
        } else {
            if (position == 0) showedAdSize = 0;
            holder.setGone(R.id.fl_ad_info, false);
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked, CommonData item, int postion);
    }
}
