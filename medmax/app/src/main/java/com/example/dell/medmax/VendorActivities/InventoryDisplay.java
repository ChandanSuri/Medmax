package com.example.dell.medmax.VendorActivities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;
import com.example.dell.medmax.ShopkeeperActivities.FinalPlaceOrderActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aggarwal on 26-10-2016.
 */
public class InventoryDisplay extends Fragment {
    public static final String JSON_URL = "http://medmax.pe.hu/showInventory.php";

    private Button addItem;
    String usermail;
    SharedPreferences sharedPreferences;

    private ProgressDialog mProgress;
    private RelativeLayout inventoryDisplayLayout;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventory_display, container, false);
        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        usermail = sharedPreferences.getString("mail", "");
        listView = (ListView) view.findViewById(R.id.itemLists);
        inventoryDisplayLayout = (RelativeLayout)view.findViewById(R.id.inventory_disp_layout);
        inventoryDisplayLayout.setVisibility(View.INVISIBLE);
        sendRequest();
        return view;
    }
    private void sendRequest() {
        mProgress = ProgressDialog.show(getActivity(),"Loading ...","Please Wait !!");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        if(response.equals("[]")) {
                            Toast.makeText(getActivity(), "Empty Inventory", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            inventoryDisplayLayout.setVisibility(View.VISIBLE);
                            showJSON(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myparams = new HashMap<String, String>();
                myparams.put("email", usermail);
                Log.d("useremailfori",usermail);
                return myparams;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void showJSON(String json) {
        ParseJSONInventory pj = new ParseJSONInventory(json);
        pj.parseJSON(usermail);
        CustomList cl = new CustomList(getActivity(), ParseJSONInventory.company, ParseJSONInventory.item, ParseJSONInventory.price);
        listView.setAdapter(cl);
    }
}
