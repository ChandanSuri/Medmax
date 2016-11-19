package com.example.dell.medmax.ShopkeeperActivities.db;

/**
 * Created by Chandan Suri on 11/13/2016.
 */
public class OrderTable extends DbTable {

    public static final String TABLE_NAME = "OrderPlaced";
    public interface Columns{
        String ITEM_NAME = "item_name";
        String ITEM_PRICE = "item_price";
        String ITEM_QTY = "item_qty";
        String ITEM_COMP_NAME = "item_company_name";
        String ITEM_TOTAL_PRICE = "item_total_price";
    }

    public static final String TABLE_CREATE_CMD = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME
            +LBR
            +Columns.ITEM_NAME+TYPE_STRING_PK+COMMA
            +Columns.ITEM_COMP_NAME+TYPE_TEXT+COMMA
            +Columns.ITEM_PRICE+TYPE_REAL+COMMA
            +Columns.ITEM_QTY+TYPE_INT+COMMA
            +Columns.ITEM_TOTAL_PRICE+TYPE_REAL
            +RBR+" ;";
    public static final String DELETE_ITEM = "DELETE FROM "+TABLE_NAME+" WHERE "+Columns.ITEM_NAME+"= ?";
    public static final String DELETE_ALL = "DELETE FROM "+TABLE_NAME;
    public static final String TABLE_SELECT_ALL="SELECT * FROM "+TABLE_NAME+" ORDER BY "+Columns.ITEM_NAME+" ASC "+"; ";

}
