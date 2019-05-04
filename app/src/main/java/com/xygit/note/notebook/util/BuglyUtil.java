package com.xygit.note.notebook.util;

import android.text.TextUtils;

import com.tencent.bugly.crashreport.BuglyLog;
import com.xygit.note.notebook.BuildConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/24
 */

public class BuglyUtil {

    public static void logError(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            BuglyLog.e(tag, msg);
        }
    }

    public static void logDebug(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            BuglyLog.d(tag, msg);
        }
    }

    public static void logInfo(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            BuglyLog.i(tag, msg);
        }
    }

    public static void logVerbose(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            BuglyLog.v(tag, msg);
        }
    }

    public static void logWarn(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            BuglyLog.w(tag, msg);
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
