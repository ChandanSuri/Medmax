package com.example.dell.medmax;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.ForgotPassword;
import com.example.dell.medmax.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aggarwal on 08-10-2016.
 */
public class ForgotEmail extends AppCompatActivity {
    EditText email;
    Button btn;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    String url = "http://medmax.pe.hu/loginscheck.php";
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Provide Email Id");
        requestQueue = Volley.newRequestQueue(ForgotEmail.this);
        email=(EditText)findViewById(R.id.forgot_mail);
        btn=(Button)findViewById(R.id.btnemailforgot);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailId=email.getText().toString();
                jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        //first take out JSON array from inside of JSON Object
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");//results is the name of the JSON array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);//i is the index of the objects
                                String email1 = obj.getString("email");
                                //Toast.makeText(Log_In.this, ""+name, Toast.LENGTH_SHORT).show();
                                if (email1.equalsIgnoreCase(emailId)) {
//                                        Intent intent=new Intent(Log_In.this,Welcome.class);
//                                        startActivity(intent);
                                    flag=true;
                                    Intent intent=new Intent(ForgotEmail.this,ForgotPassword.class);
                                    intent.putExtra("email",emailId);
                                    startActivity(intent);
                                    finish();
//                                        getFragmentManager().beginTransaction().replace(R.id.frame,new Ride_Available()).commit();

                                    break;
                                }
                            }
                            if(!flag)
                            {
                                Toast.makeText(ForgotEmail.this,"Account does not exist, Check Email Id...",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotEmail.this, "Action failed...Try Again", Toast.LENGTH_SHORT).show();
                        System.out.println("++++++++++++++++++++++++++++++++++++" + error);
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
        });
    }
}

