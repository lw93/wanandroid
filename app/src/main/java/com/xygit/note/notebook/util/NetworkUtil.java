package com.xygit.note.notebook.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xygit.note.notebook.base.BaseApplication;

/**
 * 网络判断
 *
 * @author Created by xiuyaun
 * @time on 2019/3/3
 */

public class NetworkUtil {

    public static final int NO_NETWORK = -1;

    /**
     * 判断是否打开网络
     *
     * @return
     */
    public static boolean isNetWorkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isAvailable();
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static int getNetWorkType() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isAvailable()) {
                return networkInfo.getType();
            }
        }
        return NO_NETWORK;
    }

    /**
     * 判断当前网络是否为wifi
     *
     * @return 如果为wifi返回true；否则返回false
     */
    public static boolean isWiFiConnected() {
        ConnectivityManager manager = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager != null ? manager.getActiveNetworkInfo() : null;
        return null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnectedOrConnecting();
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @return 如果mobile可用返回true；否则返回false
     */
    public static boolean isMobileConnected() {
        ConnectivityManager manager = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager != null ? manager.getActiveNetworkInfo() : null;
        return null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnectedOrConnecting();
    }
}
