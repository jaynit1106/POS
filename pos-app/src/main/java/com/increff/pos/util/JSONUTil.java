package com.increff.pos.util;

import org.json.simple.JSONObject;

public class JSONUTil {

    public static JSONObject getJSONObject(String name,String data){
        JSONObject obj = new JSONObject();
        obj.put(name,data);
        return obj;
    }
}
