package com.xygit.note.notebook.manager.evenbus;

import com.xygit.note.notebook.api.vo.TodoDesc;

/**
 * @author Created by xiuyaun
 * @time on 2019/4/14
 */

public class TodoAction {
    private String message;
    private int type;
    private TodoDesc data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TodoDesc getData() {
        return data;
    }

    public void setData(TodoDesc data) {
        this.data = data;
    }
}
