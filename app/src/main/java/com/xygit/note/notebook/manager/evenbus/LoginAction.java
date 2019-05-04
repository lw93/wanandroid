package com.xygit.note.notebook.manager.evenbus;

import com.xygit.note.notebook.api.vo.LoginResult;

/**
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class LoginAction {
    private String message;
    private int type;
    private LoginResult data;

    public LoginAction() {
    }

    public LoginResult getData() {
        return data;
    }

    public void setData(LoginResult data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
