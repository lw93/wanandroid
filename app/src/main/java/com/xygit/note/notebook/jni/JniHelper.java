package com.xygit.note.notebook.jni;


import android.content.Context;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/9
 */

public class JniHelper {

    static {
        System.loadLibrary("native-debug");
    }

    public  static native boolean debuggable(Context context);
}
