package com.example.dell.medmax.ShopkeeperActivities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chandan Suri on 11/13/2016.
 */
public class ParseJSONVendorInfo {

    private String vendorInfo;
    private ArrayList<HashMap<String,String>> list = new ArrayList<>();
    public static final String VENDOR_EMAIL_STR = "Email";
    public static final String VENDOR_NAME_STR = "Name";
    public static final String VENDOR_CONTACT_STR = "ContactNumber";
    public static final String VENDOR_ADDRESS_STR = "Address";
    public static final String VENDOR_EMAIL = "email";
    public static final String VENDOR_NAME = "name";
    public static final String VENDOR_CONTACT = "phone";
    public static final String VENDOR_ADDRESS = "address";

    public ParseJSONVendorInfo(String list){
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
        Log.d("In parseJSONList",list.toString());
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
            vendorName = jVendor.getString(VENDOR_NAME);
            vendorEmail = jVendor.getString(VENDOR_EMAIL);
            vendorContact = jVendor.getString(VENDOR_CONTACT);
            vendorAddress = jVendor.getString(VENDOR_ADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vendor.put(VENDOR_NAME_STR,vendorName);
        vendor.put(VENDOR_EMAIL_STR,vendorEmail);
        vendor.put(VENDOR_ADDRESS_STR,vendorAddress);
        vendor.put(VENDOR_CONTACT_STR,vendorContact);

        return vendor;
    }
}
