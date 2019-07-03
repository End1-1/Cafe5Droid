package com.example.cafe5droid.Structures;

import android.content.Context;

import com.example.cafe5droid.Classes.CPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SStaff {

    public static SStaff create(JSONObject o) {
        GsonBuilder builder = new GsonBuilder();
        Gson g = builder.create();
        SStaff staff = null;
        try {
            JSONArray u = o.getJSONArray("user");
            staff = g.fromJson(u.getJSONObject(0).toString(), SStaff.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return staff;
    }

    @SerializedName("f_id")
    public int id;

    @SerializedName("f_first")
    public String firstName;

    @SerializedName("f_last")
    public String lastName;

    @SerializedName("f_group")
    public int group;

    public String fullName() {
        return lastName + " " + firstName;
    }

    public static void unset(Context context) {
        SStaff staff = new SStaff();
        staff.id = 0;
        staff.group = 0;
        staff.firstName = "";
        staff.lastName = "";
        staff.saveToPreference(context);
    }

    public void saveToPreference(Context context) {
        CPref.setInt(context, "staff_id", id);
        CPref.setInt(context, "staff_group", group);
        CPref.setString(context, "staff_firstname", firstName);
        CPref.setString(context, "staff_lastname", lastName);
    }

    public boolean get(Context context) {
        id = CPref.getInt(context,"staff_id");
        group = CPref.getInt(context, "staff_group");
        firstName = CPref.getString(context, "staff_firstname");
        lastName = CPref.getString(context, "staff_lastname");
        return id > 0;
    }
}
