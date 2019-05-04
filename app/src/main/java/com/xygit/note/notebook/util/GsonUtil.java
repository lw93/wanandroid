package com.xygit.note.notebook.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Gson 临时操作类
 * @author Created by xiuyaun
 * @time on 2019/3/2
 */

public class GsonUtil {

    public static Gson build(Type type, Object typeAdapter) {
        GsonBuilder mGsonBuild = new GsonBuilder();
        mGsonBuild.registerTypeAdapter(type, typeAdapter);
        mGsonBuild.setPrettyPrinting();
        mGsonBuild.serializeNulls();
        mGsonBuild.setLenient();
        return mGsonBuild.create();
    }
}
