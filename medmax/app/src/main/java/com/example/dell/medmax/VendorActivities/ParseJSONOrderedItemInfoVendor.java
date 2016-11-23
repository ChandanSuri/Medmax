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
public class ParseJSONOrderedItemInfoVendor {
    private String recentOrdersList;
    private ArrayList<HashMap<String,String>> list = new ArrayList<>();
    public static final String PRODUCT_NAME_STR = "Product Name";
    public static final String PRODUCT_COST_STR = "Product Cost";
    public static final String PRODUCT_COMP_NAME_STR = "Product Company Name";
    public static final String PRODUCT_QTY_STR = "Product Qty";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_ID_STR = "OrderID";
    public static final String PRODUCT_STATUS_STR ="Product Status";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_COST = "product_cost";
    public static final String PRODUCT_COMP_NAME ="company_name";
    public static final String PRODUCT_QTY = "product_qty";
    public static final String PRODUCT_STATUS = "status";

    public ParseJSONOrderedItemInfoVendor(String list){
        this.recentOrdersList = list;
    }

    public ArrayList<HashMap<String,String>> parseJSON(){
        return parseJSONList();
    }

    private ArrayList<HashMap<String,String>> parseJSONList(){

        JSONObject jsonObject = null;
        JSONArray jItems = null;

        try {
            jsonObject = new JSONObject(recentOrdersList);
            jItems = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getItems(jItems);
        Log.d("In parseJSONList", list.toString());
        return list;
    }

    private void getItems(JSONArray jItems){

        int itemCount = jItems.length();
        HashMap<String,String> item = null;
        for (int i=0;i<itemCount;i++){
            try {
                item = getItem((JSONObject) jItems.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
    }

    private HashMap<String,String> getItem(JSONObject jItem){

        HashMap<String,String> item = new HashMap<>();
        String productName = null;
        String productCost = null;
        String orderId = null;
        String companyName = null;
        String productQty = null;
        String productStatus = null;

        try {
            productName = jItem.getString(PRODUCT_NAME);
            productCost = jItem.getString(PRODUCT_COST);
            orderId = jItem.getString(ORDER_ID);
            companyName = jItem.getString(PRODUCT_COMP_NAME);
            productQty = jItem.getString(PRODUCT_QTY);
            productStatus = jItem.getString(PRODUCT_STATUS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        item.put(PRODUCT_NAME_STR,productName);
        item.put(PRODUCT_COMP_NAME_STR,companyName);
        item.put(ORDER_ID_STR, orderId);
        item.put(PRODUCT_COST_STR,productCost);
        item.put(PRODUCT_QTY_STR,productQty);
        item.put(PRODUCT_STATUS_STR,productStatus);
        return item;
    }
}
