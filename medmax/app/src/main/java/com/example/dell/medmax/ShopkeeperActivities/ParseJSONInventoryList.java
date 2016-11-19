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
public class ParseJSONInventoryList {

    private String inventoryList;
    private ArrayList<HashMap<String,String>> list = new ArrayList<>();

    public static final String ITEM_NAME_STR = "Name";
    public static final String ITEM_PRICE_STR = "Price";
    public static final String ITEM_COMP_NAME_STR = "Company Name";
    public static final String ITEM_NAME = "itemname";
    public static final String ITEM_PRICE = "itemprice";
    public static final String ITEM_COMP_NAME = "companyname";

    public ParseJSONInventoryList(String list){
        this.inventoryList = list;
    }

    public ArrayList<HashMap<String,String>> parseJSON(){
        return parseJSONList();
    }

    private ArrayList<HashMap<String,String>> parseJSONList(){

        JSONObject jsonObject = null;
        JSONArray jItems = null;

        try {
            jsonObject = new JSONObject(inventoryList);
            jItems = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getInventory(jItems);
        Log.d("In parseJSONList",list.toString());
        return list;
    }

    private void getInventory(JSONArray jItems){

        int itemsCount = jItems.length();
        HashMap<String,String> item = null;
        for (int i=0;i<itemsCount;i++){
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
        String itemName = null;
        String itemPrice = null;
        String itemCompanyName = null;

        try {
            itemName = jItem.getString(ITEM_NAME);
            itemCompanyName = jItem.getString(ITEM_COMP_NAME);
            itemPrice = jItem.getString(ITEM_PRICE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        item.put(ITEM_NAME_STR,itemName);
        item.put(ITEM_COMP_NAME_STR,itemCompanyName);
        item.put(ITEM_PRICE_STR,itemPrice);

        return item;
    }
}
