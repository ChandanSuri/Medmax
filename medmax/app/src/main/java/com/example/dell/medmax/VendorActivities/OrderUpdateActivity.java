package com.example.dell.medmax.VendorActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;
import com.example.dell.medmax.ShopkeeperActivities.ParseJSONVendorInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aggarwal on 22-11-2016.
 */
public class OrderUpdateActivity extends AppCompatActivity {
    String url1 = "http://medmax.pe.hu/show_skeeper_detail.php";
    String url2 = "http://medmax.pe.hu/detail_view_order_skeeper.php";
    String url3="http://medmax.pe.hu/changestatus.php";
    String url4="http://medmax.pe.hu/changestatusorder.php";
    String url5="http://medmax.pe.hu/dispatch.php";
    String orderId,statusOfItem;
    TextView orderIdTv,skeeperNameTv,skeeperEmailTv,skeeperContactTv,skeeperAddressTv,totalCostTv;
    ArrayList<HashMap<String,String>> skeeperInfo=new ArrayList<>();
    ArrayList<HashMap<String,String>> orderInfo=new ArrayList<>();
    String emailSkeeper,totalCost;
    ListView orderedItemLv;
    ProgressDialog mProgress;
    LinearLayout orderDetailsLayout;
    String orderStatus;
    Button statusBtn;
    RelativeLayout dispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_update);
        Intent intent=getIntent();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" About Order ");
        orderId=intent.getStringExtra("OrderID");
        emailSkeeper=intent.getStringExtra("Shopkeeper email");
        totalCost=intent.getStringExtra("Total cost");
        orderStatus=intent.getStringExtra("status");
        orderIdTv=(TextView)findViewById(R.id.order_id_tv_vendor);
        skeeperNameTv=(TextView)findViewById(R.id.shopkeeper_name_info);
        skeeperEmailTv=(TextView)findViewById(R.id.shopkeeper_email_info);
        skeeperContactTv=(TextView)findViewById(R.id.shopkeeper_contact_info);
        skeeperAddressTv=(TextView)findViewById(R.id.shopkeeper_address_info);
        totalCostTv=(TextView)findViewById(R.id.total_cost_tv_vendor);

        orderedItemLv=(ListView)findViewById(R.id.ordered_items_list_view_vendor);
        orderDetailsLayout=(LinearLayout)findViewById(R.id.order_details_layout_vendor);
        statusBtn=(Button)findViewById(R.id.dispatch_button);
        dispatch=(RelativeLayout)findViewById(R.id.dispatch_button_layer);
        Log.d("button:",statusBtn.getText().toString());
        if(orderStatus.equalsIgnoreCase("0"))
        {
//            statusBtn.setVisibility(View.VISIBLE);

        }
        checkOrderStatus();
        getSkeeperData();
    }
    private void checkOrderStatus()
    {
        final Button statusBtn=(Button)findViewById(R.id.dispatch_button);
        RequestQueue requestQueue4 = Volley.newRequestQueue(OrderUpdateActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url5, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equalsIgnoreCase("1")) {
                    statusBtn.setEnabled(true);
                    statusBtn.setText("Dispatch Order");
                    statusBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builders = new AlertDialog.Builder(OrderUpdateActivity.this);
                                builders.setMessage("Dispatch Order ?");
                                builders.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                            if (statusBtn.isEnabled()) {
                                        RequestQueue requestQueue = Volley.newRequestQueue(OrderUpdateActivity.this);
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url4, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                if (s.equalsIgnoreCase("1")) {
                                                    Toast.makeText(OrderUpdateActivity.this, "Order Delivered", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else if (s.equalsIgnoreCase("2")) {
                                                    Toast.makeText(OrderUpdateActivity.this, "Failed...Try again later", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(OrderUpdateActivity.this, "Failed...Try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> myparams = new HashMap<String, String>();
                                                myparams.put("order_id", orderId);
                                                return myparams;
                                            }
                                        };
                                        requestQueue.add(stringRequest);
                                    }
//                        }
                                });
                                builders.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                            AlertDialog dialog = builders.create();
                            dialog.show();
                            }
                    });

                } else if (s.equalsIgnoreCase("2")) {
                    statusBtn.setEnabled(false);
                    statusBtn.setText("Order not ready yet");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                statusBtn.setEnabled(false);
                statusBtn.setText("Order not ready yet");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myparams = new HashMap<String, String>();
                myparams.put("order_id", orderId);
                return myparams;
            }
        };
        requestQueue4.add(stringRequest);
    }
    private void getSkeeperData() {
        mProgress = ProgressDialog.show(OrderUpdateActivity.this, "Loading,", "wait...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                skeeperInfo = getJSON(response);
                skeeperNameTv.setText(skeeperInfo.get(0).get(ParseJSONVendorInfo.VENDOR_NAME_STR));
                skeeperEmailTv.setText(skeeperInfo.get(0).get(ParseJSONVendorInfo.VENDOR_EMAIL_STR));
                skeeperAddressTv.setText(skeeperInfo.get(0).get(ParseJSONVendorInfo.VENDOR_ADDRESS_STR));
                skeeperContactTv.setText(skeeperInfo.get(0).get(ParseJSONVendorInfo.VENDOR_CONTACT_STR));
                getProductsOrderedData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                finish();
                Toast.makeText(OrderUpdateActivity.this, "Check your Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailSkeeper);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrderUpdateActivity.this);
        requestQueue.add(stringRequest);
    }
    private ArrayList<HashMap<String, String>> getJSON(String vendorInfoJSON){
        ParseJSONShopkeeperInfo parseJSONShopkeeperInfo = new ParseJSONShopkeeperInfo(vendorInfoJSON);
        skeeperInfo = parseJSONShopkeeperInfo.parseJSON();
        return skeeperInfo;
    }
    private void getProductsOrderedData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                orderDetailsLayout.setVisibility(View.VISIBLE);
                orderInfo = getJSONProductData(response);
                OrderedItemInfoListAdapter orderedItemInfoListAdapter = new OrderedItemInfoListAdapter(orderInfo);
                orderedItemLv.setAdapter(orderedItemInfoListAdapter);
                Log.d("" + orderId, "");
                if(orderStatus.equals("0")){
                    statusBtn.setVisibility(View.VISIBLE);
                orderedItemLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final CheckBox cb = (CheckBox) view.findViewById(R.id.status_of_product);
                        final TextView productNameTv = (TextView) view.findViewById(R.id.product_name);
//                        statusOfItem="";
                        if (!cb.isChecked()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderUpdateActivity.this);
                            Log.d(""+orderId ,""+ productNameTv.getText().toString());
                            builder.setMessage("Item Ready to Dispatch ?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    statusOfItem="true";
                                    setStatus(cb,productNameTv);
//                                    mProgress=ProgressDialog.show(OrderUpdateActivity.this,"Wait","updating");
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    statusOfItem="cancel";
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderUpdateActivity.this);
                            builder.setMessage("Are you sure to remove the item ?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    statusOfItem="false";
                                    setStatus(cb,productNameTv);
//                                    mProgress=ProgressDialog.show(OrderUpdateActivity.this,"Wait","updating");
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    statusOfItem="cancel";
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        System.out.print("+++++++++++++++++++++++"+statusOfItem);

                    }
                });
                }
                totalCostTv.setText(totalCost);
                orderIdTv.setText(orderId);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                finish();
                Toast.makeText(OrderUpdateActivity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", orderId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OrderUpdateActivity.this);
        requestQueue.add(stringRequest);
    }
    private void setStatus(final CheckBox cb,final TextView productNameTv)
    {
        if((!statusOfItem.equals("cancel"))&&(!statusOfItem.equals(""))){
            RequestQueue requestQueue = Volley.newRequestQueue(OrderUpdateActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (s.equalsIgnoreCase("1")) {
//                                    mProgress.setProgress(100);
//                                    mProgress.dismiss();
                        Toast.makeText(OrderUpdateActivity.this, "item status updated", Toast.LENGTH_SHORT).show();
                        if(statusOfItem.equals("true"))
                        {
                            cb.setChecked(true);
                        }
                        else if(statusOfItem.equals("false"))
                        {
                            cb.setChecked(false);
                        }
                    } else if (s.equalsIgnoreCase("2")) {
//                                    mProgress.setProgress(100);
//                                    mProgress.dismiss();
//                                                flag=false;
                        Toast.makeText(OrderUpdateActivity.this, "Failed to process.TRY AGAIN", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                                mProgress.setProgress(100);
//                                mProgress.dismiss();
                    Toast.makeText(OrderUpdateActivity.this, "Failed to process.TRY AGAIN", Toast.LENGTH_SHORT).show();
//                                            flag=false;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> myparams = new HashMap<String, String>();
                    myparams.put("order_id", orderId);
                    myparams.put("product_name", productNameTv.getText().toString());
                    myparams.put("status", statusOfItem);
                    Log.d("order id : " + orderId, " product name :" + productNameTv.getText().toString());
                    Log.d("status : ",statusOfItem);
                    System.out.print("+++++++++++++++++++++++"+statusOfItem);
                    return myparams;
                }
            };
            requestQueue.add(stringRequest);}
        else
        {
            System.out.print("+++++++++++++++++++++++"+statusOfItem);
            Log.d("status", statusOfItem);
            Toast.makeText(OrderUpdateActivity.this, "Failed to process.TRY AGAIN", Toast.LENGTH_SHORT).show();
        }
        mProgress=ProgressDialog.show(OrderUpdateActivity.this,"Updating Status","wait...");
        getProductsOrderedData();
        checkOrderStatus();
        mProgress.dismiss();
    }
    private ArrayList<HashMap<String, String>> getJSONProductData(String productsOrderedInfoJSON){
        ParseJSONOrderedItemInfoVendor parseJSONOrderedItemInfoVendor = new ParseJSONOrderedItemInfoVendor(productsOrderedInfoJSON);
        orderInfo = parseJSONOrderedItemInfoVendor.parseJSON();
        return orderInfo;
    }
    private class OrderedItemInfoListAdapter extends BaseAdapter {

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
            final OrderedItemInfoListViewHolder orderedItemInfoListViewHolder;
            if (convertView == null){
                convertView = inflater.inflate(R.layout.ordered_item_list_info, null);
                orderedItemInfoListViewHolder = new OrderedItemInfoListViewHolder();
                orderedItemInfoListViewHolder.productNameTv = (TextView)convertView.findViewById(R.id.product_name);
                orderedItemInfoListViewHolder.productCompNameTv = (TextView)convertView.findViewById(R.id.product_comp_name);
                orderedItemInfoListViewHolder.productCostTv = (TextView)convertView.findViewById(R.id.product_price);
                orderedItemInfoListViewHolder.productQtyTv = (TextView)convertView.findViewById(R.id.product_qty);
                orderedItemInfoListViewHolder.productStatusCb = (CheckBox)convertView.findViewById(R.id.status_of_product);
                orderedItemInfoListViewHolder.productStatusCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        {
                            orderedItemInfoListViewHolder.productStatusCb.setChecked(true);
                        }
                        else
                        {
                            orderedItemInfoListViewHolder.productStatusCb.setChecked(false);
                        }
                    }
                });
                convertView.setTag(orderedItemInfoListViewHolder);
            }else {
                orderedItemInfoListViewHolder = (OrderedItemInfoListViewHolder)convertView.getTag();
            }
            HashMap<String, String> thisInfo = getItem(position);
            orderedItemInfoListViewHolder.productNameTv.setText(thisInfo.get(ParseJSONOrderedItemInfoVendor.PRODUCT_NAME_STR));
            orderedItemInfoListViewHolder.productCompNameTv.setText(thisInfo.get(ParseJSONOrderedItemInfoVendor.PRODUCT_COMP_NAME_STR));
            orderedItemInfoListViewHolder.productCostTv.setText(thisInfo.get(ParseJSONOrderedItemInfoVendor.PRODUCT_COST_STR));
            orderedItemInfoListViewHolder.productQtyTv.setText(thisInfo.get(ParseJSONOrderedItemInfoVendor.PRODUCT_QTY_STR));
            orderedItemInfoListViewHolder.productStatusCb.setChecked(Boolean.parseBoolean(thisInfo.get(ParseJSONOrderedItemInfoVendor.PRODUCT_STATUS_STR)));
            return convertView;
        }
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(OrderUpdateActivity.this);
        builder.setMessage("Are you sure you want to go back ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
