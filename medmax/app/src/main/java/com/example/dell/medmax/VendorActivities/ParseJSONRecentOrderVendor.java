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
public class ParseJSONRecentOrderVendor {
    private String recentOrdersList;
    private ArrayList<HashMap<String,String>> list = new ArrayList<>();
    public static final String SHOPKEEPER_EMAIL_STR= "Email";
    public static final String TOTAL_COST_STR = "TotalCost";
    public static final String SHOPKEEPER_EMAIL = "shopkeeper_email_id";
    public static final String TOTAL_COST = "total_cost";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_ID_STR = "OrderID";
    public static final String STATUS="status";
    public static final String STATUS_STR="data";

    public ParseJSONRecentOrderVendor(String list){
        this.recentOrdersList = list;
    }

    public ArrayList<HashMap<String,String>> parseJSON(){
        return parseJSONList();
    }

    private ArrayList<HashMap<String,String>> parseJSONList(){

        JSONObject jsonObject = null;
        JSONArray jOrders = null;

        try {
            jsonObject = new JSONObject(recentOrdersList);
            jOrders = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getOrders(jOrders);
        Log.d("In parseJSONList", list.toString());
        return list;
    }

    private void getOrders(JSONArray jOrders){

        int orderCount = jOrders.length();
        HashMap<String,String> order = null;
        for (int i=0;i<orderCount;i++){
            try {
                order = getOrder((JSONObject) jOrders.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(order);
        }
    }

    private HashMap<String,String> getOrder(JSONObject jOrder){

        HashMap<String,String> order = new HashMap<>();
        String vendorEmail = null;
        String totalCost = null;
        String orderId = null;
        String status=null;
        try {
            totalCost = jOrder.getString(TOTAL_COST);
            vendorEmail = jOrder.getString(SHOPKEEPER_EMAIL);
            orderId = jOrder.getString(ORDER_ID);
            status=jOrder.getString(STATUS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        order.put(SHOPKEEPER_EMAIL_STR,vendorEmail);
        order.put(TOTAL_COST_STR,totalCost);
        order.put(ORDER_ID_STR, orderId);
        order.put(STATUS_STR,status);
        return order;
    }
}
