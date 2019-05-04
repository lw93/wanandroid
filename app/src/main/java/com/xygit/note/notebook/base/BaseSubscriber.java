package com.xygit.note.notebook.base;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonParseException;
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.constant.PreferencesConst;
import com.xygit.note.notebook.login.LoginActivity;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.util.PreferencesUtil;
import com.xygit.note.notebook.util.ToastUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public abstract class BaseSubscriber<T> extends Subscriber<CommResponse<T>> {

    protected Context context;
    protected boolean autoHandle;
    private Intent intent;

    public BaseSubscriber(Context context) {
        this(context, true, new Intent(context, LoginActivity.class));
    }

    public BaseSubscriber(Context context, boolean autoHandle, Intent intent) {
        this.context = context;
        this.autoHandle = autoHandle;
        this.intent = intent;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (!autoHandle) {
            onFail(e);
            return;
        }
        e.printStackTrace();
        if (e instanceof HttpException) {
             /*网络异常*/
            ToastUtil.showToast("网络异常");
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
             /*链接异常*/
            ToastUtil.showToast("网络连接异常");
        } else if (e instanceof JSONException || e instanceof JsonParseException) {
             /*json解析异常*/
            ToastUtil.showToast("数据解析异常");
        } else if (e instanceof UnknownHostException) {
            /*无法解析该域名异常*/
            ToastUtil.showToast("域名解析异常");
        } else {
            /*未知异常*/
            ToastUtil.showToast("请求服务器异常");
        }
    }

    @Override
    public void onNext(CommResponse<T> tCommResponse) {
        if (null != tCommResponse) {
            if (NoteBookConst.RESPONSE_SINGIN_INVALID == tCommResponse.getErrorCode()) {
                PreferencesUtil.removePreference(PreferencesConst.USER_NAME);
                PreferencesUtil.removePreference(PreferencesConst.PASSWORD);
                ActivityUtil.startActivity(context, intent);
                return;
            } else if (NoteBookConst.RESPONSE_SUCCESS != tCommResponse.getErrorCode()) {
                ToastUtil.showToast(tCommResponse.getErrorMsg());
                return;
            }
            onSucess(tCommResponse);
        } else {
            Observable.empty();
        }
    }

    protected abstract void onSucess(CommResponse<T> tCommResponse);

    protected void onFail(Throwable e) {

    }
}
