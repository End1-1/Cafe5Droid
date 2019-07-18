package com.example.cafe5droid.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.cafe5droid.Structures.SDish;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class CMenu {

    public static ArrayList<SDish> listOfDishes = new ArrayList<>();
    public static ArrayList<String> listOfPart1 = new ArrayList<>();
    public static ArrayList<String> listOfMenu = new ArrayList<>();
    public static ArrayList<String> filterPart2 = new ArrayList<>();
    public static ArrayList<SDish> filterDish = new ArrayList<>();

    public static void processDishes(JSONArray ja) {
        listOfDishes.clear();
        listOfPart1.clear();
        GsonBuilder builder = new GsonBuilder();
        Gson g = builder.create();
        try {
            SortedSet<String> p1 = new TreeSet<>();
            SortedSet<String> p2 = new TreeSet<>();
            SortedSet<String> m = new TreeSet<>();
            for (int i = 0; i < ja.length(); i++) {
                SDish d = g.fromJson(ja.getJSONObject(i).toString(), SDish.class);
                listOfDishes.add(d);
                p1.add(d.part1);
                p2.add(d.part2);
                m.add(d.menu);
            }
            listOfPart1 = new ArrayList<>(p1);
            listOfMenu = new ArrayList<>(m);
            filterPart2 = new ArrayList<>(p2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void save(Context context) {
        CDatabase db = new CDatabase(context);
        ContentValues v = db.getContentValues();
        db.exec("delete from menu");
        db.exec("delete from menuname");
        for (int i = 0; i < listOfDishes.size(); i++) {
            SDish d = listOfDishes.get(i);
            v.put("menuname", d.menu);
            v.put("part1", d.part1);
            v.put("part2", d.part2);
            v.put("dishcode", d.id);
            v.put("dishname", d.name);
            v.put("price", d.price);
            v.put("store", d.store);
            v.put("print1", d.print1);
            v.put("print2", d.print2);
            v.put("dishcolor", d.color);
            v.put("typecolor", d.typeColor);
            v.put("adgcode", d.adgCode);
            v.put("remind", d.remind);
            v.put("comment", d.description);
            v.put("img", d.image);
            db.insert("menu");
        }
        db.close();
    }

    public static void loadFromDB(Context context) {
        CDatabase db = new CDatabase(context);
        Cursor c = db.select("select menuname, part1, part2, dishname, price, comment, dishcode from menu");
        SortedSet<String> p1 = new TreeSet<>();
        SortedSet<String> p2 = new TreeSet<>();
        SortedSet<String> m = new TreeSet<>();
        while (c.moveToNext()) {
            SDish d = new SDish();
            d.menu = c.getString(0);
            d.part1 = c.getString(1);
            d.part2 = c.getString(2);
            d.name = c.getString(3);
            d.price = c.getString(4);
            d.description = c.getString(5);
            d.id = c.getString(6);
            p1.add(d.part1);
            p2.add(d.part2);
            m.add(d.menu);
            listOfDishes.add(d);
        }
        listOfPart1 = new ArrayList<>(p1);
        filterPart2 = new ArrayList<>(p2);
        listOfMenu = new ArrayList<>(m);
    }

    public static void filter(String menu, String part1, String part2) {
        filterDish.clear();
        SortedSet<String> p = new TreeSet<>();
        for (SDish d: listOfDishes) {
            if (menu != null) {
                if (!d.menu.equals(menu)) {
                    continue;
                }
            }
            if (part1 != null) {
                if (!d.part1.equals(part1)) {
                    continue;
                } else {
                    p.add(d.part2);
                }
            } else {
                p.add(d.part2);
            }
            if (part2 != null) {
                if (!d.part2.equals(part2)) {
                    continue;
                }
            }
            filterDish.add(d);
        }
        filterPart2 = new ArrayList<>(p);
    }
}
