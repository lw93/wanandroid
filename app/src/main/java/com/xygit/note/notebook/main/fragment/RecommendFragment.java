package com.xygit.note.notebook.main.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerAdapter;
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.WanAndService;
import com.xygit.note.notebook.api.vo.BasePage;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.base.BaseFragment;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.login.LoginActivity;
import com.xygit.note.notebook.main.activity.CollectionActivity;
import com.xygit.note.notebook.main.adapter.HomeArticleAdapter;
import com.xygit.note.notebook.manager.evenbus.CollectAction;
import com.xygit.note.notebook.manager.evenbus.LoginAction;
import com.xygit.note.notebook.manager.glide.GlideImageLoder;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.web.WebActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 推荐页
 *
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public class RecommendFragment extends BaseFragment implements OnBannerListener, OnRefreshListener, OnLoadMoreListener,
        HttpCallBack, HomeArticleAdapter.OnCheckedChangeListener, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener {
    private Banner bannerFragmentRecommend;
    private RecyclerView rvFragmentRecommend;
    private SmartRefreshLayout refreshFragmentRecommend;
    private View emptyLayout;
    private List<com.xygit.note.notebook.api.vo.Banner> noteBookBanner;
    private List<String> bannerUrls, bannerTitles;
    private BasePage<CommonData> articles;
    private int currentPage = 0;
    private HomeArticleAdapter articleAdapter;
    private FloatingActionButton btnArrowUp;
//    private CommonData collectItem;

    private AdapterDataObserver articleObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
//            emptyLayout();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
//            emptyLayout();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
//            emptyLayout();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
//            emptyLayout();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
//            emptyLayout();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
//            emptyLayout();
        }
    };

    private void emptyLayout() {
        if (articleAdapter.getItemCount() < 1) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.GONE);
        }
    }

    private void bindData(List<com.xygit.note.notebook.api.vo.Banner> noteBookBanner, BasePage<CommonData> articles) {
        if (null == noteBookBanner && null == articles) {
            return;
        }
        if (null != noteBookBanner) {
            int size = noteBookBanner.size();
            bannerUrls.clear();
            bannerUrls = new ArrayList<>();
            bannerTitles.clear();
            bannerTitles = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                com.xygit.note.notebook.api.vo.Banner banner = noteBookBanner.get(i);
                if (1 == banner.getIsVisible()) {
                    bannerUrls.add(banner.getImagePath());
                    bannerTitles.add(banner.getTitle());
                }
            }
            bannerFragmentRecommend.update(bannerUrls, bannerTitles);
        }
        if (null != articles) {
            List<CommonData> data = articles.getDatas();
            articleAdapter.addItems(data);
        }

    }

    @Override
    public int layoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initView() {
        bannerFragmentRecommend = rootView.findViewById(R.id.banner_fragment_recommend);
        refreshFragmentRecommend = rootView.findViewById(R.id.refresh_fragment_recommend);
        rvFragmentRecommend = rootView.findViewById(R.id.rv_fragment_recommend);
        btnArrowUp = rootView.findViewById(R.id.fb_arrow_up);
        emptyLayout = rootView.findViewById(R.id.cl_container_layout_empty);
        emptyLayout.setVisibility(View.GONE);
        rvFragmentRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFragmentRecommend.addItemDecoration(new DividerItemDecoration(rvFragmentRecommend.getContext(), LinearLayout.VERTICAL));
    }

    @Override
    public void initAction() {
        emptyLayout.setOnClickListener(this);
        btnArrowUp.setOnClickListener(this);
        bannerFragmentRecommend.setOnBannerListener(this);
        refreshFragmentRecommend.setRefreshHeader(new ClassicsHeader(bannerFragmentRecommend.getContext()));
        refreshFragmentRecommend.setOnRefreshListener(this);
        refreshFragmentRecommend.setOnLoadMoreListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        refreshFragmentRecommend.setEnableRefresh(true);//启用刷新
        refreshFragmentRecommend.setEnableLoadMore(true);//启用加载
        articleAdapter = new HomeArticleAdapter(null, rvFragmentRecommend);
        articleAdapter.setOnCheckedChangeListener(this);
        articleAdapter.registerAdapterDataObserver(articleObserver);
        articleAdapter.setOnItemClickListener(this);
        bannerUrls = new ArrayList<>(7);
        bannerTitles = new ArrayList<>(7);
        bannerFragmentRecommend.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setIndicatorGravity(BannerConfig.RIGHT).setImages(bannerUrls)
                .setBannerTitles(bannerTitles).setImageLoader(new GlideImageLoder())
                .start();
        rvFragmentRecommend.setAdapter(articleAdapter);
        refreshFragmentRecommend.autoRefresh();
//        SpruceManager.getInstance().build(clContainerFragmentRecommend);
    }

    @Override
    public void clearData() {
        bannerFragmentRecommend.releaseBanner();
        if (noteBookBanner != null) {
            noteBookBanner.clear();
            noteBookBanner = null;
        }
        if (bannerUrls != null) {
            bannerUrls.clear();
            bannerUrls = null;
        }
        if (bannerTitles != null) {
            bannerTitles.clear();
            bannerTitles = null;
        }
        if (articles != null) {
            articles = null;
        }
        if (articleAdapter != null) {
            articleAdapter.clear();
            articleAdapter = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        bannerFragmentRecommend.startAutoPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        //开始轮播
        bannerFragmentRecommend.stopAutoPlay();
    }

    @Override
    public void OnBannerClick(int position) {
        if (noteBookBanner != null && !noteBookBanner.isEmpty()) {
            com.xygit.note.notebook.api.vo.Banner item = noteBookBanner.get(position);
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra(NoteBookConst.INETENT_PARAM_URL, item.getUrl());
            ActivityUtil.startActivity(getActivity(), intent);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        queryArticles();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        HttpManager.getInstance().quryHomeBanner(this, new CommSubscriber<List<com.xygit.note.notebook.api.vo.Banner>>(getActivity()) {

            @Override
            protected void onSucess(CommResponse<List<com.xygit.note.notebook.api.vo.Banner>> sparseArrayCommResponse) {
                noteBookBanner = sparseArrayCommResponse.getData();
                bindData(noteBookBanner, null);
                hideProgress();
            }
        });
        currentPage = 0;
        queryTopArticles();
    }

    private void queryTopArticles() {
        HttpManager.getInstance()
                .createService(WanAndService.class)
                .quryHomeTop()
                .concatMap(new Func1<CommResponse<List<CommonData>>, Observable<CommResponse<BasePage<CommonData>>>>() {
                    @Override
                    public Observable<CommResponse<BasePage<CommonData>>> call(CommResponse<List<CommonData>> listCommResponse) {
                        if (listCommResponse != null && listCommResponse.getData() != null) {
                            final List<CommonData> topArticles = listCommResponse.getData();
                            for (CommonData topArticle : topArticles) {
                                topArticle.setTop(true);
                            }
                            refreshFragmentRecommend.post(new Runnable() {
                                @Override
                                public void run() {
                                    articleAdapter.clear();
                                    articleAdapter.addItemsSilent(topArticles);
                                }
                            });
                        }
                        return HttpManager.getInstance()
                                .createService(WanAndService.class)
                                .quryHomeArticle(String.valueOf(currentPage));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        hideProgress();
                    }
                })
                .subscribe(new CommSubscriber<BasePage<CommonData>>(getActivity()) {

                    @Override
                    protected void onSucess(CommResponse<BasePage<CommonData>> basePageCommResponse) {
                        articles = basePageCommResponse.getData();
                        bindData(null, articles);
                        hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refreshFragmentRecommend.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                emptyLayout();
                            }
                        }, 1500);
                    }
                });
    }

    private void queryArticles() {
        HttpManager.getInstance().quryHomeArticle(this, String.valueOf(currentPage), new CommSubscriber<BasePage<CommonData>>(getActivity()) {

            @Override
            protected void onSucess(CommResponse<BasePage<CommonData>> basePageCommResponse) {
                articles = basePageCommResponse.getData();
                bindData(null, articles);
                hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgress();
            }
        });
    }

    @Override
    public void showProgress() {
        if (0 == currentPage) {
            if (refreshFragmentRecommend.getState() != RefreshState.Refreshing) {
                refreshFragmentRecommend.autoRefresh();
            }
        } else {
            if (refreshFragmentRecommend.getState() != RefreshState.Loading) {
                refreshFragmentRecommend.autoLoadMore();
            }
        }
    }

    @Override
    public void hideProgress() {
        if (0 == currentPage) {
            refreshFragmentRecommend.finishRefresh(true);
        } else {
            refreshFragmentRecommend.finishLoadMore(true);
        }
        refreshFragmentRecommend.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLayout();
            }
        }, 1500);
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked, CommonData item, final int position) {
        buttonView.setChecked(item.isCollect());
//        collectItem = item;
        onCollectionChanged(item, position);
    }

    private void onCollectionChanged(final CommonData item, final int position) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra(NoteBookConst.INETENT_START_TYPE, LoginActivity.START_FROM_RECOMMEND);
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, item);
        if (!item.isCollect()) {
            HttpManager.getInstance().collectInnerArticle(this, String.valueOf(item.getId()), new CommSubscriber<Object>(getActivity(), intent) {

                @Override
                protected void onSucess(CommResponse<Object> objectCommResponse) {
                    if (NoteBookConst.RESPONSE_SUCCESS == objectCommResponse.getErrorCode()) {
                        item.setCollect(true);
                        articleAdapter.notifyItemChanged(position);
                    }
                }
            });
            return;
        }
        HttpManager.getInstance().cancelCollectArticle(this, String.valueOf(item.getId()), new CommSubscriber<Object>(getActivity(), intent) {

            @Override
            protected void onSucess(CommResponse<Object> objectCommResponse) {
                if (NoteBookConst.RESPONSE_SUCCESS == objectCommResponse.getErrorCode()) {
                    item.setCollect(false);
                    articleAdapter.notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_container_layout_empty) {
            emptyLayout.setVisibility(View.GONE);
            refreshFragmentRecommend.autoRefresh();
        } else if (id == R.id.fb_arrow_up) {
            if (articleAdapter != null && articleAdapter.getItemCount() > 0) {
                rvFragmentRecommend.smoothScrollToPosition(0);
            }
        }
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {
        CommonData data = articleAdapter.getList().get(position);
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(NoteBookConst.INETENT_PARAM_URL, data.getLink());
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, data);
        ActivityUtil.startActivity(getActivity(), intent);
    }

    @Subscribe
    public void collectionAction(CollectAction collectAction) {
        if (null != collectAction) {
            CommonData commonData = collectAction.getData();
            int position = 0;
            Iterator<CommonData> iterator = articleAdapter.getList().iterator();
            while (iterator.hasNext()) {
                CommonData realData = iterator.next();
                if (CollectionActivity.START_FROM_COLLECTION == collectAction.getType()) {
                    if (String.valueOf(realData.getId()).equals(commonData.getOriginId()) && realData.getTitle().equals(commonData.getTitle())) {
                        commonData = realData;
                        position = articleAdapter.getList().indexOf(realData);
                        break;
                    }
                } else {
                    if (realData.getId() == commonData.getId() && realData.getTitle().equals(commonData.getTitle())) {
                        commonData = realData;
                        position = articleAdapter.getList().indexOf(realData);
                        break;
                    }
                }

            }
            onCollectionChanged(commonData, position);
        }
    }

    @Subscribe
    public void loginAction(LoginAction loginAction) {
        refreshFragmentRecommend.autoRefresh();
    }
}
