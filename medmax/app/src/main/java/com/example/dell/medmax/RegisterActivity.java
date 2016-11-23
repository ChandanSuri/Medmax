package com.example.dell.medmax;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aggarwal on 18-08-2016.
 */
public class RegisterActivity extends AppCompatActivity {
    EditText editName;
    EditText editEmailId;
    EditText editPhone;
    EditText editAddress;
    EditText editPassword;
    Spinner editUserType;
    Button signUp;
    TextView loginScreen;
    String name,email,phone,address,password,userType;
    String url="http://medmax.pe.hu/logins.php";
    RequestQueue requestQueue,requestQueue1;
    JsonObjectRequest jsonObjectRequest;
    String url1 = "http://medmax.pe.hu/loginscheck.php";
    Boolean flag,flags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        requestQueue= Volley.newRequestQueue(RegisterActivity.this);
        requestQueue1= Volley.newRequestQueue(RegisterActivity.this);
        loginScreen = (TextView) findViewById(R.id.link_to_login);
        editName=(EditText)findViewById(R.id.reg_name);
        editEmailId=(EditText)findViewById(R.id.reg_email);
        editPhone=(EditText)findViewById(R.id.reg_phone);
        editAddress=(EditText)findViewById(R.id.reg_address);
        editPassword=(EditText)findViewById(R.id.reg_password);
        editUserType=(Spinner)findViewById(R.id.reg_userType);
        signUp=(Button)findViewById(R.id.btnRegister);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                Intent intent=new Intent(RegisterActivity.this, com.example.dell.medmax.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=editName.getText().toString();
                email=editEmailId.getText().toString();
                phone=editPhone.getText().toString();
                address=editAddress.getText().toString();
                password=editPassword.getText().toString();
                userType=editUserType.getSelectedItem().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty() || !email.contains("@") || !email.contains("."))
                {
                    Toast.makeText(RegisterActivity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
                }
                else if(phone.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter Contact Number", Toast.LENGTH_SHORT).show();
                }
                else if(phone.length()<10||phone.length()>13)
                {
                    Toast.makeText(RegisterActivity.this, "Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
                }
                else if(address.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    isAccountExist(email,requestQueue1,jsonObjectRequest,url1);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(RegisterActivity.this, com.example.dell.medmax.LoginActivity.class);
        startActivity(i);
        finish();
    }
    void isAccountExist(final String mail,RequestQueue requestQueue,JsonObjectRequest jsonObjectRequest,String url1)
    {
//        System.out.print("fdfdsfdfds----------------------------------------------");
//        final boolean[] flag = new boolean[1];
//        flags=false;
        jsonObjectRequest = new JsonObjectRequest(url1, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean flag1=false;
                //first take out JSON array from inside of JSON Object
                try {
                    JSONArray jsonArray = response.getJSONArray("data");//results is the name of the JSON array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);//i is the index of the objects
                        String email1 = obj.getString("email");
                        //Toast.makeText(Log_In.this, ""+name, Toast.LENGTH_SHORT).show();
                        if (email1.equalsIgnoreCase(mail)) {
                            flag1=true;
                            Toast.makeText(RegisterActivity.this, "Account Already Exists", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if(!flag1)
                    {
//                        flag1=false;
                        register();
//                        Toast.makeText(RegisterActivity.this, "-----------------------------------", Toast.LENGTH_SHORT).show();
//                        System.out.print("fdfdsfdfds");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Action failed...Try Again"+error, Toast.LENGTH_SHORT).show();
                System.out.println("++++++++++++++++++++++++++++++++"+error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    void register()
    {
        editName=(EditText)findViewById(R.id.reg_name);
        editEmailId=(EditText)findViewById(R.id.reg_email);
        editPhone=(EditText)findViewById(R.id.reg_phone);
        editAddress=(EditText)findViewById(R.id.reg_address);
        editPassword=(EditText)findViewById(R.id.reg_password);
        editUserType=(Spinner)findViewById(R.id.reg_userType);
        name=editName.getText().toString();
        email=editEmailId.getText().toString();
        phone=editPhone.getText().toString();
        address=editAddress.getText().toString();
        password=editPassword.getText().toString();
        userType=editUserType.getSelectedItem().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.equalsIgnoreCase("1"))
                {
                    flag=true;
                    Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                }
                else if(s.equalsIgnoreCase("2"))
                {
                    flag=false;
//                                Toast.makeText(RegisterActivity.this, "Failed to process.TRY AGAIN", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(RegisterActivity.this, "Failed to process.TRY AGAIN" , Toast.LENGTH_SHORT).show();
                flag=false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myparams = new HashMap<String, String>();
                myparams.put("name", name);
                myparams.put("email", email);
                myparams.put("phone", phone);
                myparams.put("address",address);
                myparams.put("password", password);
                myparams.put("usertype", userType);
                return myparams;
            }
        };
        requestQueue.add(stringRequest);
        flag=true;
        if (flag) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Account created Successfully");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(RegisterActivity.this, com.example.dell.medmax.LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            Toast.makeText(RegisterActivity.this,"Failed to process.TRY AGAIN",Toast.LENGTH_SHORT).show();
        }
    }
}

