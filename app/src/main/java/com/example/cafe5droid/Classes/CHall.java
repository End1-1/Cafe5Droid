package com.example.cafe5droid.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.cafe5droid.Structures.SHall;
import com.example.cafe5droid.Structures.STable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public final class CHall {
    public static ArrayList<SHall> listOfHall = new ArrayList<>();
    public static ArrayList<STable> listOfTables = new ArrayList<>();
    public static ArrayList<STable> listOfFilteredTables = new ArrayList<>();

    public boolean empty() {
        return (listOfHall.isEmpty() || listOfTables.isEmpty());
    }

    public static final SHall hall(int position) {
        return listOfHall.get(position);
    }

    public static final SHall getHall(int id) {
        for (int i = 0; i < listOfHall.size(); i++) {
            if (listOfHall.get(i).id == id) {
                return listOfHall.get(i);
            }
        }
        return null;
    }

    public static final STable table(int positioin) {
        return listOfFilteredTables.get(positioin);
    }

    public static final STable getTable(int id) {
        for (int i = 0; i < listOfTables.size(); i++) {
            if (listOfTables.get(i).id == id) {
                return listOfTables.get(i);
            }
        }
        return null;
    }

    public static final void filterTables(int hall) {
        listOfFilteredTables.clear();
        for (int i = 0; i < listOfTables.size(); i++) {
            STable t = listOfTables.get(i);
            if (t.hall == hall) {
                listOfFilteredTables.add(t);
            }
        }
    }

    public void setupHall(Context context, JSONArray ha, JSONArray ta) {
        GsonBuilder builder = new GsonBuilder();
        Gson g = builder.create();
        listOfFilteredTables.clear();
        listOfTables.clear();
        listOfHall.clear();
        try {
            for (int i = 0; i < ha.length(); i++) {
                SHall h = g.fromJson(ha.getJSONObject(i).toString(), SHall.class);
                listOfHall.add(h);
            }
            for (int i = 0; i< ta.length(); i++) {
                STable t = g.fromJson(ta.getJSONObject(i).toString(), STable.class);
                listOfTables.add(t);
            }
            saveData(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveData(Context context) {
        CDatabase db = new CDatabase(context);
        db.exec("delete from hall");
        db.exec("delete from tables");
        ContentValues v = db.getContentValues();
        for (SHall h: listOfHall) {
            v.put("id", h.id);
            v.put("name", h.name);
            v.put("settings", h.settings);
            db.insert("hall");
        }
        for (STable t: listOfTables) {
            v.put("id", t.id);
            v.put("hall", t.hall);
            v.put("name", t.name);
            v.put("header", t.header);
            db.insert("tables");
        }
    }

    public static void loadFromDB(Context context) {
        listOfHall.clear();
        listOfTables.clear();
        listOfFilteredTables.clear();
        CDatabase db = new CDatabase(context);
        Cursor c = db.select("select id, name, settings from hall");
        while (c.moveToNext()) {
            SHall hall = new SHall();
            hall.id = c.getInt(0);
            hall.name = c.getString(1);
            hall.settings = c.getInt(2);
            listOfHall.add(hall);
        }
        c = db.select("select id, name, hall, header from tables");
        while (c.moveToNext()) {
            STable t = new STable();
            t.id = c.getInt(0);
            t.name = c.getString(1);
            t.hall = c.getInt(2);
            t.header = c.getString(3);
            listOfTables.add(t);
        }
    }
}
