package com.xygit.note.notebook.api;

import com.xygit.note.notebook.api.vo.Banner;
import com.xygit.note.notebook.api.vo.BasePage;
import com.xygit.note.notebook.api.vo.Children;
import com.xygit.note.notebook.api.vo.CommResponse;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.api.vo.LoginResult;
import com.xygit.note.notebook.api.vo.Navigation;
import com.xygit.note.notebook.api.vo.SimpleData;
import com.xygit.note.notebook.api.vo.TodoDesc;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/9
 */

public interface WanAndService {

    //获取公众号列表
    @GET(NoteBookUrl.PULIC_NUM_LIST)
    Observable<CommResponse<List<Children>>> quryPublicList();

    //查看某个公众号历史数据
    @GET(NoteBookUrl.SINGLE_NUM_HISTORY)
    Observable<CommResponse<BasePage<CommonData>>> qurySinglePublicHistory(@Path("publicId") String publicId, @Path("pageNum") String pageNum);

    //在某个公众号中搜索历史文章
    @GET(NoteBookUrl.SINGLE_NUM_ARTICLE_HISTORY)
    Observable<CommResponse<BasePage<CommonData>>> qurySinglePublicArticleHistory(@Path("publicId") String publicId, @Path("pageNum") String pageNum, @Query("k") String k);

    //按时间分页展示所有项目。
    @GET(NoteBookUrl.LATRST_PROJECTS)
    Observable<CommResponse<BasePage<CommonData>>> quryAllProjectByPage(@Path("pageNum") String pageNum);

    //首页文章列表
    @GET(NoteBookUrl.ARTCLE_LIST)
    Observable<CommResponse<BasePage<CommonData>>> quryHomeArticle(@Path("pageNum") String pageNum);

    //首页banner
    @GET(NoteBookUrl.HOME_BANNER)
    Observable<CommResponse<List<Banner>>> quryHomeBanner();

    //置顶文章
    @GET(NoteBookUrl.HOME_TOP)
    Observable<CommResponse<List<CommonData>>> quryHomeTop();

    //常用网站
    @GET(NoteBookUrl.FRIEND_WEBSITES)
    Observable<CommResponse<List<SimpleData>>> quryCommonUseWebSites();

    //目前搜索最多的关键词
    @GET(NoteBookUrl.HOT_KEY)
    Observable<CommResponse<List<SimpleData>>> quryHotKeys();

    //体系数据
    @GET(NoteBookUrl.TREE)
    Observable<CommResponse<List<Children>>> quryTreeDatas();

    //知识体系下的文章
    @GET(NoteBookUrl.KNOWLEDGE_TREE)
    Observable<CommResponse<BasePage<CommonData>>> quryKnowledgeTreeDatas(@Path("pageNum") String pageNum, @Query("cid") String cid);

    //导航数据
    @GET(NoteBookUrl.NAVIGATION)
    Observable<CommResponse<List<Navigation>>> quryNavigationDatas();

    //项目分类
    @GET(NoteBookUrl.PROJECT_TREE)
    Observable<CommResponse<List<Children>>> quryProjectCategory();

    //某一个分类下项目列表数据，分页展示
    @GET(NoteBookUrl.SINGLE_PROJECT_LIST)
    Observable<CommResponse<BasePage<CommonData>>> qurySingleProjectCategory(@Path("pageNum") String pageNum, @Query("cid") String cid);

    //登录
    @POST(NoteBookUrl.USER_LOGIN)
    Observable<CommResponse<LoginResult>> login(@Query("username") String userName, @Query("password") String password);

    //注册
    @POST(NoteBookUrl.USER_REGISTER)
    Observable<CommResponse<LoginResult>> register(@Query("username") String userName, @Query("password") String password, @Query("repassword") String repassword);

    //退出
    @GET(NoteBookUrl.USER_LOGOUT)
    Observable<CommResponse<Object>> logout();

    //收藏文章列表
    @GET(NoteBookUrl.COLLECT_ARTICLE)
    Observable<CommResponse<BasePage<CommonData>>> quryCollectArticles(@Path("pageNum") String pageNum);

    //收藏站内文章
    @POST(NoteBookUrl.COLLECT_INNER_ARTICLE)
    Observable<CommResponse<Object>> collectInnerArticle(@Path("articleId") String articleId);

    //收藏站外文章
    @POST(NoteBookUrl.COLLECT_OUT_ARTICLE)
    Observable<CommResponse<CommonData>> collectOutArticle(@Query("title") String title, @Query("author") String author, @Query("link") String link);

    //取消收藏(文章列表)
    @POST(NoteBookUrl.CANCLE_COLLECT_ARTICLE)
    Observable<CommResponse<Object>> cancelCollectArticle(@Path("articleId") String articleId);

    //取消收藏(我的收藏页面)
    @POST(NoteBookUrl.CANCLE_MY_COLLECT_ARTICLE)
    Observable<CommResponse<Object>> cancelMyCollectArticle(@Path("id") String id, @Query("originId") String originId);

    //收藏网站列表
    @GET(NoteBookUrl.COLLECT_WEBSITES)
    Observable<CommResponse<List<SimpleData>>> quryCollectWebSites();

    //收藏网址
    @POST(NoteBookUrl.COLLECT_URL)
    Observable<CommResponse<List<SimpleData>>> collectUrl(@Query("name") String name, @Query("link") String link);

    //编辑收藏网站
    @POST(NoteBookUrl.UPDATE_COLLECT_WEBSITE)
    Observable<CommResponse<List<SimpleData>>> updateCollectWebSite(@Query("id") String id, @Query("name") String name, @Query("link") String link);

    //删除收藏网站
    @POST(NoteBookUrl.DELETE_COLLECT_WEBSITE)
    Observable<CommResponse<Object>> deleteCollectWebSite(@Query("id") String id);

    //关键词搜索
    @POST(NoteBookUrl.QURY_ARTICLE)
    Observable<CommResponse<BasePage<CommonData>>> quryArticleByKey(@Path("pageNum") String pageNum, @Query("k") String k);

    //新增一个TODO
    @POST(NoteBookUrl.ADD_TODO)
    Observable<CommResponse<TodoDesc>> addTODO(@Query("title") String title, @Query("content") String content, @Query("date") String date, @Query("type") String type, @Query("priority") String priority);

    //更新一个Todo
    @POST(NoteBookUrl.UPDATE_TODO)
    Observable<CommResponse<TodoDesc>> updateTODO(@Path("id") String id, @Query("title") String title, @Query("content") String content, @Query("date") String date, @Query("status") String status, @Query("type") String type, @Query("priority") String priority);

    //删除一个Todo
    @POST(NoteBookUrl.DELETE_TODO)
    Observable<CommResponse<Object>> deleteTODO(@Path("id") String id);

    //仅更新完成状态Todo
    @POST(NoteBookUrl.UPDATE_DONE_TODO)
    Observable<CommResponse<TodoDesc>> updateTODOStatus(@Path("id") String id, @Query("status") String status);

    //TODO列表
    @POST(NoteBookUrl.TODO_LIST)
    Observable<CommResponse<BasePage<TodoDesc>>> quryTODOList(@Path("pageNum") String pageNum, @Query("status") String status, @Query("type") String type, @Query("priority") String priority, @Query("orderby") String orderby);
}
