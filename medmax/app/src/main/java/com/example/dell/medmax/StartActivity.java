package com.example.dell.medmax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.example.dell.medmax.R;
import com.example.dell.medmax.ShopkeeperActivities.ShopkeeperMainActivity;

/**
 * Created by Bhaskar Majithia on 21-10-2016.
 */
public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        SharedPreferences sharedPreferences=this.getSharedPreferences("login", Context.MODE_PRIVATE);
        String login=sharedPreferences.getString("loginTrue", "FALSE");
        String userType = sharedPreferences.getString("usertype","None");
        Boolean flag=isInternetAvailable();
        if(!flag)
        {
            Toast.makeText(StartActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(login.equalsIgnoreCase("true"))
        {
            if (userType.equalsIgnoreCase("Vendor")) {
                Intent intent = new Intent(StartActivity.this, com.example.dell.medmax.FirstActivity.class);
                startActivity(intent);
                finish();
            }else if(userType.equalsIgnoreCase("Shopkeeper")){
                Intent intent = new Intent(StartActivity.this, com.example.dell.medmax.ShopkeeperActivities.ShopkeeperMainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else
        {
            Intent intent=new Intent(StartActivity.this, com.example.dell.medmax.LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean isInternetAvailable() {
        boolean isWifiAvailable = false;
        boolean isMobileInternetAvailable = false;
        ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netwrkInfo = connectManager.getAllNetworkInfo();
        for (NetworkInfo networkInfor : netwrkInfo) {
            if (networkInfor.getTypeName().equalsIgnoreCase("WIFI")) {
                if (networkInfor.isConnected()) {
                    isWifiAvailable = true;
                }
            }
            if (networkInfor.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (networkInfor.isConnected()) {
                    isMobileInternetAvailable = true;
                }
            }
        }
        return isMobileInternetAvailable || isWifiAvailable;
    }
}
