package com.xygit.note.notebook.util;

import android.content.SharedPreferences;

import com.xygit.note.notebook.base.BaseApplication;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/23
 */

public class PreferencesUtil {

    private static final String PREFIX_PREFERENCES = "prefix_";

    private static SharedPreferences preferences;

    public static SharedPreferences getDefaultPreferences() {
        if (null == preferences) {
            preferences = BaseApplication.getDefaultPreferences();
        }
        return preferences;
    }


    public static void addPreference(String key, String value) {
        SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.putString(PREFIX_PREFERENCES + key, value);
        editor.apply();
    }


    public static void addPreference(String key, int value) {
        SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.putInt(PREFIX_PREFERENCES + key, value);
        editor.apply();
    }

    public static void addPreference(String key, Boolean value) {
        SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.putBoolean(PREFIX_PREFERENCES + key, value);
        editor.apply();
    }

    public static void addPreference(String key, Float value) {
        SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.putFloat(PREFIX_PREFERENCES + key, value);
        editor.apply();
    }

    public static void addPreference(String key, Long value) {
        SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.putLong(PREFIX_PREFERENCES + key, value);
        editor.apply();
    }

    public static String getPreference(String key, String value) {
        SharedPreferences preferences = getDefaultPreferences();
        return preferences.getString(PREFIX_PREFERENCES + key, value);
    }


    public static int getPreference(String key, int value) {
        SharedPreferences preferences = getDefaultPreferences();
        return preferences.getInt(PREFIX_PREFERENCES + key, value);
    }

    public static boolean getPreference(String key, Boolean value) {
        SharedPreferences preferences = getDefaultPreferences();
        return preferences.getBoolean(PREFIX_PREFERENCES + key, value);
    }

    public static float getPreference(String key, Float value) {
        SharedPreferences preferences = getDefaultPreferences();
        return preferences.getFloat(PREFIX_PREFERENCES + key, value);
    }

    public static long getPreference(String key, Long value) {
        SharedPreferences preferences = getDefaultPreferences();
        return preferences.getLong(PREFIX_PREFERENCES + key, value);
    }

    public static void removePreference(String key) {
        SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.remove(PREFIX_PREFERENCES + key);
        editor.apply();
    }

    public static void clearPreference(String key) {
        SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.clear();
        editor.apply();
    }
}
