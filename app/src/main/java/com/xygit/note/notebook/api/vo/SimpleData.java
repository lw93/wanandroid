package com.xygit.note.notebook.api.vo;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 简单数据的bean
 *
 * @author Created by xiuyaun
 * @time on 2019/3/10
 */

public class SimpleData implements Serializable {
    private static final long serialVersionUID = 1327195412905701858L;
    private String icon;
    private int id;
    private String link;
    private String name;
    private int order;
    private int visible;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SimpleData) {
            SimpleData data = (SimpleData) o;
            return hashCode() == data.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int h = 17;
        if (!TextUtils.isEmpty(icon)) {
            h = 31 * h + icon.hashCode();
        }
        if (!TextUtils.isEmpty(name)) {
            h = 31 * h + name.hashCode();
        }
        if (!TextUtils.isEmpty(link)) {
            h = 31 * h + link.hashCode();
        }
        h = 31 * h + id;
        h = 31 * h + userId;
        h = 31 * h + visible;
        h = 31 * h + order;
        return h;
    }
}
