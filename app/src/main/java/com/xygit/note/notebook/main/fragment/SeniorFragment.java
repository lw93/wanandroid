package com.xygit.note.notebook.main.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.vo.Children;
import com.xygit.note.notebook.base.BaseFragment;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.main.activity.SearchDetailActivity;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * 大牛页
 *
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public class SeniorFragment extends BaseFragment implements View.OnClickListener, OnRefreshListener, HttpCallBack, TagFlowLayout.OnTagClickListener {
    public static final int START_FROM_SENIOR = 0x006;
    private TagFlowLayout flTagSenior;
    private View emptyLayout;
    private SmartRefreshLayout refreshLayoutSenior;
    private TagAdapter<Children> publicTagAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_senior;
    }


    @Override
    public void initView() {
        flTagSenior = (TagFlowLayout) rootView.findViewById(R.id.fl_tag_senior);
        refreshLayoutSenior = rootView.findViewById(R.id.refresh_fragment_senior);
        emptyLayout = rootView.findViewById(R.id.cl_container_layout_empty);
    }

    @Override
    public void initAction() {
        emptyLayout.setOnClickListener(this);
        flTagSenior.setOnTagClickListener(this);
        refreshLayoutSenior.setRefreshHeader(new ClassicsHeader(refreshLayoutSenior.getContext()));
        refreshLayoutSenior.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        emptyLayout.setVisibility(View.GONE);
        refreshLayoutSenior.autoRefresh();
    }

    @Override
    public void clearData() {
        if (publicTagAdapter != null) {
            publicTagAdapter = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_container_layout_empty) {
            emptyLayout.setVisibility(View.GONE);
            refreshLayoutSenior.autoRefresh();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        quryPublicList();
    }

    private void quryPublicList() {
        HttpManager.getInstance().quryPublicList(this,new CommSubscriber<List<Children>>(getActivity()) {
            @Override
            protected void onSucess(CommResponse<List<Children>> listCommResponse) {
                if (listCommResponse != null) {
                    setTagAdapter(listCommResponse.getData());
                }
                hideProgress();
            }
        });

    }

    private void setTagAdapter(List<Children> data) {
        publicTagAdapter = new TagAdapter<Children>(data) {
            @Override
            public View getView(FlowLayout parent, int position, Children children) {
                ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_tag_list, null, false);
                TextView textView = constraintLayout.findViewById(R.id.tv_tag_item);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
                params.setMargins(getResources().getDimensionPixelSize(R.dimen.margin_24), getResources().getDimensionPixelSize(R.dimen.margin_16), getResources().getDimensionPixelSize(R.dimen.margin_24), getResources().getDimensionPixelSize(R.dimen.margin_16));
                constraintLayout.setLayoutParams(params);
                if (null != children) {
                    textView.setText(children.getName());
                }
                return constraintLayout;
            }
        };
        flTagSenior.setAdapter(publicTagAdapter);
    }

    @Override
    public void showProgress() {
        if (refreshLayoutSenior.getState() != RefreshState.Refreshing) {
            refreshLayoutSenior.autoRefresh();
        }
    }

    @Override
    public void hideProgress() {
        refreshLayoutSenior.finishRefresh(true);
        emptyLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLayout();
            }
        }, 2000);
    }

    private void emptyLayout() {
        if (publicTagAdapter == null) {
            emptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (publicTagAdapter.getCount() < 1) {
            emptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        if (publicTagAdapter != null) {
            Children children = publicTagAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), SearchDetailActivity.class);
            intent.putExtra(NoteBookConst.INETENT_START_TYPE, SeniorFragment.START_FROM_SENIOR);
            intent.putExtra(NoteBookConst.INETENT_PARAM_ENTITY, children);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), view, "tagTextView");
            startActivity(intent, options.toBundle());        }
        return false;
    }
}
