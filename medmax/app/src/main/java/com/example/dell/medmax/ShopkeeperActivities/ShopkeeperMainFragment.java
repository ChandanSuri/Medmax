package com.example.dell.medmax.ShopkeeperActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopkeeperMainFragment extends Fragment {

    private ListView shopkeeperOrdersListView;
    public static final String RECENT_ORDERS_URL = "http://medmax.pe.hu/recent_order_shopkeeper.php";
    private ArrayList<HashMap<String,String>> recentOrdersArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopkeeper_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shopkeeperOrdersListView = (ListView)getActivity().findViewById(R.id.shopkeeper_recent_orders_list);
        Log.d("Got list view","121212");
        getData();
    }

    private void getData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String shopEmail = sharedPreferences.getString("mail","Sorry ! Not Able to retrieve !!");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECENT_ORDERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recentOrdersArrayList = getJSON(response);
                RecentOrdersListAdapter recentOrdersListAdapter = new RecentOrdersListAdapter(recentOrdersArrayList);
                shopkeeperOrdersListView.setAdapter(recentOrdersListAdapter);
                shopkeeperOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent orderInfoIntent = new Intent(getActivity(), OrderInfoActivity.class);
                        orderInfoIntent.putExtra("OrderId",recentOrdersArrayList.get(position).get(ParseJSONRecentOrdersList.ORDER_ID_STR));
                        orderInfoIntent.putExtra("Vendor Email",recentOrdersArrayList.get(position).get(ParseJSONRecentOrdersList.VENDOR_EMAIL_STR));
                        orderInfoIntent.putExtra("Total Cost",recentOrdersArrayList.get(position).get(ParseJSONRecentOrdersList.TOTAL_COST_STR));
                        startActivity(orderInfoIntent);
                        Toast.makeText(getActivity(), "Clicked!!"+recentOrdersArrayList.get(position).get(ParseJSONRecentOrdersList.ORDER_ID_STR), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please Check Your Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", shopEmail);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private ArrayList<HashMap<String, String>> getJSON(String recentOrderList){
        ParseJSONRecentOrdersList parseJSONRecentOrdersList = new ParseJSONRecentOrdersList(recentOrderList);
        recentOrdersArrayList = parseJSONRecentOrdersList.parseJSON();
        return recentOrdersArrayList;
    }

    private class RecentOrdersListAdapter extends BaseAdapter {

        class RecentOrdersViewHolder{
            TextView vendorEmailTv;
            TextView totalCostTv;
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
                convertView = inflater.inflate(R.layout.recent_order_list_item, parent, false);
                recentOrdersViewHolder = new RecentOrdersViewHolder();
                recentOrdersViewHolder.vendorEmailTv = (TextView)convertView.findViewById(R.id.vendor_email_tv);
                recentOrdersViewHolder.totalCostTv = (TextView)convertView.findViewById(R.id.total_cost_tv);
                convertView.setTag(recentOrdersViewHolder);
            }else {
                recentOrdersViewHolder = (RecentOrdersViewHolder)convertView.getTag();
            }
            HashMap<String, String> thisOrder = getItem(position);
            recentOrdersViewHolder.vendorEmailTv.setText(thisOrder.get(ParseJSONRecentOrdersList.VENDOR_EMAIL_STR));
            recentOrdersViewHolder.totalCostTv.setText(thisOrder.get(ParseJSONRecentOrdersList.TOTAL_COST_STR));
            return convertView;
        }
    }

}
