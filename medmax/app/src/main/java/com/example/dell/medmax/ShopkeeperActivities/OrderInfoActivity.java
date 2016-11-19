package com.example.dell.medmax.ShopkeeperActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderInfoActivity extends AppCompatActivity {

    public static final String VENDOR_INFO_URL = "http://medmax.pe.hu/show_vendor_detail.php";
    public static final String ORDER_INFO_URL = "http://medmax.pe.hu/detail_view_order_skeeper.php";
    private String orderId;
    private TextView orderIdTv, vendorNameTv, vendorEmailTv, vendorAddressTv, vendorContactTv, totalCostTv;
    private ArrayList<HashMap<String, String>> vendorInfo = new ArrayList<>();
    private ArrayList<HashMap<String, String>> orderInfo = new ArrayList<>();
    private String emailVendor, totalCost;
    private ListView orderedItemsLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent orderInfoIntent = getIntent();
        orderId = orderInfoIntent.getStringExtra("OrderId");
        emailVendor = orderInfoIntent.getStringExtra("Vendor Email");
        totalCost = orderInfoIntent.getStringExtra("Total Cost");

        orderIdTv = (TextView)findViewById(R.id.order_id_tv);
        vendorNameTv = (TextView)findViewById(R.id.vendor_name_info);
        vendorEmailTv = (TextView)findViewById(R.id.vendor_email_info);
        vendorAddressTv = (TextView)findViewById(R.id.vendor_address_info);
        vendorContactTv = (TextView)findViewById(R.id.vendor_contact_info);
        totalCostTv = (TextView)findViewById(R.id.total_cost_tv);

        orderedItemsLv = (ListView)findViewById(R.id.ordered_items_list_view);

        getVendorData();
        getProductsOrderedData();
    }

    private void getVendorData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VENDOR_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vendorInfo = getJSON(response);
                vendorNameTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_NAME_STR));
                vendorEmailTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_EMAIL_STR));
                vendorAddressTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_ADDRESS_STR));
                vendorContactTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_CONTACT_STR));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderInfoActivity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailVendor);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OrderInfoActivity.this);
        requestQueue.add(stringRequest);
    }

    private ArrayList<HashMap<String, String>> getJSON(String vendorInfoJSON){
        ParseJSONVendorInfo parseJSONVendorInfo = new ParseJSONVendorInfo(vendorInfoJSON);
        vendorInfo = parseJSONVendorInfo.parseJSON();
        return vendorInfo;
    }

    private void getProductsOrderedData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDER_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                orderInfo = getJSONProductData(response);
                OrderedItemInfoListAdapter orderedItemInfoListAdapter = new OrderedItemInfoListAdapter(orderInfo);
                orderedItemsLv.setAdapter(orderedItemInfoListAdapter);
                totalCostTv.setText(totalCost);
                orderIdTv.setText(orderId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderInfoActivity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", orderId);
                return params;            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OrderInfoActivity.this);
        requestQueue.add(stringRequest);
    }

    private ArrayList<HashMap<String, String>> getJSONProductData(String productsOrderedInfoJSON){
        ParseJSONOrderedItemInfo parseJSONOrderedItemInfo = new ParseJSONOrderedItemInfo(productsOrderedInfoJSON);
        orderInfo = parseJSONOrderedItemInfo.parseJSON();
        return orderInfo;
    }

    private class OrderedItemInfoListAdapter extends BaseAdapter{

        class OrderedItemInfoListViewHolder{
            TextView productNameTv, productCostTv, productQtyTv, productCompNameTv;
            CheckBox productStatusCb;
        }

        private ArrayList<HashMap<String, String>> orderedItemInfo;

        public OrderedItemInfoListAdapter(ArrayList<HashMap<String,String>> list) {
            this.orderedItemInfo = list;
        }

        @Override
        public int getCount() {
            return orderedItemInfo.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return orderedItemInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            OrderedItemInfoListViewHolder orderedItemInfoListViewHolder;
            if (convertView == null){
                convertView = inflater.inflate(R.layout.ordered_item_list_info, null);
                orderedItemInfoListViewHolder = new OrderedItemInfoListViewHolder();
                orderedItemInfoListViewHolder.productNameTv = (TextView)convertView.findViewById(R.id.product_name);
                orderedItemInfoListViewHolder.productCompNameTv = (TextView)convertView.findViewById(R.id.product_comp_name);
                orderedItemInfoListViewHolder.productCostTv = (TextView)convertView.findViewById(R.id.product_price);
                orderedItemInfoListViewHolder.productQtyTv = (TextView)convertView.findViewById(R.id.product_qty);
                orderedItemInfoListViewHolder.productStatusCb = (CheckBox)convertView.findViewById(R.id.status_of_product);
                convertView.setTag(orderedItemInfoListViewHolder);
            }else {
                orderedItemInfoListViewHolder = (OrderedItemInfoListViewHolder)convertView.getTag();
            }
            HashMap<String, String> thisInfo = getItem(position);
            orderedItemInfoListViewHolder.productNameTv.setText(thisInfo.get(ParseJSONOrderedItemInfo.PRODUCT_NAME_STR));
            orderedItemInfoListViewHolder.productCompNameTv.setText(thisInfo.get(ParseJSONOrderedItemInfo.PRODUCT_COMP_NAME_STR));
            orderedItemInfoListViewHolder.productCostTv.setText(thisInfo.get(ParseJSONOrderedItemInfo.PRODUCT_COST_STR));
            orderedItemInfoListViewHolder.productQtyTv.setText(thisInfo.get(ParseJSONOrderedItemInfo.PRODUCT_QTY_STR));
            orderedItemInfoListViewHolder.productStatusCb.setChecked(Boolean.parseBoolean(thisInfo.get(ParseJSONOrderedItemInfo.PRODUCT_STATUS_STR)));

            return convertView;
        }
    }
}
