package com.xygit.note.notebook.util;

import android.os.Environment;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/17
 */

public class ExternalStorageUtil {

    // 读/写检查
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // 只读检查
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
