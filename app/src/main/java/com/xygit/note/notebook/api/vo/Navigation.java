package com.xygit.note.notebook.api.vo;

import java.io.Serializable;
import java.util.List;

/** 导航条数据
 * @author Created by xiuyaun
 * @time on 2019/3/10
 */

public class Navigation implements Serializable {
    private static final long serialVersionUID = 5804828122924501580L;
    private List<CommonData> articles;
    private int cid;
    private String name;

    public List<CommonData> getArticles() {
        return articles;
    }

    public void setArticles(List<CommonData> articles) {
        this.articles = articles;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
