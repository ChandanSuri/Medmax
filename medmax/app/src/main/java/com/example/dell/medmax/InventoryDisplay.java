package com.example.dell.medmax;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Aggarwal on 26-10-2016.
 */
public class InventoryDisplay extends Fragment {
    public static final String JSON_URL = "http://medmax.pe.hu/showInventory.php";

    private Button addItem;
    String usermail;
    SharedPreferences sharedPreferences;

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventory_display, container, false);
        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        usermail = sharedPreferences.getString("mail", "");
        listView = (ListView) view.findViewById(R.id.itemLists);
        sendRequest();
        return view;
    }
    private void sendRequest() {

        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "please check your internet", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void showJSON(String json) {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON(usermail);
        CustomList cl = new CustomList(getActivity(), ParseJSON.company, ParseJSON.item, ParseJSON.price);
        listView.setAdapter(cl);
    }
}
