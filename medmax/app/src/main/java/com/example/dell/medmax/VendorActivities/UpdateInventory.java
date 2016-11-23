package com.example.dell.medmax.VendorActivities;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.HashMap;
import java.util.Map;

public class UpdateInventory extends Fragment {

    public static final String JSON_URL = "http://medmax.pe.hu/showInventory.php";

    private FloatingActionButton addItem;
    String usermail;
    SharedPreferences sharedPreferences;
    private ProgressDialog mProgress;

    private ListView listView;
    private RelativeLayout relativeLayout;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        usermail = sharedPreferences.getString("mail","");
        listView = (ListView) view.findViewById(R.id.itemList);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.inventory_layout);
        relativeLayout.setVisibility(View.INVISIBLE);
        sendRequest();
        addItem = (FloatingActionButton) view.findViewById(R.id.add);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Additem.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), Editdelete.class);
                i.putExtra("pos", position);
                startActivity(i);
            }
        });
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
                            relativeLayout.setVisibility(View.VISIBLE);
                            showJSON(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        Toast.makeText(getActivity(),"please check your internet", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myparams = new HashMap<String, String>();
                myparams.put("email", usermail);
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


    /*
    private boolean deleteItem(final String c_name,final String i_name,final String i_price) {
        String URL = "http://dinein.esy.es/deleteItem.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("Data Deleted successfully...!!")){
                            Toast.makeText(UpdateInventory.this, "Item deleted" , Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(UpdateInventory.this, "problem while deleting item" , Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    if(error!=null){
                        Toast.makeText(UpdateInventory.this, "something went wrong" , Toast.LENGTH_LONG).show();

                    }
            }
        }
        ){ @Override
           protected Map<String, String> getParams()
        {
            Map<String, String> params = new HashMap<String, String>();
            params.put("companyname", c_name);
            params.put("itemname", i_name);
            params.put("itemnrice", i_price);
            return params;
        }
        };
    }*/
}