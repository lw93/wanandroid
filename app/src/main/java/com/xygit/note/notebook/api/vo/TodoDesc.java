package com.xygit.note.notebook.api.vo;


import java.io.Serializable;

/**
 * TODO实体
 *
 * @author Created by xiuyaun
 * @time on 2019/3/10
 */

public class TodoDesc implements Serializable {
    private static final long serialVersionUID = -7190307880165926276L;
    private Object completeDate;
    private String completeDateStr;
    private String content;
    private long date;
    private String dateStr;
    private int id;
    private int status;
    private String title;
    private int type;
    private int userId;
    private int priority;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Object getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Object completeDate) {
        this.completeDate = completeDate;
    }

    public String getCompleteDateStr() {
        return completeDateStr;
    }

    public void setCompleteDateStr(String completeDateStr) {
        this.completeDateStr = completeDateStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TodoDesc) {
            TodoDesc data = (TodoDesc) obj;
            return hashCode() == data.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int h = 17;
        if (dateStr != null) {
            h = h * 31 + dateStr.hashCode();
        }
        h = (int) (h * 31 + date);
        return h;
    }
}
