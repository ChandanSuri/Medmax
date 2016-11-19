package com.example.dell.medmax.ShopkeeperActivities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chandan Suri on 11/12/2016.
 */
public class ParseJSONVendorList {

    private String vendorList;
    private ArrayList<HashMap<String,String>> list = new ArrayList<>();
    public static final String VENDOR_EMAIL_STR = "Email";
    public static final String VENDOR_NAME_STR = "Name";
    public static final String VENDOR_EMAIL = "email";
    public static final String VENDOR_NAME = "name";

    public ParseJSONVendorList(String list){
        this.vendorList = list;
    }

    public ArrayList<HashMap<String,String>> parseJSON(){
        return parseJSONList();
    }

    private ArrayList<HashMap<String,String>> parseJSONList(){

        JSONObject jsonObject = null;
        JSONArray jVendors = null;

        try {
            jsonObject = new JSONObject(vendorList);
            jVendors = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getVendors(jVendors);
        Log.d("In parseJSONList",list.toString());
        return list;
    }

    private void getVendors(JSONArray jVendors){

        int vendorsCount = jVendors.length();
        HashMap<String,String> vendor = null;
        for (int i=0;i<vendorsCount;i++){
            try {
                vendor = getVendor((JSONObject) jVendors.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(vendor);
        }
    }

    private HashMap<String,String> getVendor(JSONObject jVendor){

        HashMap<String,String> vendor = new HashMap<>();
        String vendorName = null;
        String vendorEmail = null;

        try {
            vendorName = jVendor.getString(VENDOR_NAME);
            vendorEmail = jVendor.getString(VENDOR_EMAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vendor.put(VENDOR_NAME_STR,vendorName);
        vendor.put(VENDOR_EMAIL_STR,vendorEmail);

        return vendor;
    }
}
