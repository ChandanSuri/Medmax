package com.example.dell.medmax;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bhaskar Majithia on 11-10-2016.
 */
public class ParseJSON{
    public static String[] company;
    public static String[] item;
    public static String[] price;
    String usermail;
    public static final String JSON_ARRAY = "data";
    public static final String KEY_COMPANY = "companyname";
    public static final String KEY_NAME = "itemname";
    public static final String KEY_PRICE = "itemprice";
    public static final String KEY_EMAIL = "vendorsemail";

    private JSONArray users = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(String usermail){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            int k,l;
            k=0;
            for(l=0;l<users.length();l++){
                JSONObject jo = users.getJSONObject(l);
                if (usermail.equalsIgnoreCase(jo.getString(KEY_EMAIL))) {
                    k++;
                }
            }
            company = new String[k];
            item = new String[k];
            price = new String[k];
            int j,i;
            j=0;
            for(i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                if (usermail.equalsIgnoreCase(jo.getString(KEY_EMAIL))) {
                    company[j] = jo.getString(KEY_COMPANY);
                    item[j] = jo.getString(KEY_NAME);
                    price[j] = jo.getString(KEY_PRICE);
                    j++;
                }
            }
            System.out.println("++++++++++++++++++++++++++++++++++++"+j+"+++++++++++++++++"+i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
