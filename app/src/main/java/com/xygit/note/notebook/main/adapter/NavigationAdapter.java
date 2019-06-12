package com.xygit.note.notebook.main.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerAdapter;
import com.xygit.note.notebook.adapter.BaseRecyclerHolder;
import com.xygit.note.notebook.adv.MiAdvType;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.api.vo.Navigation;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/17
 */

public class NavigationAdapter extends BaseRecyclerAdapter<Navigation, BaseRecyclerHolder> {

    private OnTagClickListener onTagClickListener;
    private IAdWorker advInfoList;
    private int mAdSize;
    private int showedAdSize;

    public NavigationAdapter(@Nullable List<Navigation> data, RecyclerView adapterView) {
        this(R.layout.item_list_knowledge_system, data, adapterView);
    }

    public NavigationAdapter(int layoutResId, @Nullable List<Navigation> data, RecyclerView adapterView) {
        super(layoutResId, data, adapterView);
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    @Override
    protected void convert(final BaseRecyclerHolder holder, final Navigation item, final int position) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.setText(R.id.tv_title_system, item.getName());
        final TagFlowLayout flowLayout = (TagFlowLayout) holder.getView(R.id.fl_tag_system);
        flowLayout.setAdapter(new TagAdapter<CommonData>(item.getArticles()) {
            @Override
            public View getView(FlowLayout parent, int position, CommonData children) {
                ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.item_tag_list, null, false);
                TextView textView = constraintLayout.findViewById(R.id.tv_tag_item);
                if (null != children) {
                    textView.setText(children.getTitle());
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
        if (position != 0 && position % 4 == 0) {
            holder.setGone(R.id.container_stimulate_system, true);
            ViewGroup container = holder.getView(R.id.container_stimulate_system);
            if (mAdSize > 0) {
                try {
                    container.addView(advInfoList.updateAdView(null, showedAdSize));
                    showedAdSize++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    holder.setGone(R.id.container_stimulate_system, false);
                }
            } else {
                holder.setGone(R.id.container_stimulate_system, false);
            }
        } else {
            if (position == 0) showedAdSize = 0;
            holder.setGone(R.id.container_stimulate_system, false);
        }
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

    public interface OnTagClickListener {
        void onTagClick(Navigation parent, int parentPostion, int childPosition, TagFlowLayout flowLayout, View childView);
    }

    static class TagViewHolder {
        private TextView textView;

        public TagViewHolder(View itemView) {
            textView = itemView.findViewById(R.id.tv_tag_item);
            textView.setText("");
        }
    }
}
