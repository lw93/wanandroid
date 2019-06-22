package com.xygit.note.notebook.manager.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.xygit.note.notebook.BuildConfig;
import com.xygit.note.notebook.api.vo.CommResponse;
import com.xygit.note.notebook.api.NoteBookUrl;
import com.xygit.note.notebook.api.WanAndService;
import com.xygit.note.notebook.api.vo.Banner;
import com.xygit.note.notebook.api.vo.BasePage;
import com.xygit.note.notebook.api.vo.Children;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.api.vo.LoginResult;
import com.xygit.note.notebook.api.vo.Navigation;
import com.xygit.note.notebook.api.vo.SimpleData;
import com.xygit.note.notebook.api.vo.TodoDesc;
import com.xygit.note.notebook.manager.gson.GsonManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/2
 */

public class HttpManager {

    private Retrofit mRetrofit;
    private CompositeSubscription subscriptions;
    private HttpCallBack httpCallback;

    private HttpManager() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cookieJar(new CookieCache())
                .retryOnConnectionFailure(false)
                .cache(HttpFileCache.getInstance().getHttpFileCache())
                .addInterceptor(new LoginIntercepter())
                .addInterceptor(new HttpCacheInterceptor());
        if (BuildConfig.DEBUG) {
            okhttpBuilder.addNetworkInterceptor(new StethoInterceptor());
        }
        mRetrofit = new Retrofit.Builder()
                .client(okhttpBuilder.build())
                .baseUrl(NoteBookUrl.WAN_ANDROID_HOST)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(new StringFactory())
                .addConverterFactory(GsonConverterFactory.create(GsonManager.getInstance().builder().build()))
                .build();
        subscriptions = new CompositeSubscription();
        httpCallback = new DefaultHttpCallBack();
    }

    public void clearSubscribers() {
        if (subscriptions.hasSubscriptions()) {
            subscriptions.clear();
        }
    }

    private void setHttpCallback(HttpCallBack httpCallback) {
        if (null == httpCallback) {
            httpCallback = new DefaultHttpCallBack();
        }
        this.httpCallback = httpCallback;
    }

    public static HttpManager getInstance() {
        return HttpManageHelp.mHttpManager;
    }


    private static class HttpManageHelp {
        private static final HttpManager mHttpManager = new HttpManager();
    }


    public <T> T createService(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    //获取公众号列表
    public void quryPublicList(HttpCallBack callBack, Subscriber<CommResponse<List<Children>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryPublicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //查看某个公众号历史数据
    public void qurySinglePublicHistory(HttpCallBack callBack, String publicId, String pageNum, Subscriber<CommResponse<BasePage<CommonData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .qurySinglePublicHistory(publicId, pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //在某个公众号中搜索历史文章
    public void qurySinglePublicArticleHistory(HttpCallBack callBack, String publicId, String pageNum, String k, Subscriber<CommResponse<BasePage<CommonData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .qurySinglePublicArticleHistory(publicId, pageNum, k)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //按时间分页展示所有项目。
    public void quryAllProjectByPage(HttpCallBack callBack, String pageNum, Subscriber<CommResponse<BasePage<CommonData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryAllProjectByPage(pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //首页文章列表
    public void quryHomeArticle(HttpCallBack callBack, String pageNum, Subscriber<CommResponse<BasePage<CommonData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryHomeArticle(pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }


    //首页banner
    public void quryHomeBanner(HttpCallBack callBack, Subscriber<CommResponse<List<Banner>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryHomeBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //常用网站
    public void quryCommonUseWebSites(HttpCallBack callBack, Subscriber<CommResponse<List<SimpleData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryCommonUseWebSites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //目前搜索最多的关键词
    public void quryHotKeys(HttpCallBack callBack, Subscriber<CommResponse<List<SimpleData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryHotKeys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //体系数据
    public void quryTreeDatas(HttpCallBack callBack, Subscriber<CommResponse<List<Children>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryTreeDatas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }


    //知识体系下的文章
    public void quryKnowledgeTreeDatas(HttpCallBack callBack, String pageNum, String cid, Subscriber<CommResponse<BasePage<CommonData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryKnowledgeTreeDatas(pageNum, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //导航数据
    public void quryNavigationDatas(HttpCallBack callBack, Subscriber<CommResponse<List<Navigation>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryNavigationDatas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //项目分类
    public void quryProjectCategory(HttpCallBack callBack, Subscriber<CommResponse<List<Children>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryProjectCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //某一个分类下项目列表数据，分页展示
    public void qurySingleProjectCategory(HttpCallBack callBack, String pageNum, String cid, Subscriber<CommResponse<BasePage<CommonData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .qurySingleProjectCategory(pageNum, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //登录
    public void login(HttpCallBack callBack, String userName, String password, Subscriber<CommResponse<LoginResult>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .login(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //注册
    public void register(HttpCallBack callBack, String userName, String password, String repassword, Subscriber<CommResponse<LoginResult>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .register(userName, password, repassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //退出
    public void logout(HttpCallBack callBack, Subscriber<CommResponse<Object>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //收藏文章列表
    public void quryCollectArticles(HttpCallBack callBack, String pageNum, Subscriber<CommResponse<BasePage<CommonData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryCollectArticles(pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //收藏站内文章
    public void collectInnerArticle(HttpCallBack callBack, String articleId, Subscriber<CommResponse<Object>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .collectInnerArticle(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //收藏站外文章
    public void collectOutArticle(HttpCallBack callBack, String title, String author, String link, Subscriber<CommResponse<CommonData>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .collectOutArticle(title, author, link)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //取消收藏(文章列表)
    public void cancelCollectArticle(HttpCallBack callBack, String articleId, Subscriber<CommResponse<Object>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .cancelCollectArticle(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //取消收藏(我的收藏页面)
    public void cancelMyCollectArticle(HttpCallBack callBack, String id, String originId, Subscriber<CommResponse<Object>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .cancelMyCollectArticle(id, originId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //收藏网站列表
    public void quryCollectWebSites(HttpCallBack callBack, Subscriber<CommResponse<List<SimpleData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryCollectWebSites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //收藏网址
    public void collectUrl(HttpCallBack callBack, String name, String link, Subscriber<CommResponse<List<SimpleData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .collectUrl(name, link)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //编辑收藏网站
    public void updateCollectWebSite(HttpCallBack callBack, String id, String name, String link, Subscriber<CommResponse<List<SimpleData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .updateCollectWebSite(id, name, link)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //删除收藏网站
    public void deleteCollectWebSite(HttpCallBack callBack, String id, Subscriber<CommResponse<Object>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .deleteCollectWebSite(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //关键词搜索
    public void quryArticleByKey(HttpCallBack callBack, String pageNum, String k, Subscriber<CommResponse<BasePage<CommonData>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryArticleByKey(pageNum, k)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //新增一个TODO
    public void addTODO(HttpCallBack callBack, String title, String content, String date, String type, String priority, Subscriber<CommResponse<TodoDesc>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .addTODO(title, content, date, type, priority)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //更新一个Todo
    public void updateTODO(HttpCallBack callBack, String id, String title, String content, String date, String status, String type, String priority, Subscriber<CommResponse<TodoDesc>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .updateTODO(id, title, content, date, status, type, priority)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //删除一个Todo
    public void deleteTODO(HttpCallBack callBack, String id, Subscriber<CommResponse<Object>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .deleteTODO(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //仅更新完成状态Todo
    public void updateTODOStatus(HttpCallBack callBack, String id, String status, Subscriber<CommResponse<TodoDesc>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .updateTODOStatus(id, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }

    //TODO列表
    public void quryTODOList(HttpCallBack callBack, String pageNum, String status, String type, String priority, String orderby, Subscriber<CommResponse<BasePage<TodoDesc>>> subscriber) {
        setHttpCallback(callBack);
        Subscription subscription = createService(WanAndService.class)
                .quryTODOList(pageNum, status, type, priority, orderby)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        httpCallback.hideProgress();
                    }
                })
                .subscribe(subscriber);
        subscriptions.add(subscription);
    }
}
