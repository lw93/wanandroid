package com.xygit.note.notebook.manager.net;


import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 *  非空序列化
 * @author Created by xiuyaun
 * @time on 2019/3/2
 */
public class NotEmptyStringSerializer implements JsonSerializer<String> {
    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        if (!TextUtils.isEmpty(src)) {
            return new JsonPrimitive(src);
        }
        return JsonNull.INSTANCE;
    }
}
