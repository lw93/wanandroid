package com.xygit.note.notebook.api;

/**
 * 其中errorCode如果为负数则认为错误，此时errorMsg会包含错误信息。data为Object，返回数据根据不同的接口而变化。
 * errorCode = 0 代表执行成功，不建议依赖任何非0的 errorCode.
 * errorCode = -1001 代表登录失效，需要重新登录。
 *
 * @author Created by xiuyaun
 * @time on 2019/2/23
 */

public class CommResponse<T> {
    private T data;
    private int errorCode = -1000000;
    private String errorMsg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
