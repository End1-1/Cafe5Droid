package com.cafe5.cafe5droid.Structures;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class STable implements Serializable {

    @SerializedName("f_id")
    public int id;

    @SerializedName("f_name")
    public String name;

    @SerializedName("f_hall")
    public int hall;

    public String header;

    public STable () {
        header = "";
    }
}
