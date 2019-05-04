package com.xygit.note.notebook.manager.net;

import com.xygit.note.notebook.BuildConfig;
import com.xygit.note.notebook.base.BaseApplication;
import com.xygit.note.notebook.util.ExternalStorageUtil;

import java.io.File;

import okhttp3.Cache;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/3
 */

public class HttpFileCache {

    public static final long CACHE_SIZE = 10 * 1024 * 1024L; // 10 MB
    public static final String CACHE_FILE = "httpCache";
    private Cache mCache;

    private HttpFileCache() {
        if (ExternalStorageUtil.isExternalStorageWritable()) {
            mCache = new Cache(new File(BuildConfig.CACHE_PATH + File.separator + "httpCache"), CACHE_SIZE);
        } else {
            mCache = new Cache(new File(BaseApplication.getAppContext().getCacheDir() + File.separator + "httpCache"), CACHE_SIZE);
        }
    }

    public static HttpFileCache getInstance() {
        return HttpFileHelp.mHttpFileCache;
    }

    public Cache getHttpFileCache() {
        return mCache;
    }

    private static class HttpFileHelp {
        private static final HttpFileCache mHttpFileCache = new HttpFileCache();
    }
}
