package com.example.dell.medmax.ShopkeeperActivities.model;

/**
 * Created by Chandan Suri on 11/13/2016.
 */
public class Order {

    String itemName;
    float itemPrice;
    String itemCompName;
    int itemQty;
    float itemTotalPrice;

    public String getItemName(){
        return itemName;
    }

    public String getItemCompName(){
        return itemCompName;
    }

    public float getItemPrice(){
        return itemPrice;
    }

    public int getItemQty(){
        return itemQty;
    }

    public float getItemTotalPrice(){
        return itemTotalPrice;
    }

    public Order(String itemName, String itemCompName, float itemPrice, int itemQty, float itemTotalPrice){

        this.itemName = itemName;
        this.itemCompName = itemCompName;
        this.itemPrice = itemPrice;
        this.itemQty = itemQty;
        this.itemTotalPrice = itemPrice*itemQty;
    }

}
