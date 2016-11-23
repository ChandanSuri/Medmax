package com.example.dell.medmax.VendorActivities;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;

import java.util.HashMap;
import java.util.Map;

public class Additem extends Activity {
    String URL1 = "http://medmax.pe.hu/addData.php";
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    EditText companyName=null;
    EditText itemName=null;
    EditText itemPrice=null;
    Button additem;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        requestQueue = Volley.newRequestQueue(Additem.this);
        additem=(Button)findViewById(R.id.addItem);
        sharedPreferences=this.getSharedPreferences("login", Context.MODE_PRIVATE);
        final String usermail = sharedPreferences.getString("mail", "");
        companyName=(EditText)findViewById(R.id.companyName);
        itemName=(EditText)findViewById(R.id.itemName);
        itemPrice=(EditText)findViewById(R.id.itemPrice);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String c_name=companyName.getText().toString();
                final String i_name=itemName.getText().toString();
                final String i_price=itemPrice.getText().toString();
                StringRequest stringRequest=new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.contains("Data Inserted successfully...!!"))
                        {
                            Toast.makeText(Additem.this,"Item added to list" , Toast.LENGTH_LONG).show();
                        }
                        else if (s.contains("Insertion Failed. Some Fields are Blank....!!")){
                            Toast.makeText(Additem.this,"Some fields were empty" , Toast.LENGTH_LONG).show();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Additem.this, "connection error", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("vendorsEmail", usermail);
                        params.put("companyName", c_name);
                        params.put("itemName", i_name);
                        params.put("itemPrice", i_price);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                Intent intent=new Intent(Additem.this,VendorMainActivity.class);
                startActivity(intent);
                finish();
                finish();
            }
        });

    }


}