package com.example.dell.medmax.ShopkeeperActivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
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

public class AboutVendorActivity extends AppCompatActivity {

    public String emailVendor;
    public static final String VENDOR_INFO_URL = "http://medmax.pe.hu/show_vendor_detail.php";
    private TextView vendorNameTv, vendorEmailTv, vendorContactTv, vendorAddressTv;
    private ArrayList<HashMap<String, String>> vendorInfo = new ArrayList<>();
    private ProgressDialog mProgress;
    private LinearLayout vendorDetailsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_vendor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vendorNameTv = (TextView)findViewById(R.id.vendor_name_info);
        vendorEmailTv = (TextView) findViewById(R.id.vendor_email_info);
        vendorAddressTv = (TextView) findViewById(R.id.vendor_address_info);
        vendorContactTv = (TextView)findViewById(R.id.vendor_contact_info);
        vendorDetailsLayout = (LinearLayout)findViewById(R.id.vendor_details_layout);

        Intent intent = getIntent();
        emailVendor = intent.getStringExtra("EmailVendor");
        getData();
    }
    private void getData(){
        mProgress = ProgressDialog.show(AboutVendorActivity.this,"Loading ...","Please Wait !!");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, VENDOR_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                vendorDetailsLayout.setVisibility(View.VISIBLE);
                vendorInfo = getJSON(response);
                vendorNameTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_NAME_STR));
                vendorEmailTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_EMAIL_STR));
                vendorAddressTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_ADDRESS_STR));
                vendorContactTv.setText(vendorInfo.get(0).get(ParseJSONVendorInfo.VENDOR_CONTACT_STR));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                finish();
                Toast.makeText(AboutVendorActivity.this, "Please Check Your Internet Connectivity !!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailVendor);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AboutVendorActivity.this);
        requestQueue.add(stringRequest);
    }

    private ArrayList<HashMap<String, String>> getJSON(String vendorInfoJSON){
        ParseJSONVendorInfo parseJSONVendorInfo = new ParseJSONVendorInfo(vendorInfoJSON);
        vendorInfo = parseJSONVendorInfo.parseJSON();
        return vendorInfo;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AboutVendorActivity.this);
        builder.setTitle("Go Back ?");
        builder.setMessage("Are You Sure You want to Go back to see the vendor list ?");
        builder.setCancelable(true);
        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNegativeButton("YES, GO BACK!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AboutVendorActivity.this, "The list of available vendors shown", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AboutVendorActivity.this, ShopkeeperMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).create().show();
    }
}
