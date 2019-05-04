package com.xygit.note.notebook.api;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/23
 */

public class NoteBookUrl {

    public static final String WAN_ANDROID_HOST = "https://www.wanandroid.com/";

    public static final String PULIC_NUM_LIST = "wxarticle/chapters/json";//获取公众号列表 方法： GET

    public static final String SINGLE_NUM_HISTORY = "wxarticle/list/{publicId}/{pageNum}/json";//查看某个公众号历史数据 方法： GET   参数：公众号 ID：拼接在 url 中，eg:405 公众号页码：拼接在url 中，eg:1

    public static final String SINGLE_NUM_ARTICLE_HISTORY = "wxarticle/list/{publicId}/{pageNum}/json";//在某个公众号中搜索历史文章 方法： GET参数 ：k : 字符串，eg:Java公众号 ID：拼接在 url 中，eg:405 公众号页码：拼接在url 中，eg:1

    public static final String LATRST_PROJECTS = "article/listproject/{pageNum}/json";//按时间分页展示所有项目。 方法： GET 参数：页码，拼接在连接中，从0开始。

    public static final String ARTCLE_LIST = "article/list/{pageNum}/json";//首页文章列表 方法： GET 参数：页码，拼接在连接中，从0开始。

    public static final String HOME_BANNER = "banner/json";//首页banner 方法： GET 参数：无

    public static final String HOME_TOP = "article/top/json";//首页置顶 方法： GET 参数：无

    public static final String FRIEND_WEBSITES = "friend/json";//常用网站 方法： GET 参数：无

    public static final String HOT_KEY = "hotkey/json";//搜索热词 方法： GET 参数：无

    public static final String TREE = "tree/json";//体系数据 方法： GET 参数：无

    public static final String KNOWLEDGE_TREE = "article/list/{pageNum}/json";//知识体系下的文章 方法： GET 参数：cid 分类的id，上述二级目录的id页码：拼接在链接上，从0开始。

    public static final String NAVIGATION = "navi/json";//导航数据 方法： GET 参数：无

    public static final String PROJECT_TREE = "project/tree/json";//项目分类 方法： GET 参数：无

    public static final String SINGLE_PROJECT_LIST = "project/list/{pageNum}/json";//项目列表数据 某一个分类下项目列表数据，分页展示 方法： GET 参数：cid 分类的id，上面项目分类接口 页码：拼接在链接中，从1开始。

    public static final String USER_LOGIN = "user/login";//登录 方法：POST参数：username，password 登录后会在cookie中返回账号密码，只要在客户端做cookie持久化存储即可自动登录验证。

    public static final String USER_REGISTER = "user/register";//注册 方法：POST 参数 username,password,repassword

    public static final String USER_LOGOUT = "user/logout/json";//退出 方法： GET 参数：无

    public static final String COLLECT_ARTICLE = "lg/collect/list/{pageNum}/json";//收藏文章列表 方法： GET 参数： 页码：拼接在链接中，从0开始。

    public static final String COLLECT_INNER_ARTICLE = "lg/collect/{articleId}/json";//收藏站内文章 方法：POST 参数： 文章id，拼接在链接中。

    public static final String COLLECT_OUT_ARTICLE = "lg/collect/add/json";//收藏站外文章 方法：POST 参数：title，author，link

    //取消收藏
    public static final String CANCLE_COLLECT_ARTICLE = "lg/uncollect_originId/{articleId}/json";//文章列表 方法：POST 参数：id:拼接在链接上

    public static final String CANCLE_MY_COLLECT_ARTICLE = "lg/uncollect/{id}/json";//我的收藏页面（该页面包含自己录入的内容） 方法：POST 参数：id:拼接在链接上 originId:列表页下发，无则为-1 originId 代表的是你收藏之前的那篇文章本身的id； 但是收藏支持主动添加，这种情况下，没有originId则为-1

    public static final String COLLECT_WEBSITES = "lg/collect/usertools/json";//收藏网站列表 方法： GET 参数：无

    public static final String COLLECT_URL = "lg/collect/addtool/json";//收藏网址 方法：POST 参数：name,link

    public static final String UPDATE_COLLECT_WEBSITE = "lg/collect/updatetool/json";//编辑收藏网站 方法：POST 参数：id,name,link

    public static final String DELETE_COLLECT_WEBSITE = "lg/collect/deletetool/json";//删除收藏网站 方法：POST 参数：id

    public static final String QURY_ARTICLE = "article/query/{pageNum}/json";//搜素 方法：POST 参数：页码：拼接在链接上，从0开始。k ： 搜索关键词 注意：支持多个关键词，用空格隔开

    //TODO
    public static final String ADD_TODO = "lg/todo/add/json";//新增一个todo 方法：POST 参数：title: 新增标题（必须）content: 新增详情（必须）date: 2018-08-01 预定完成时间（不传默认当天，建议传）type: 大于0的整数（可选）；priority 大于0的整数（可选）；

    public static final String UPDATE_TODO = "lg/todo/update/{id}/json";//更新一个todo 方法：POST 参数：id: 拼接在链接上，为唯一标识，列表数据返回时，每个todo 都会有个id标识 （必须）title: 更新标题 （必须）content: 新增详情（必须）date: 2018-08-01（必须）status: 0 // 0为未完成，1为完成type: ；priority: ； 如果有当前状态没有携带，会被默认值更新，比如当前 status=1，更新时没有带上，会认为被重置。

    public static final String DELETE_TODO = "lg/todo/delete/{id}/json";//删除一个Todo 方法：POST 参数：id

    public static final String UPDATE_DONE_TODO = "lg/todo/done/{id}/json";//仅更新完成状态Todo 方法：POST 参数：id: 拼接在链接上，为唯一标识status: 0或1，传1代表未完成到已完成，反之则反之。 只会变更status，未完成->已经完成 or 已经完成->未完成。

    public static final String TODO_LIST = "lg/todo/v2/list/{pageNum}/json";//列表TODO 方法：GET 参数 页码从1开始，拼接在url 上status 状态， 1-完成；0未完成; 默认全部展示；type 创建时传入的类型, 默认全部展示priority 创建时传入的优先级；默认全部展示orderby 1:完成日期顺序；2.完成日期逆序；3.创建日期顺序；4.创建日期逆序(默认)；

}
