package com.example.dell.medmax.ShopkeeperActivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;
import com.example.dell.medmax.ShopkeeperActivities.db.OrderTable;
import com.example.dell.medmax.ShopkeeperActivities.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class FinalPlaceOrderActivity extends AppCompatActivity {

    private TextView vendorNameTv, vendorEmailTv, vendorContactTv, vendorAddressTv;
    private String emailVendor;
    public static final String VENDOR_INFO_URL = "http://medmax.pe.hu/show_vendor_detail.php";
    public static final String PLACE_ORDER_URL = "http://medmax.pe.hu/placeOrder.php";
    public static final String ORDERED_ITEM_ADD_URL = "http://medmax.pe.hu/insert_item_order.php";
    public static final String GET_ORDER_ID_URL = "http://medmax.pe.hu/get_order_id.php";

    private String[] orderId=null;
    private ArrayList<HashMap<String, String>> vendorInfo = new ArrayList<>();
    private ListView orderedItemList;
    private float totalPayment;
    private TextView totalPaymentTv;
    float finalTotalPay;
    RequestQueue requestQueue;
    private FloatingActionButton fab;
    private LinearLayout couldNotLoad;
    private TextView couldNotLoadTv;
    private ProgressDialog mProgress,mPlaceProgress;
    private LinearLayout finalPlaceOrderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_place_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        finalPlaceOrderLayout = (LinearLayout)findViewById(R.id.final_place_order_info_layout);
        vendorNameTv = (TextView)findViewById(R.id.vendor_name_info);
        vendorEmailTv = (TextView) findViewById(R.id.vendor_email_info);
        vendorAddressTv = (TextView) findViewById(R.id.vendor_address_info);
        vendorContactTv = (TextView)findViewById(R.id.vendor_contact_info);
        orderedItemList = (ListView)findViewById(R.id.items_ordered_list_view);
        orderedItemList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(orderedItemList);
        totalPaymentTv = (TextView)findViewById(R.id.total_price_tv);
        Intent intent = getIntent();
        emailVendor = intent.getStringExtra("EmailVendor");
        fab = (FloatingActionButton) findViewById(R.id.place_order);
        couldNotLoad = (LinearLayout)findViewById(R.id.could_not_load_layout);
        couldNotLoadTv = (TextView)findViewById(R.id.not_load_recent_order_tv);
        getData();
        showOrderedItemsList();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FinalPlaceOrderActivity.this);
                builder.setTitle("Place Order !!");
                builder.setMessage("Are You Sure that you want to Place the order ?");
                builder.setPositiveButton("YES, PLACE THE ORDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlaceProgress = ProgressDialog.show(FinalPlaceOrderActivity.this,"Placing the Order ...","Please Wait for a while !!");
                        requestQueue = Volley.newRequestQueue(FinalPlaceOrderActivity.this);
                        sendDataToShopkeeperTable();
                    }
                }).setNegativeButton("NO !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
            }
        });

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, Toolbar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void getData(){
        mProgress = ProgressDialog.show(FinalPlaceOrderActivity.this,"Loading ...","Please Wait !!");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VENDOR_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]")) {
                    mProgress.dismiss();
                    couldNotLoad.setVisibility(View.VISIBLE);
                    couldNotLoadTv.setText("Could not Load the information of the vendor, needed to go Back !!");
                    Toast.makeText(FinalPlaceOrderActivity.this, "Could Not Load the Information!!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.dismiss();
                    couldNotLoad.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    finalPlaceOrderLayout.setVisibility(View.VISIBLE);
                    vendorInfo = getJSON(response);
                    vendorNameTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_NAME_STR));
                    vendorEmailTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_EMAIL_STR));
                    vendorAddressTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_ADDRESS_STR));
                    vendorContactTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_CONTACT_STR));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                couldNotLoad.setVisibility(View.VISIBLE);
                couldNotLoadTv.setText("Sorry, There was some Connectivity Problem, Could Not Load Data, Go Back !!");
                Toast.makeText(FinalPlaceOrderActivity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailVendor);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FinalPlaceOrderActivity.this);
        requestQueue.add(stringRequest);
    }

    private ArrayList<HashMap<String, String>> getJSON(String vendorInfoJSON){
        ParseJSONVendorInfo parseJSONVendorInfo = new ParseJSONVendorInfo(vendorInfoJSON);
        vendorInfo = parseJSONVendorInfo.parseJSON();
        return vendorInfo;
    }

    private void showOrderedItemsList(){
        ArrayList<Order> orderArrayList = getOrderedItemsList();
        OrderedItemsListAdapter orderedItemsListAdapter;
        orderedItemsListAdapter = new OrderedItemsListAdapter(orderArrayList);
        orderedItemList.setAdapter(orderedItemsListAdapter);
        totalPaymentTv.setText(String.valueOf(totalPayment));
    }

    private ArrayList<Order> getOrderedItemsList(){

        ArrayList<Order> orderArrayList = new ArrayList<>();
        SQLiteDatabase myDb = DbOpener.openReadableDatabase(this);

        totalPayment = 0.0f;
        Cursor c1;
        c1 = myDb.rawQuery(OrderTable.TABLE_SELECT_ALL, null);
        if (c1!=null){
            if (c1.moveToFirst()){
                do{
                    String orderedItemName = c1.getString(c1.getColumnIndex(OrderTable.Columns.ITEM_NAME));
                    String orderedItemCompName = c1.getString(c1.getColumnIndex(OrderTable.Columns.ITEM_COMP_NAME));
                    int orderedItemQty = c1.getInt(c1.getColumnIndex(OrderTable.Columns.ITEM_QTY));
                    float orderedItemPrice = c1.getFloat(c1.getColumnIndex(OrderTable.Columns.ITEM_PRICE));
                    float orderedItemTotalPrice = c1.getFloat(c1.getColumnIndex(OrderTable.Columns.ITEM_TOTAL_PRICE));
                    totalPayment += orderedItemTotalPrice;
                    Order order =new Order(orderedItemName, orderedItemCompName,orderedItemPrice, orderedItemQty, orderedItemTotalPrice);
                    orderArrayList.add(order);
                }while(c1.moveToNext());
            }
        }
        c1.close();
        return orderArrayList;
    }

    private class OrderedItemsListAdapter extends BaseAdapter{

        class OrderedItemViewHolder{
            TextView orderedItemNameTv;
            TextView orderedItemPriceTv;
            TextView orderedItemCompNameTv;
            TextView orderedItemQtyTv;
            TextView orderedItemTotalPriceTv;
        }

        private ArrayList<Order> orderedItemsArrayList;

        public OrderedItemsListAdapter(ArrayList<Order> orderedItems){
            this.orderedItemsArrayList = orderedItems;
        }

        @Override
        public int getCount() {
            return orderedItemsArrayList.size();
        }

        @Override
        public Order getItem(int position) {
            return orderedItemsArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = getLayoutInflater();
            OrderedItemViewHolder orderedItemViewHolder;
            if (convertView==null){
                convertView = layoutInflater.inflate(R.layout.ordered_list_item, null);
                orderedItemViewHolder = new OrderedItemViewHolder();
                orderedItemViewHolder.orderedItemNameTv = (TextView)convertView.findViewById(R.id.ordered_item_name);
                orderedItemViewHolder.orderedItemCompNameTv = (TextView)convertView.findViewById(R.id.ordered_item_comp_name);
                orderedItemViewHolder.orderedItemPriceTv = (TextView)convertView.findViewById(R.id.ordered_item_price);
                orderedItemViewHolder.orderedItemQtyTv = (TextView)convertView.findViewById(R.id.ordered_item_qty);
                orderedItemViewHolder.orderedItemTotalPriceTv = (TextView)convertView.findViewById(R.id.ordered_item_total_price);
                convertView.setTag(orderedItemViewHolder);
            }else {
                orderedItemViewHolder = (OrderedItemViewHolder)convertView.getTag();
            }

            Order thisOrder = getItem(position);
            orderedItemViewHolder.orderedItemNameTv.setText(thisOrder.getItemName());
            orderedItemViewHolder.orderedItemCompNameTv.setText(thisOrder.getItemCompName());
            orderedItemViewHolder.orderedItemPriceTv.setText("Rs. "+ String.valueOf(thisOrder.getItemPrice()));
            orderedItemViewHolder.orderedItemQtyTv.setText("Qty => "+ thisOrder.getItemQty());
            orderedItemViewHolder.orderedItemTotalPriceTv.setText("Rs. "+ String.valueOf(thisOrder.getItemTotalPrice()));

            return convertView;
        }
    }

    private void sendDataToShopkeeperTable(){

        SQLiteDatabase myDb = DbOpener.openReadableDatabase(this);
        float totalPay = 0.0f;
        Cursor c1;
        c1 = myDb.rawQuery(OrderTable.TABLE_SELECT_ALL, null);
        if (c1!=null){
            if (c1.moveToFirst()){
                do{
                    float orderedItemTotalPrice = c1.getFloat(c1.getColumnIndex(OrderTable.Columns.ITEM_TOTAL_PRICE));
                    totalPay += orderedItemTotalPrice;
                }while(c1.moveToNext());
            }
        }
        c1.close();

        finalTotalPay = totalPay;
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        final String shopEmail = sharedPreferences.getString("mail","Sorry ! Not Able to retrieve !!");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PLACE_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (Objects.equals(response, "1")){
                    Log.d("response of Order", "Order Placed !");
                    getOrderId();
                }else {
                    Toast.makeText(FinalPlaceOrderActivity.this, "There was some Error, Check Internet Connectivity!!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FinalPlaceOrderActivity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shopkeeper_email",shopEmail);
                params.put("vendor_email",emailVendor);
                params.put("total_cost", String.valueOf(finalTotalPay));
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    private void getOrderId(){

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        final String shopEmail = sharedPreferences.getString("mail","Sorry ! Not Able to retrieve !!");

        orderId = new String[1];
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, GET_ORDER_ID_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                orderId[0] = response;
                Log.d("ORDER ID =>", orderId[0]);
                if (orderId[0]!=null) {
                    sendDataToItemsTable();
                    //Toast.makeText(FinalPlaceOrderActivity.this, "Order Has Been Placed Successfully !", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(FinalPlaceOrderActivity.this, "Sorry, the Order could be place!!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FinalPlaceOrderActivity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shopkeeper_email",shopEmail);
                params.put("vendor_email",emailVendor);
                params.put("total_cost", String.valueOf(finalTotalPay));
                return params;
            }

        };
        requestQueue.add(stringRequest1);

    }
    private void sendDataToItemsTable(){

        SQLiteDatabase myDb = DbOpener.openReadableDatabase(this);
        Cursor c1;
        c1 = myDb.rawQuery(OrderTable.TABLE_SELECT_ALL, null);

        if (c1!=null){
            if (c1.moveToFirst()){
                do{
                    final String orderedItemName = c1.getString(c1.getColumnIndex(OrderTable.Columns.ITEM_NAME));
                    final String orderedItemCompName = c1.getString(c1.getColumnIndex(OrderTable.Columns.ITEM_COMP_NAME));
                    final int orderedItemQty = c1.getInt(c1.getColumnIndex(OrderTable.Columns.ITEM_QTY));
                    final float orderedItemPrice = c1.getFloat(c1.getColumnIndex(OrderTable.Columns.ITEM_PRICE));
                    final float orderedItemTotalPrice = c1.getFloat(c1.getColumnIndex(OrderTable.Columns.ITEM_TOTAL_PRICE));
                    Log.d("+++++++++++++++++++",orderedItemName+" "+orderId[0]);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDERED_ITEM_ADD_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (Objects.equals(response, "1")){
                                Log.d("response of Order 2", "Order Placed !");
                                mPlaceProgress.dismiss();
                                orderPlacedMessage();
                            }else {
                                Toast.makeText(FinalPlaceOrderActivity.this, "There was some Error, Check Internet Connectivity!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(FinalPlaceOrderActivity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            Log.d("ORDER ID [2nd time] =>", orderId[0]);
                            params.put("order_id", orderId[0]);
                            params.put("product_name",orderedItemName);
                            params.put("product_cost", String.valueOf(orderedItemPrice));
                            params.put("product_qty", String.valueOf(orderedItemQty));
                            params.put("company_name",orderedItemCompName);
                            return params;
                        }

                        @Override
                        public Priority getPriority() {
                            return Priority.LOW;
                        }
                    };
                    requestQueue.add(stringRequest);
                }while(c1.moveToNext());
            }
        }
        c1.close();
    }

    private void orderPlacedMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(FinalPlaceOrderActivity.this);
        builder.setTitle("Order Placed !!");
        builder.setMessage("Your Order has been placed successfully with an Order Id =>"+orderId[0]+
                "\nThank You for Placing an order with us !");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dropSQLTable();
                closeAllActivities();
            }
        }).create().show();

    }
    private void dropSQLTable(){
        SQLiteDatabase myDb = DbOpener.openWritableDatabase(this);
        myDb.execSQL(OrderTable.DELETE_ALL);
        myDb.close();
    }

    private void closeAllActivities(){
        Intent intent = new Intent(getApplicationContext(), ShopkeeperMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

