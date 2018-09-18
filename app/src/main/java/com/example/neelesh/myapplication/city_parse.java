package com.example.neelesh.myapplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by Neelesh on 10/23/2017.
 */

public class city_parse {
    private static final String ns = null;

public static String parse(String jsonStr)throws JSONException {
    String city = null;
    JSONObject city_data = new JSONObject(jsonStr);
    String state = city_data.getString("state");
    String Cityname = city_data.getString("city");
    city = Cityname+"("+state+")";
    return city;
    }
}
