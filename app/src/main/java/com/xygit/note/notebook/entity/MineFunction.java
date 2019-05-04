package com.xygit.note.notebook.entity;

import android.support.annotation.DrawableRes;

/**
 * @author Created by xiuyaun
 * @time on 2019/4/9
 */

public class MineFunction {

    public MineFunction(@DrawableRes int drawbleRes, String title) {
        this.drawbleRes = drawbleRes;
        this.title = title;
    }

    private int drawbleRes;

    private String title;

    public int getDrawbleRes() {
        return drawbleRes;
    }

    public void setDrawbleRes(int drawbleRes) {
        this.drawbleRes = drawbleRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
