package com.example.cafe5droid.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class CDatabase extends SQLiteOpenHelper {
    public static final int dbversion = 4;
    private ContentValues contentValues;
    private SQLiteDatabase dbLite;

    public CDatabase(@Nullable Context context) {
        super(context, "cafe5droid", null, dbversion);
        dbLite = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table menu (menuname text, part1 text, part2 text, dishname text, " +
                "price text, store text,  adgcode text, print1 text, print2 text, " +
                "remind text, dishcolor text, typecolor text, comment text, img text)");
        db.execSQL("create table menuname (name text)");
        db.execSQL("create table hall (id integer, name text, settings integer)");
        db.execSQL("create table tables (id integer, name text, hall integer)");
    }

    public void noException(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            noException(db, "drop table menu");
            noException(db, "drop table menuname");
            noException(db, "drop table hall");
            noException(db, "drop table tables");
            onCreate(db);
        }
    }

    public ContentValues getContentValues() {
        if (contentValues == null) {
            contentValues = new ContentValues();
        }
        contentValues.clear();
        return contentValues;
    }

    public boolean exec(String sql) {
        try {
            dbLite.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Cursor select(String sql) {
        return dbLite.rawQuery(sql, null);
    }

    public boolean insert(String table) {
        try {
            dbLite.insertOrThrow(table, null, contentValues);
            contentValues.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        dbLite.close();
    }
}
