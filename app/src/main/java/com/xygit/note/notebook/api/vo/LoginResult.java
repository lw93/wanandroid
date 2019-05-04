package com.xygit.note.notebook.api.vo;

import java.io.Serializable;

/** 登陆返回bean
 * @author Created by xiuyaun
 * @time on 2019/3/10
 */

public class LoginResult implements Serializable {
    private static final long serialVersionUID = 548913363134820302L;
    private Object[] chapterTops;
    private int[] collectIds;
    private String email;
    private String icon;
    private int id;
    private String password;
    private String token;
    private int type;
    private String username;

    public Object[] getChapterTops() {
        return chapterTops;
    }

    public void setChapterTops(Object[] chapterTops) {
        this.chapterTops = chapterTops;
    }

    public int[] getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(int[] collectIds) {
        this.collectIds = collectIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
