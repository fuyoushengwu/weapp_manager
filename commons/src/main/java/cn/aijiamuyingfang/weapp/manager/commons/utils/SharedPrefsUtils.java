package cn.aijiamuyingfang.weapp.manager.commons.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;

/**
 * Created by pc on 2018/3/31.
 */

public final class SharedPrefsUtils {

    private SharedPrefsUtils() {
    }

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_data";

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = obtainPrefEditor();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences preferences = obtainPref();
        return preferences.getString(key, defaultValue);
    }

    public static void putStringSet(String key, Set<String> cookies) {
        SharedPreferences.Editor editor = obtainPrefEditor();
        editor.putStringSet(key, cookies);
        editor.apply();
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        SharedPreferences preferences = obtainPref();
        return preferences.getStringSet(key, defaultValue);
    }


    public static long getLong(String key, long defaultValue) {
        SharedPreferences preferences = obtainPref();
        return preferences.getLong(key, defaultValue);
    }

    public static void setLong(String key, long value) {
        SharedPreferences.Editor editor = obtainPrefEditor();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取SharedPreferences.Editor对象
     *
     * @return
     */
    private static SharedPreferences.Editor obtainPrefEditor() {
        return obtainPref().edit();
    }

    /**
     * 获取SharedPreferences对象
     *
     * @return
     */
    private static SharedPreferences obtainPref() {
        Context context = CommonApp.getApplication();
        return context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
    }
}
