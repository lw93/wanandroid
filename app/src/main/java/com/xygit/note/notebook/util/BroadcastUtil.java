package com.xygit.note.notebook.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/17
 */

public class BroadcastUtil {

    private static LocalBroadcastManager localBroadcastManager;

    public static void registerReceiverByLocal(Context context, @NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        if (null == localBroadcastManager) {
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
        }
        localBroadcastManager.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiverByLocal(@NonNull BroadcastReceiver receiver) {
        if (null != localBroadcastManager) {
            localBroadcastManager.unregisterReceiver(receiver);
        }
    }

    public static void sendBroadcastByLocal(@NonNull Intent broadcast) {
        if (null != localBroadcastManager) {
            localBroadcastManager.sendBroadcast(broadcast);
        }
    }

    public static void registerReceiverByGlobal(Context context, BroadcastReceiver receiver, IntentFilter filter) {
        context.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiverByGlobal(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    public static void sendBroadcastByGlobal(Context context, Intent broadcast) {
        context.sendBroadcast(broadcast);
    }
}
