package com.example.dell.medmax.ShopkeeperActivities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopkeeperPlaceOrder1Fragment extends Fragment {

    ListView vendorList;
    public static final String VENDOR_LIST_URL = "http://medmax.pe.hu/vendor_list.php";
    private ArrayList<HashMap<String,String>> vendorArrayList = new ArrayList<>();
    private LinearLayout couldNotLoad;
    private TextView couldNotLoadTv;
    private ProgressDialog mProgress;

    public ShopkeeperPlaceOrder1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopkeeper_place_order1, container, false);
        Log.d("In On create View","121212");
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vendorList = (ListView)getActivity().findViewById(R.id.shop_order_vendor_list);
        couldNotLoad = (LinearLayout)getActivity().findViewById(R.id.could_not_load_layout);
        couldNotLoadTv = (TextView)getActivity().findViewById(R.id.not_load_recent_order_tv);
        Log.d("Got list view","121212");
        getData();
    }

    private void getData(){
        mProgress = ProgressDialog.show(getActivity(),"Loading ...","Please Wait !!");
        StringRequest stringRequest=new StringRequest(VENDOR_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]")) {
                    couldNotLoad.setVisibility(View.VISIBLE);
                    mProgress.dismiss();
                    Toast.makeText(getActivity(), "Could Not Load Vendors List!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.dismiss();
                    couldNotLoad.setVisibility(View.GONE);
                    vendorList.setVisibility(View.VISIBLE);
                    vendorArrayList = getJSON(response);
                    Log.d("Got response", vendorArrayList.toString());
                    VendorListAdapter vendorListAdapter = new VendorListAdapter(vendorArrayList);
                    vendorList.setAdapter(vendorListAdapter);
                    vendorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("Decision ?");
                            alertDialogBuilder.setMessage("What would you like to see ?");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setPositiveButton("INVENTORY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent showInventoryIntent = new Intent(getActivity(), PlaceOrder2Activity.class);
                                    showInventoryIntent.putExtra("EmailVendor",
                                            vendorArrayList.get(position).get(ParseJSONVendorList.VENDOR_EMAIL_STR));
                                    startActivity(showInventoryIntent);
                                }
                            });
                            alertDialogBuilder.setNeutralButton("ABOUT VENDOR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent aboutVendorIntent = new Intent(getActivity(), AboutVendorActivity.class);
                                    aboutVendorIntent.putExtra("EmailVendor",
                                            vendorArrayList.get(position).get(ParseJSONVendorList.VENDOR_EMAIL_STR));
                                    startActivity(aboutVendorIntent);
                                }
                            });
                            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                couldNotLoad.setVisibility(View.VISIBLE);
                couldNotLoadTv.setText("Sorry, There was some Connectivity Problem, Could Not Load Data !!");
                Toast.makeText(getActivity(), "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        Log.d("Got string request","121212");
    }

    private ArrayList<HashMap<String, String>> getJSON(String vendorListJSON){

        ParseJSONVendorList list = new ParseJSONVendorList(vendorListJSON);
        vendorArrayList = list.parseJSON();
        Log.d("In get JSON data parsed",vendorArrayList.toString());
        return vendorArrayList;
    }

    private class VendorListAdapter extends BaseAdapter {

        class VendorListViewHolder{
            TextView vendorNameTv;
            TextView vendorEmailIv;
        }

        private ArrayList<HashMap<String,String>> vendorList;

        public VendorListAdapter(ArrayList<HashMap<String,String>> list){
            this.vendorList = list;
        }

        @Override
        public int getCount() {
            Log.d("In Get Count", String.valueOf(vendorList.size()));
            return vendorList.size();
        }

        @Override
        public HashMap<String,String> getItem(int position) {
            return vendorList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            VendorListViewHolder vendorListViewHolder;

            if (convertView==null){
                convertView=inflater.inflate(R.layout.shop_order_vendor_list_item,null);
                vendorListViewHolder = new VendorListViewHolder();
                vendorListViewHolder.vendorNameTv = (TextView)convertView.findViewById(R.id.vendor_name_shop_order);
                vendorListViewHolder.vendorEmailIv = (TextView)convertView.findViewById(R.id.vendor_email_shop_order);
                convertView.setTag(vendorListViewHolder);
            }else {
                vendorListViewHolder = (VendorListViewHolder)convertView.getTag();
            }
            HashMap<String,String> thisVendor = getItem(position);
            vendorListViewHolder.vendorNameTv.setText(thisVendor.get(ParseJSONVendorList.VENDOR_NAME_STR));
            vendorListViewHolder.vendorEmailIv.setText(thisVendor.get(ParseJSONVendorList.VENDOR_EMAIL_STR));

            return convertView;
        }
    }


}
