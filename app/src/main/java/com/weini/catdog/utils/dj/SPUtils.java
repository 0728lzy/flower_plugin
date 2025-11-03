package com.weini.catdog.utils.dj;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.weini.catdog.APP;

/**
 * desc: SP缓存类
 */
public class SPUtils {
    private static SPUtils util;
    public static String SP_UNINSTALL = "uninstall";
    public static String SP_WALLPAPER = "wallpaper_set";
    public static String SP_INSTALL_TIME = "sp_install_time";
    public static String SP_BAIDU_ID = "baiduId";

    public static SPUtils getInstance() {
        if (util == null) {
            util = new SPUtils();
        }
        return util;

    }

    private SPUtils() {
        super();
    }

    private String name = APP.Companion.getInstance().getPackageName();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInt(String key) {

        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);

        return sp.getInt(key, 0);
    }

    public void setInt(String key, int num) {

        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(key, num);
        editor.commit();

    }

    public void setLong(String key, Long num) {

        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong(key, num);
        editor.commit();

    }

    public long getLong(String key) {

        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sp.getLong(key, 0L);
    }

    public void setFloat(String key, Float num) {

        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putFloat(key, num);
        editor.commit();

    }

    public Float getFloat(String key) {

        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sp.getFloat(key, 0F);
    }

    public String getString(String key) {
        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public String getString(String key, String defaultVal) {

        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);

        return sp.getString(key, defaultVal);
    }

    public void setString(String key, String str) {

        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, str);
        editor.commit();
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = APP.Companion.getInstance().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }
}
