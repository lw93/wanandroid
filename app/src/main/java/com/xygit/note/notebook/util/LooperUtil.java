package com.xygit.note.notebook.util;

import android.os.Looper;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/23
 */

public class LooperUtil {

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

}
