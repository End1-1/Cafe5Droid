package com.example.cafe5droid.Structures;

import com.google.gson.annotations.SerializedName;

public class SDish {
    @SerializedName("menu_name")
    public String menu;

    @SerializedName("f_dish")
    public String id;

    @SerializedName("part1")
    public String part1;

    @SerializedName("part2")
    public String part2;

    @SerializedName("f_name")
    public String name;

    @SerializedName("f_adgcode")
    public String adgCode;

    @SerializedName("f_price")
    public String price;

    @SerializedName("f_store")
    public String store;

    @SerializedName("f_print1")
    public String print1;

    @SerializedName("f_print2")
    public String print2;

    @SerializedName("dish_color")
    public String color;

    @SerializedName("type_color")
    public String typeColor;

    @SerializedName("f_remind")
    public String remind;

    @SerializedName("f_description")
    public String description;

    public String qty1;

    public String image;

    public SDish() {
        qty1 = "1";
    }
}
