package com.example.dell.medmax.ShopkeeperActivities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;
import com.example.dell.medmax.ShopkeeperActivities.db.OrderTable;
import com.example.dell.medmax.ShopkeeperActivities.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrder2Activity extends AppCompatActivity {

    private ListView inventoryListView;
    public static final String INVENTORY_LIST_URL = "http://medmax.pe.hu/show_inventory_vendor.php";
    public String emailVendor;
    private ArrayList<HashMap<String, String>> inventoryArrayList = new ArrayList<>();
    private FloatingActionButton proceedFab;
    private LinearLayout couldNotLoad;
    private TextView couldNotLoadTv;
    private ProgressDialog mProgress;
    private static int itemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inventoryListView = (ListView)findViewById(R.id.inventory_list);
        proceedFab = (FloatingActionButton)findViewById(R.id.proceed_to_checkout);
        Intent intent = getIntent();
        emailVendor = intent.getStringExtra("EmailVendor");
        getData();
        couldNotLoad = (LinearLayout)findViewById(R.id.could_not_load_layout);
        couldNotLoadTv = (TextView)findViewById(R.id.not_load_recent_order_tv);
        proceedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrder2Activity.this);
                    builder.setTitle("Proceed to Checkout !");
                    builder.setMessage("Are You Sure that you want to proceed to checkout ?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("YES, PROCEED", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent proceedIntent = new Intent(PlaceOrder2Activity.this, FinalPlaceOrderActivity.class);
                            proceedIntent.putExtra("EmailVendor", emailVendor);
                            startActivity(proceedIntent);
                        }
                    })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.create().show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrder2Activity.this);
                    builder.setTitle("No Items Selected!!");
                    builder.setMessage("You Have No Items in Your Cart !!");
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK !", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }


    private void getData(){
        mProgress = ProgressDialog.show(PlaceOrder2Activity.this,"Loading ...","Please Wait !!");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INVENTORY_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]")) {
                    mProgress.dismiss();
                    couldNotLoad.setVisibility(View.VISIBLE);
                    couldNotLoadTv.setText("Could not Load the Inventory of the vendor, needed to go Back !!");
                    Toast.makeText(PlaceOrder2Activity.this, "Could Not Load the Inventory!!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.dismiss();
                    couldNotLoad.setVisibility(View.GONE);
                    inventoryListView.setVisibility(View.VISIBLE);
                    proceedFab.setVisibility(View.VISIBLE);
                    inventoryArrayList = getJSON(response);
                    InventoryListAdapter inventoryListAdapter = new InventoryListAdapter(inventoryArrayList);
                    inventoryListView.setAdapter(inventoryListAdapter);
                    inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                            final CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox_item);
                            final String itemNameTv = ((TextView) view.findViewById(R.id.item_name)).getText().toString();
                            final String itemCompTv = ((TextView) view.findViewById(R.id.item_company_name)).getText().toString();
                            final String itemPriceTv = ((TextView) view.findViewById(R.id.item_price)).getText().toString();

                            if (!cb.isChecked()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrder2Activity.this);
                                final EditText input = new EditText(PlaceOrder2Activity.this);
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                builder.setTitle("Want to add the item to your cart ?")
                                        .setMessage("Just type in the Quantity of the item you want !")
                                        .setCancelable(true)
                                        .setView(input)
                                        .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                itemCount++;
                                                proceedFab.setVisibility(View.VISIBLE);
                                                String qty = input.getText().toString();
                                                Toast.makeText(PlaceOrder2Activity.this, "Qty = " + qty, Toast.LENGTH_SHORT).show();
                                                cb.setChecked(true);
                                                Log.d("DATA GOT", itemNameTv + " " + itemPriceTv + " " + itemCompTv);
                                                saveToDeviceDatabase(itemNameTv, itemCompTv, Float.parseFloat(itemPriceTv), Integer.parseInt(qty));
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.create().show();
                                Toast.makeText(PlaceOrder2Activity.this, "Clicked!!", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrder2Activity.this);
                                builder.setTitle("Want to Remove the item from Cart !")
                                        .setMessage("Are You Sure that you want to remove the item from the Cart ?")
                                        .setCancelable(true)
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                itemCount--;
                                                if (itemCount==0){
                                                    proceedFab.setVisibility(View.INVISIBLE);
                                                }else {
                                                    proceedFab.setVisibility(View.VISIBLE);
                                                }
                                                cb.setChecked(false);
                                                removeFromDeviceDatabase(itemNameTv);
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .create().show();
                                Toast.makeText(PlaceOrder2Activity.this, "Already Clicked!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                couldNotLoad.setVisibility(View.VISIBLE);
                couldNotLoadTv.setText("Sorry, There was some Connectivity Problem, Could Not Load Data, Go Back !!");
                Toast.makeText(PlaceOrder2Activity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailVendor);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PlaceOrder2Activity.this);
        requestQueue.add(stringRequest);
    }

    private ArrayList<HashMap<String, String>> getJSON(String inventoryListJSON){
        ParseJSONInventoryList parseJSONInventoryList = new ParseJSONInventoryList(inventoryListJSON);
        inventoryArrayList = parseJSONInventoryList.parseJSON();
        return inventoryArrayList;
    }

    private class InventoryListAdapter extends BaseAdapter{

        class InventoryListViewHolder{
            TextView itemNameTv;
            TextView itemPriceTv;
            TextView itemCompanyNameTv;
            CheckBox itemCheck;
        }

        private ArrayList<HashMap<String, String>> inventoryList;

        public InventoryListAdapter(ArrayList<HashMap<String, String>> list){
            this.inventoryList = list;
        }

        @Override
        public int getCount() {
            return inventoryList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return inventoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            InventoryListViewHolder inventoryListViewHolder;
            if(convertView==null){
                convertView = inflater.inflate(R.layout.place_order_2_inventory_item,null);
                inventoryListViewHolder = new InventoryListViewHolder();
                inventoryListViewHolder.itemNameTv = (TextView)convertView.findViewById(R.id.item_name);
                inventoryListViewHolder.itemCompanyNameTv = (TextView)convertView.findViewById(R.id.item_company_name);
                inventoryListViewHolder.itemPriceTv = (TextView)convertView.findViewById(R.id.item_price);
                inventoryListViewHolder.itemCheck = (CheckBox)convertView.findViewById(R.id.checkBox_item);
                convertView.setTag(inventoryListViewHolder);
            }else{
                inventoryListViewHolder = (InventoryListViewHolder)convertView.getTag();
            }
            HashMap<String, String> thisItem = getItem(position);
            String ItemName = thisItem.get(ParseJSONInventoryList.ITEM_NAME_STR);
            if(checkForItemName(ItemName))
            {
                inventoryListViewHolder.itemCheck.setChecked(true);
            }else {
                inventoryListViewHolder.itemCheck.setChecked(false);
            }
            inventoryListViewHolder.itemNameTv.setText(thisItem.get(ParseJSONInventoryList.ITEM_NAME_STR));
            inventoryListViewHolder.itemCompanyNameTv.setText(thisItem.get(ParseJSONInventoryList.ITEM_COMP_NAME_STR));
            inventoryListViewHolder.itemPriceTv.setText(thisItem.get(ParseJSONInventoryList.ITEM_PRICE_STR));

            return convertView;
        }
    }

    private boolean checkForItemName(String itemName){
        SQLiteDatabase myDb = DbOpener.openWritableDatabase(this);
        String SQLQuery = "SELECT "+OrderTable.Columns.ITEM_NAME+" FROM "+OrderTable.TABLE_NAME+
                " WHERE "+OrderTable.Columns.ITEM_NAME+" = ?";
        Cursor cursor;
        cursor = myDb.rawQuery(SQLQuery, new String[]{itemName});
        if (cursor!=null){
            if (cursor.moveToFirst()) {
                do {
                    String orderedItemName = cursor.getString(cursor.getColumnIndex(OrderTable.Columns.ITEM_NAME));
                    if (orderedItemName!=null) {
                        cursor.close();
                        return true;
                    }
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return false;
    }
    private void saveToDeviceDatabase(String itemName, String itemCompName, float itemPrice, int qty){
        SQLiteDatabase myDb = DbOpener.openWritableDatabase(this);
        Order order = new Order(itemName, itemCompName, itemPrice, qty, (float) 0.0);

        ContentValues values = new ContentValues();
        values.put(OrderTable.Columns.ITEM_NAME, order.getItemName());
        values.put(OrderTable.Columns.ITEM_COMP_NAME, order.getItemCompName());
        values.put(OrderTable.Columns.ITEM_PRICE, order.getItemPrice());
        values.put(OrderTable.Columns.ITEM_QTY, order.getItemQty());
        values.put(OrderTable.Columns.ITEM_TOTAL_PRICE, order.getItemTotalPrice());

        myDb.insert(OrderTable.TABLE_NAME, null, values);
        myDb.close();
    }

    private void removeFromDeviceDatabase(String itemName){
        SQLiteDatabase myDb = DbOpener.openWritableDatabase(this);
        String[] item ={itemName};
        myDb.delete(OrderTable.TABLE_NAME, OrderTable.Columns.ITEM_NAME+"= ?", item);
        myDb.close();
    }

    private void removeWholeDatabase(){
        SQLiteDatabase myDb = DbOpener.openWritableDatabase(this);
        myDb.execSQL(OrderTable.DELETE_ALL);
        myDb.close();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrder2Activity.this);
        builder.setTitle("Cancel Order !!");
        builder.setMessage("Are You Sure You want to Go back, your selections will not be saved !!");
        builder.setCancelable(true);
        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNegativeButton("YES, GO BACK!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeWholeDatabase();
                Toast.makeText(PlaceOrder2Activity.this, "Order Not Placed!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlaceOrder2Activity.this, ShopkeeperMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).create().show();
    }
}
