package com.xygit.note.notebook.api.vo;

import java.io.Serializable;

/** 标签 bean
 * @author Created by xiuyaun
 * @time on 2019/3/10
 */

public class CommonTag implements Serializable {
    private static final long serialVersionUID = -5254395816885666872L;
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
