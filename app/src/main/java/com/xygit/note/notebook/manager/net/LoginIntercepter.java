package com.xygit.note.notebook.manager.net;

import android.support.annotation.NonNull;

import com.xygit.note.notebook.api.vo.CommResponse;
import com.xygit.note.notebook.api.NoteBookUrl;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.constant.PreferencesConst;
import com.xygit.note.notebook.manager.gson.GsonManager;
import com.xygit.note.notebook.util.PreferencesUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Created by xiuyaun
 * @time on 2019/5/3
 */

public class LoginIntercepter implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response != null) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responeStr = response.body().string();
                CommResponse commResponse = GsonManager.getInstance().build().fromJson(responeStr, CommResponse.class);
                if (commResponse != null && NoteBookConst.RESPONSE_SINGIN_INVALID == commResponse.getErrorCode()) {
                    String name = PreferencesUtil.getPreference(PreferencesConst.USER_NAME, "");
                    String pass = PreferencesUtil.getPreference(PreferencesConst.PASSWORD, "");
                    FormBody.Builder requestBody = new FormBody.Builder();
                    requestBody.add("username", name);
                    requestBody.add("password", pass);
                    Request requestLogin = new Request.Builder()
                            .url(NoteBookUrl.WAN_ANDROID_HOST + NoteBookUrl.USER_LOGIN)
                            .method("POST", requestBody.build())
                            .build();
                    chain.proceed(requestLogin);
                }
                return chain.proceed(request);
            }
        }
        return response;
    }
}
