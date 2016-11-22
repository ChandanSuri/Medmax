package com.example.dell.medmax;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.LoginActivity;
import com.example.dell.medmax.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aggarwal on 08-10-2016.
 */
public class ForgotPassword extends AppCompatActivity{
    EditText pass,pass1;
    Button btn;
    String url="http://medmax.pe.hu/changepassword.php";
    RequestQueue requestQueue;
    boolean flag1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password !");
        requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        final String email=getIntent().getStringExtra("email");
        pass=(EditText)findViewById(R.id.forgot_password);
        pass1=(EditText)findViewById(R.id.forgot_password_confirm);
        btn=(Button)findViewById(R.id.btnforgotpassword);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password=pass.getText().toString();
                String password2=pass1.getText().toString();
                if(password.equals(""))
                {
                    Toast.makeText(ForgotPassword.this, "Password is Required", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(password2))
                {
                    Toast.makeText(ForgotPassword.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if(s.equalsIgnoreCase("1"))
                            {
                                flag1=true;
                                Toast.makeText(ForgotPassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            }
                            else if(s.equalsIgnoreCase("2"))
                            {
                                flag1=false;
//                                Toast.makeText(RegisterActivity.this, "Failed to process.TRY AGAIN", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(RegisterActivity.this, "Failed to process.TRY AGAIN" , Toast.LENGTH_SHORT).show();
                            flag1=false;
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> myparams = new HashMap<String, String>();
                            myparams.put("email", email);
                            myparams.put("password", password);
                            return myparams;
                        }
                    };
                    requestQueue.add(stringRequest);
                    flag1=true;
                    if (flag1) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                        builder.setMessage("Password Changed Successfully");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else
                    {
                        Toast.makeText(ForgotPassword.this,"Failed to process.TRY AGAIN",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

