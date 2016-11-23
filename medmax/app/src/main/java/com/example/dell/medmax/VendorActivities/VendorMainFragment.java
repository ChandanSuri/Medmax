package com.example.dell.medmax.VendorActivities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.dell.medmax.LoginActivity;
import com.example.dell.medmax.R;
import com.example.dell.medmax.ShopkeeperActivities.ParseJSONRecentOrdersList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aggarwal on 08-10-2016.
 */
public class VendorMainFragment extends Fragment {
    String url = "http://medmax.pe.hu/recent_order_vendor.php";
    SharedPreferences sharedPreferences;
    ListView vendorOrdersListView;
    private ArrayList<HashMap<String,String>> recentOrdersArrayList = new ArrayList<>();
    private TextView recentOrdersTv;
    private LinearLayout couldNotLoad;
    private TextView couldNotLoadTv;
    private ProgressDialog mProgress;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vendorOrdersListView=(ListView)getActivity().findViewById(R.id.vendor_recent_orders_list);
        recentOrdersTv = (TextView)getActivity().findViewById(R.id.recent_orders_tv_vendor);
        couldNotLoad = (LinearLayout)getActivity().findViewById(R.id.could_not_load_layout_vendor);
        couldNotLoadTv = (TextView)getActivity().findViewById(R.id.not_load_recent_order_tv_vendor);
        getData();
    }
    private void getData()
    {
        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String vendorEmail = sharedPreferences.getString("mail","Sorry ! Not Able to retrieve !!");
        mProgress = ProgressDialog.show(getActivity(),"Loading ...","Please Wait !!");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]")) {
                    mProgress.setProgress(100);
                    mProgress.dismiss();
                    couldNotLoad.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Could not load Recent Orders...", Toast.LENGTH_SHORT).show();
                } else {
                    couldNotLoad.setVisibility(View.GONE);
                    recentOrdersTv.setVisibility(View.VISIBLE);
                    vendorOrdersListView.setVisibility(View.VISIBLE);
                    recentOrdersArrayList=getJSON(response);
                    mProgress.setProgress(50);
                    RecentOrdersListAdapter recentOrdersListAdapter=new RecentOrdersListAdapter(recentOrdersArrayList);
                    vendorOrdersListView.setAdapter(recentOrdersListAdapter);
                    mProgress.setProgress(100);
                    mProgress.dismiss();
                    vendorOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(getActivity(), OrderUpdateActivity.class);
                            intent.putExtra("OrderID",recentOrdersArrayList.get(position).get(ParseJSONRecentOrderVendor.ORDER_ID_STR));
                            intent.putExtra("Shopkeeper email",recentOrdersArrayList.get(position).get(ParseJSONRecentOrderVendor.SHOPKEEPER_EMAIL_STR));
                            intent.putExtra("Total cost",recentOrdersArrayList.get(position).get(ParseJSONRecentOrderVendor.TOTAL_COST_STR));
                            intent.putExtra("status",recentOrdersArrayList.get(position).get(ParseJSONRecentOrderVendor.STATUS_STR));
                            startActivity(intent);
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.setProgress(100);
                mProgress.dismiss();
                couldNotLoad.setVisibility(View.VISIBLE);
                couldNotLoadTv.setText("Sorry, There was some Connectivity Problem, Could Not Load Data !!");
                Toast.makeText(getActivity(), "Please Check Your Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", vendorEmail);
                Log.d("vendor email", vendorEmail);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private ArrayList<HashMap<String, String>> getJSON(String recentOrderList){
        ParseJSONRecentOrderVendor parseJSONRecentOrderVendor = new ParseJSONRecentOrderVendor(recentOrderList);
        recentOrdersArrayList = parseJSONRecentOrderVendor.parseJSON();
        return recentOrdersArrayList;
    }
    private class RecentOrdersListAdapter extends BaseAdapter {

        class RecentOrdersViewHolder{
            TextView shopkeeperEmailTv;
            TextView totalCostTv;
            TextView status;
        }

        private ArrayList<HashMap<String, String>> recentOrdersList;

        public RecentOrdersListAdapter(ArrayList<HashMap<String, String>> list){
            this.recentOrdersList = list;
        }

        @Override
        public int getCount() {
            return recentOrdersList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return recentOrdersList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater= getActivity().getLayoutInflater();
            RecentOrdersViewHolder recentOrdersViewHolder;
            if (convertView==null){
                convertView = inflater.inflate(R.layout.recent_order_list_vendor, parent, false);
                recentOrdersViewHolder = new RecentOrdersViewHolder();
                recentOrdersViewHolder.shopkeeperEmailTv = (TextView)convertView.findViewById(R.id.shopkeeper_email);
                recentOrdersViewHolder.totalCostTv = (TextView)convertView.findViewById(R.id.total_cost_vendor);
                recentOrdersViewHolder.status=(TextView)convertView.findViewById(R.id.status_order_vendor);
                convertView.setTag(recentOrdersViewHolder);
            }else {
                recentOrdersViewHolder = (RecentOrdersViewHolder)convertView.getTag();
            }
            HashMap<String, String> thisOrder = getItem(position);
            recentOrdersViewHolder.shopkeeperEmailTv.setText(thisOrder.get(ParseJSONRecentOrderVendor.SHOPKEEPER_EMAIL_STR));
            recentOrdersViewHolder.totalCostTv.setText(thisOrder.get(ParseJSONRecentOrderVendor.TOTAL_COST_STR));
            if(thisOrder.get(ParseJSONRecentOrderVendor.STATUS_STR).equalsIgnoreCase("0")) {
                recentOrdersViewHolder.status.setText("Not Delivered");
            }
            else
            {
                recentOrdersViewHolder.status.setText("Delivered");
            }
            return convertView;
        }
    }
}
