package com.xygit.note.notebook.manager.net;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * cookie 缓存
 *
 * @author Created by xiuyaun
 * @time on 2019/3/3
 */

public class CookieCache implements CookieJar {

    private static final HashMap<HttpUrl, List<Cookie>> mCookieStore = new HashMap<>(7);

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
//        if (url.url().getPath().contains("/user/logout")) {
//            mCookieStore.clear();
//            return;
//        }
        mCookieStore.put(HttpUrl.parse(url.host()), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        List<Cookie> cookies = mCookieStore.get(HttpUrl.parse(url.host()));
        return null != cookies ? cookies : new ArrayList<Cookie>();
    }
}
