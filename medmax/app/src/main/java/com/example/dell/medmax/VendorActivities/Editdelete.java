package com.example.dell.medmax.VendorActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;

import java.util.HashMap;
import java.util.Map;

public class Editdelete extends Activity {

    Button update, delete;

    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdelete);
        sharedPreferences=this.getSharedPreferences("login", Context.MODE_PRIVATE);
        final String usermail = sharedPreferences.getString("mail", "");
        final Intent intent = getIntent();
        final int pos = intent.getExtras().getInt("pos");
        final TextView companyname = (TextView) findViewById(R.id.companyName1);
        final TextView itemname = (TextView) findViewById(R.id.itemName1);
        final EditText itemprice = (EditText) findViewById(R.id.itemPrice1);
        companyname.setText(ParseJSONInventory.company[pos]);
        itemname.setText(ParseJSONInventory.item[pos]);
        itemprice.setText(ParseJSONInventory.price[pos]);
        update = (Button) findViewById(R.id.updateInventory);
        delete = (Button) findViewById(R.id.delete);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "http://medmax.pe.hu/update.php";
                final String updateprice = ((EditText) findViewById(R.id.itemPrice1)).getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("Data Updated successfully...!!")) {

                            Toast.makeText(Editdelete.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                        } else if (response.contains("Insertion Failed. Some Fields are Blank....!!")) {

                            Toast.makeText(Editdelete.this, "item price can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Editdelete.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("vendorsEmail", usermail);
                        params.put("companyName", ParseJSONInventory.company[pos]);
                        params.put("itemName", ParseJSONInventory.item[pos]);
                        params.put("updateprice", updateprice);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Editdelete.this);
                requestQueue.add(stringRequest);
                Intent intent = new Intent(Editdelete.this, VendorMainActivity.class);
                startActivity(intent);
                finish();
                finish();
            }
        });


    delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String URL1="http://medmax.pe.hu/deleteItem.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    if (response.contains("Data Deleted successfully...!!")) {

                        Toast.makeText(Editdelete.this, "deleted Successfully", Toast.LENGTH_SHORT).show();

                    } else if (response.contains("Deletion Failed...")) {

                        Toast.makeText(Editdelete.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Editdelete.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("vendorsName", usermail);
                    params.put("companyName", ParseJSONInventory.company[pos]);
                    Log.i(companyname.getText().toString(), "getParams: ");
                    params.put("itemName", ParseJSONInventory.item[pos]);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Editdelete.this);
            requestQueue.add(stringRequest);
            Intent intent1=new Intent(Editdelete.this,VendorMainActivity.class);
            startActivity(intent1);
            finish();
            finish();
        }
    });
    }
}
