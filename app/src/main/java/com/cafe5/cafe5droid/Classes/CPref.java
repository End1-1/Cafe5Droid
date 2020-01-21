package com.cafe5.cafe5droid.Classes;

import android.content.Context;
import android.content.SharedPreferences;

public final class CPref {

    public static String serverIP;
    public static int serverPort;
    public static String menuName;
    public static int currentHall;
    public static int lastTable;
    public static String settingsPassword;
    public static int lastMessage;
    public static float serviceValue;
    public static boolean backFromOrder = false;

    private static final String prefName  = "CAFE5DROID";

    public static void init(Context context) {
        serverIP = getString(context, "server_id");
        serverPort = getInt(context, "server_port");
        menuName = getString(context,"menu");
        currentHall = getInt(context, "current_hall");
        lastTable = getInt(context, "last_table");
        settingsPassword = getString(context, "settings_password");
        lastMessage = getInt(context, "lastmessage");
        serviceValue = getFloat(context, "servicevalue");
    }

    private static SharedPreferences pref(Context context) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(Context context, String key) {
        return pref(context).getBoolean(key, false);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor e = pref(context).edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public static int getInt(Context context, String key) {
        return pref(context).getInt(key, 0);
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences.Editor e = pref(context).edit();
        e.putInt(key, value);
        e.commit();
    }

    public static float getFloat(Context context, String key) {
        return pref(context).getFloat(key, 0);
    }

    public static void setFloat(Context context, String key, float value) {
        SharedPreferences.Editor e = pref(context).edit();
        e.putFloat(key, value);
        e.commit();
    }

    public static String getString(Context context, String key) {
        return pref(context).getString(key, "");
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences.Editor e = pref(context).edit();
        e.putString(key, value);
        e.commit();
    }

    public static void setCurrentHall(Context context, int id) {
        currentHall = id;
        setInt(context, "current_hall", id);
    }

    public static void setLastTable(Context context, int id) {
        lastTable = id;
        setInt(context, "last_table", id);
    }

    public static void setSettingsPassword(Context context, String pass) {
        settingsPassword = pass;
        setString(context, "settings_password", pass);
    }

    public static void setLastMessage(Context context, int msg) {
        lastMessage = msg;
        setInt(context, "lastmessage", msg);
    }

    public static void setServiceValue(Context context, float value) {
        serviceValue = value;
        setFloat(context, "servicevalue", value);
    }
}
