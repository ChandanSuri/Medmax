package com.example.dell.medmax.ShopkeeperActivities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dell.medmax.ShopkeeperActivities.db.OrderTable;

/**
 * Created by Chandan Suri on 11/13/2016.
 */
public class DbOpener extends SQLiteOpenHelper{

    public static final String DB_NAME = "Current_Order";
    public static final int DB_VER = 1;

    public static DbOpener dbOpener = null;

    public static SQLiteDatabase openReadableDatabase(Context context){
        if (dbOpener==null){
            dbOpener = new DbOpener(context);
        }
        return dbOpener.getReadableDatabase();
    }

    public static SQLiteDatabase openWritableDatabase(Context context){
        if (dbOpener==null){
            dbOpener = new DbOpener(context);
        }
        return dbOpener.getWritableDatabase();
    }

    public DbOpener(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(OrderTable.TABLE_CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(OrderTable.DELETE_ALL);
    }
}
