package com.xygit.note.notebook.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.annotation.NonNull;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/17
 */

public class ServiceUtil {

    public static void startService(Context context, Intent service) {
        context.startService(service);
    }

    @SuppressLint("NewApi")
    public static void startForegroundService(Context context, Intent service) {
        context.startForegroundService(service);
    }

    public static void stopService(Context context, Intent service) {
        context.stopService(service);
    }

    public static void bindService(Context context, Intent service, ServiceConnection connection, int flags) {
        context.bindService(service, connection, flags);
    }


    public static void unbindService(Context context, @NonNull ServiceConnection connection) {
        context.unbindService(connection);
    }
}
