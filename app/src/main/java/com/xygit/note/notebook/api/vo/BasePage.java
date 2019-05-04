package com.xygit.note.notebook.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页bean ，例如某个公众号历史数据
 *
 * @author Created by xiuyaun
 * @time on 2019/3/10
 */

public class BasePage<D> implements Serializable {
    private static final long serialVersionUID = -4374810248392023753L;

    private int curPage;
    private List<D> datas;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<D> getDatas() {
        return datas;
    }

    public void setDatas(List<D> datas) {
        this.datas = datas;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
