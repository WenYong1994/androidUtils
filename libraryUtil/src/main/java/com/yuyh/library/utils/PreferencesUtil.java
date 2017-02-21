package com.yuyh.library.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtil {
    public static SharedPreferences sp = null;
    public static String temp = null;
    public static Editor editor;

    /**
     * 录入键值对到SharedPreferences
     *
     * @param filename 记录目标文件名
     * @param name     记录变量名
     * @param content      记录变量值
     * @param context  当前的context
     * @return 未记录返回false, 记录成功返回true
     */
    public static boolean record(String filename, String name, String content,
                                 Context context) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        temp = sp.getString(name, "");
        if (temp == null || temp.equals("")) {
            editor = sp.edit();
            editor.remove(name);
            editor.putString(name, content);
            editor.commit();
            return true;
        }
        return false;
    }

    /**
     * 录入Map对到SharedPreferences
     *
     * @param filename 记录目标文件名
     * @param map      记录MAP
     * @param context  当前的context
     * @return
     */
    public static void record(String filename, Map<String, String> map,
                              Context context) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        editor = sp.edit();
        Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            if (entry.getKey() != null && !entry.getKey().toString().equals("")) {
                editor.remove(entry.getKey());
                editor.putString(entry.getKey(), entry.getValue());
            }
        }
        editor.commit();
    }


    /**
     * 获取SharedPreferences
     *
     * @param filename 目标文件名
     * @param context  当前的context
     * @return 返回变量的值，若无返回空
     */
    public static Map<String, ?> getAllRecord(String filename, Context context) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);

        Map<String, ?> map = sp.getAll();

        return map;
    }

    /**
     * 获取SharedPreferences
     *
     * @param filename 目标文件名
     * @param name     变量名
     * @param context  当前的context
     * @return 返回变量的值，若无返回空
     */
    public static String getrecord(String filename, String name, Context context) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        temp = sp.getString(name, "");
        if (temp != null && !temp.equals("")) {
            return temp;
        }
        return "";
    }

    /**
     * 删除目标SharedPreferences
     *
     * @param filename 目标文件名
     * @param name     要删除的变量名
     * @param context  当前的context
     * @return 删除成功返回true, 失败返回false
     */
    public static boolean removerecord(String filename, String name,
                                       Context context) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        temp = sp.getString(name, "");
        if (temp != null && !temp.equals("")) {
            editor = sp.edit();
            editor.remove(name);
            editor.commit();
            return true;
        }
        return false;
    }

    /**
     * 清除目标SharedPreferences文件
     *
     * @param filename 目标文件名
     * @param context  当前的context
     * @return 删除成功返回true, 失败返回false
     */
    public static boolean clearRecord(String filename, Context context) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        if (temp != null && !temp.equals("")) {
            editor = sp.edit();
            editor.clear();
            editor.commit();
            return true;
        }
        return false;
    }

}
