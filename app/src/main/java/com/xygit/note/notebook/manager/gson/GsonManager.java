package com.xygit.note.notebook.manager.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import java.lang.reflect.Type;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/2
 */

public class GsonManager {

    private GsonBuilder mGsonBuild;

    private GsonManager() {
    }

    public static GsonManager getInstance() {
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager builder() {
        mGsonBuild = GsonManageHelp.mGsonInstance;
        mGsonBuild.setPrettyPrinting();
        mGsonBuild.serializeNulls();
        mGsonBuild.setLenient();
        mGsonBuild.setVersion(1.0);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager setLongSerializationPolicy(LongSerializationPolicy serializationPolicy) {
        mGsonBuild.setLongSerializationPolicy(serializationPolicy);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        mGsonBuild.setFieldNamingStrategy(fieldNamingStrategy);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager setFieldNamingPolicy(FieldNamingPolicy namingConvention) {
        mGsonBuild.setFieldNamingPolicy(namingConvention);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager setExclusionStrategies(ExclusionStrategy... strategies) {
        mGsonBuild.setExclusionStrategies(strategies);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager setDateFormat(int style) {
        mGsonBuild.setDateFormat(style);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager setDateFormat(int dateStyle, int timeStyle) {
        mGsonBuild.setDateFormat(dateStyle, timeStyle);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager setDateFormat(String pattern) {
        mGsonBuild.setDateFormat(pattern);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager excludeFieldsWithModifiers(int... modifiers) {
        mGsonBuild.excludeFieldsWithModifiers(modifiers);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        mGsonBuild.addDeserializationExclusionStrategy(strategy);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        mGsonBuild.addSerializationExclusionStrategy(strategy);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager registerTypeAdapter(Type type, Object typeAdapter) {
        mGsonBuild.registerTypeAdapter(type, typeAdapter);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public GsonManager setVersion(double ignoreVersionsAfter) {
        mGsonBuild.setVersion(ignoreVersionsAfter);
        return GsonManageHelp.mGsonManagerInstance;
    }

    public Gson build() {
        return mGsonBuild.create();
    }

    private static class GsonManageHelp {
        private static final GsonBuilder mGsonInstance = new GsonBuilder();
        private static final GsonManager mGsonManagerInstance = new GsonManager();
    }
}
