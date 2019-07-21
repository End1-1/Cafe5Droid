package com.cafe5.cafe5droid.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class CDatabase extends SQLiteOpenHelper {
    public static final int dbversion = 6;
    private ContentValues contentValues;
    private SQLiteDatabase dbLite;
    private Cursor cursor;

    public CDatabase(@Nullable Context context) {
        super(context, "cafe5droid", null, dbversion);
        dbLite = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table menu (menuname text, part1 text, part2 text, dishcode, dishname text, " +
                "price text, store text,  adgcode text, print1 text, print2 text, " +
                "remind text, dishcolor text, typecolor text, comment text, img text)");
        db.execSQL("create table menuname (name text)");
        db.execSQL("create table hall (id integer, name text, settings integer)");
        db.execSQL("create table tables (id integer, name text, hall integer, header text)");
        db.execSQL("create table body (id integer primary key autoincrement, tableid integer, header text, bodyid text, dishcode text, dishname text, " +
                "qty1 text, qty2 text, price text, service text, discount text, total text, store text, " +
                "print1 text, print2 text, comment text, remind text, adgcode text, removereason text)");
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
            noException(db, "drop table body");
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
        cursor = dbLite.rawQuery(sql, null);
        return cursor;
    }

    public boolean moveToNext() {
        if (cursor == null) {
            return false;
        }
        return cursor.moveToNext();
    }

    public int getInt(String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public String getString(String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
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

    public int insertWithId(String table) {
        if (insert(table)) {
            Cursor c = dbLite.rawQuery("select last_insert_rowid()", null);
            if (c.moveToLast()) {
                return c.getInt(0);
            }
        }
        return 0;
    }

    public void close() {
        dbLite.close();
    }
}
