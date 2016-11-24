package com.example.dell.medmax;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.ShopkeeperActivities.ShopkeeperMainActivity;
import com.example.dell.medmax.VendorActivities.VendorMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aggarwal on 18-08-2016.
 */
public class LoginActivity extends AppCompatActivity {
    EditText loginId;
    EditText password;
    Spinner userType;
    Button login;
    TextView registerScreen;
    String email,pass,userTypes;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    String url = "http://medmax.pe.hu/loginscheck.php";
    SharedPreferences sharedPreferences=null;
    TextView forgotpass;
    private boolean noAccount;
    private boolean passwordWrong, userTypeWrong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedPreferences=this.getSharedPreferences("login", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        loginId=(EditText)findViewById(R.id.login_mail);
        password=(EditText)findViewById(R.id.login_password);
        userType=(Spinner)findViewById(R.id.login_userType);
        registerScreen = (TextView) findViewById(R.id.link_to_register);
        forgotpass=(TextView)findViewById(R.id.link_to_forgot);
        login=(Button)findViewById(R.id.btnLogin);
        Boolean connection = getIntent().getBooleanExtra("Connection", false);
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, com.example.dell.medmax.ForgotEmail.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noAccount = true;
                email=loginId.getText().toString();
                pass=password.getText().toString();
                userTypes=userType.getSelectedItem().toString();
                if(email.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
                }
                else if(pass.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override

                        public void onResponse(JSONObject response) {
                            //first take out JSON array from inside of JSON Object
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");//results is the name of the JSON array
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);//i is the index of the objects
                                    String name1 = obj.getString("name");//name is same as written in the JSOn url
                                    String email1 = obj.getString("email");
                                    String pass1=obj.getString("password");
                                    String cont1=obj.getString("phone");
                                    String usertype1=obj.getString("usertype");
                                    String address1=obj.getString("address");
                                    //Toast.makeText(Log_In.this, ""+name, Toast.LENGTH_SHORT).show();
                                    if (email1.equalsIgnoreCase(email) && pass1.equalsIgnoreCase(pass)&&userTypes.equalsIgnoreCase(usertype1)) {
//                                        Intent intent=new Intent(Log_In.this,Welcome.class);
//                                        startActivity(intent);
                                        Toast.makeText(LoginActivity.this,"Welcome "+name1,Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("loginTrue","true");
                                        editor.putString("name", name1);
                                        editor.putString("mail", email1);
                                        editor.putString("contact",cont1);
                                        editor.putString("password",pass1);
                                        editor.putString("usertype",usertype1);
                                        editor.putString("address",address1);
                                        editor.commit();
                                        noAccount = false;
                                        passwordWrong = false;
                                        userTypeWrong = false;
//                                        getFragmentManager().beginTransaction().replace(R.id.frame,new Ride_Available()).commit();
                                        if(usertype1.equalsIgnoreCase("vendor")) {
                                            Intent intent = new Intent(LoginActivity.this, VendorMainActivity.class);        //problem
                                            startActivity(intent);
                                            finish();
                                            break;
                                        }
                                        else
                                        {
                                            Intent intentShop=new Intent(getApplicationContext(), ShopkeeperMainActivity.class);
                                            startActivity(intentShop);
                                            finish();
                                            break;
                                            //Toast.makeText(LoginActivity.this, "Shopkeeper page still under construction", Toast.LENGTH_SHORT).show();
                                        }
                                    }else if (email1.equalsIgnoreCase(email) && userTypes.equalsIgnoreCase(usertype1)){
                                        noAccount = false;
                                        userTypeWrong = false;
                                        passwordWrong = true;
                                        break;
                                    }else if (email1.equalsIgnoreCase(email) && pass1.equalsIgnoreCase(pass)){
                                        noAccount = false;
                                        passwordWrong = false;
                                        userTypeWrong = true;
                                        break;
                                    }
                                }
                                if(noAccount) {
                                    Toast.makeText(LoginActivity.this,"Account does not exist, Create Account first...",Toast.LENGTH_SHORT).show();
                                }else if (passwordWrong){
                                    Toast.makeText(LoginActivity.this,"Please Make Sure your password is correct !!",Toast.LENGTH_SHORT).show();
                                }else if (userTypeWrong){
                                    Toast.makeText(LoginActivity.this,"Please make sure that you have selected the correct user type !1",Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "Login failed...Try Again", Toast.LENGTH_SHORT).show();
                            //System.out.println("++++++++++++++++++++++++++++++++++++" + error);
                        }
                    });

                    requestQueue.add(jsonObjectRequest);
                }
//                System.out.println(""+email+"\t"+pass+"\t"+userTypes);
            }
        });

        if (!connection){
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Connectivity Problem !!!");
            builder.setMessage("Please Connect to Internet \nEnsure that you are connected to a network or not !!");
            builder.setCancelable(true);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                super.onBackPressed();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
