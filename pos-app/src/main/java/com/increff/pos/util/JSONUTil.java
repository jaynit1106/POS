package com.increff.pos.util;

import org.json.simple.JSONObject;

public class JSONUTil {

    public static JSONObject getJSONObject(String data){
        JSONObject obj = new JSONObject();
        obj.put("errors",data);
        return obj;
    }
}
