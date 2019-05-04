package com.xygit.note.notebook.manager.net;


import android.support.annotation.NonNull;

import com.xygit.note.notebook.util.NetworkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http 缓存
 *
 * @author Created by xiuyaun
 * @time on 2019/3/3
 */

public class HttpCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isNetWorkAvailable()) { // 无网络时 设置超时为1周
            CacheControl FORCE_CACHE = new CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(60 * 60 * 12, TimeUnit.SECONDS)
                    .minFresh(60,TimeUnit.MINUTES)
                    .build();
            request = request.newBuilder().cacheControl(FORCE_CACHE).build();
            return chain.proceed(request);
        }
        Response originalResponse = chain.proceed(request);
        // 有网络时 设置缓存为默认值
        String cacheControl = request.cacheControl().toString();
        return originalResponse.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .build();

        /* int maxStale = 60 * 60 * 24 * 7;
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();*/
    }
}
