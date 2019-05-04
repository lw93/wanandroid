package com.xygit.note.notebook.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.net.URISyntaxException;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/16
 */

public class ActivityUtil {


    public static void startActivity(Context activity, Intent intent) {
        activity.startActivity(intent);
    }

    public static void startActivity(Context activity, Intent[] intents) {
        activity.startActivities(intents);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityWithThird(Context activity, Intent intent, OnActivityException onActivityException) {
        if (null != intent.resolveActivity(activity.getPackageManager())) {
            try {
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                onActivityException.onActivityException(e);
            }
            return;
        }
        onActivityException.onActivityException(new NullPointerException("启动的应用不存在！"));
    }

    public static void startActivityBySchemeURL(Activity activity, Uri uri, OnActivityException onActivityException) {
        Intent intent = null;
        try {
            intent = Intent.parseUri(uri.toString(), 0);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            intent.setSelector(null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (null != intent && null != intent.resolveActivity(activity.getPackageManager())) {
            try {
                //当启动的Activity设置了singleTop、singleTask时并且request Code小于0时不启动。
                activity.startActivityIfNeeded(intent, -1);
            } catch (ActivityNotFoundException e) {
                onActivityException.onActivityException(e);
            }
            return;
        }
        onActivityException.onActivityException(new NullPointerException("浏览器未安装,请到应用市场下载安装！"));
    }

    public interface OnActivityException {
        void onActivityException(Throwable throwable);
    }
}
