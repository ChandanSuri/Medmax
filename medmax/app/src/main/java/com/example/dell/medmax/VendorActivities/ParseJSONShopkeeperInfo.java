package com.example.dell.medmax.VendorActivities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aggarwal on 22-11-2016.
 */
public class ParseJSONShopkeeperInfo {
    private String vendorInfo;
    private ArrayList<HashMap<String,String>> list = new ArrayList<>();
    public static final String SHOPKEEPER_EMAIL_STR = "Email";
    public static final String SHOPKEEPER_NAME_STR = "Name";
    public static final String SHOPKEEPER_CONTACT_STR = "ContactNumber";
    public static final String SHOPKEEPER_ADDRESS_STR = "Address";
    public static final String SHOPKEEPER_EMAIL = "email";
    public static final String SHOPKEEPER_NAME = "name";
    public static final String SHOPKEEPER_CONTACT = "phone";
    public static final String SHOPKEEPER_ADDRESS = "address";

    public ParseJSONShopkeeperInfo(String list){
        this.vendorInfo = list;
    }

    public ArrayList<HashMap<String,String>> parseJSON(){
        return parseJSONList();
    }

    private ArrayList<HashMap<String,String>> parseJSONList(){

        JSONObject jsonObject = null;
        JSONArray jVendorInfo = null;

        try {
            jsonObject = new JSONObject(vendorInfo);
            jVendorInfo = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getVendorInfo(jVendorInfo);
        Log.d("In parseJSONList", list.toString());
        return list;
    }

    private void getVendorInfo(JSONArray jVendorInfo){

        int infoCount = jVendorInfo.length();
        HashMap<String,String> vendor = null;
        for (int i=0;i<infoCount;i++){
            try {
                vendor = getVendor((JSONObject) jVendorInfo.get(i));
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
        String vendorContact = null;
        String vendorAddress = null;

        try {
            vendorName = jVendor.getString(SHOPKEEPER_NAME);
            vendorEmail = jVendor.getString(SHOPKEEPER_EMAIL);
            vendorContact = jVendor.getString(SHOPKEEPER_CONTACT);
            vendorAddress = jVendor.getString(SHOPKEEPER_ADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vendor.put(SHOPKEEPER_NAME_STR,vendorName);
        vendor.put(SHOPKEEPER_EMAIL_STR,vendorEmail);
        vendor.put(SHOPKEEPER_ADDRESS_STR,vendorAddress);
        vendor.put(SHOPKEEPER_CONTACT_STR,vendorContact);

        return vendor;
    }
}
