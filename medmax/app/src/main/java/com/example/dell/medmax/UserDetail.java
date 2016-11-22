package com.example.dell.medmax;
import android.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.medmax.R;
import com.example.dell.medmax.ShopkeeperActivities.ShopkeeperMainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aggarwal on 08-10-2016.
 */
public class UserDetail extends Fragment {

    EditText name_edit;
    EditText mail_edit;
    EditText contact_edit;
    EditText address_edit;
    SharedPreferences sharedPreferences;
    Button button;
    boolean flag,flag1;
    String url="http://medmax.pe.hu/updateuser.php";
    RequestQueue requestQueue;
    private ProgressDialog mProgress;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_detail, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        button=(Button)view.findViewById(R.id.user_btn);
        flag=false;
        FrameLayout contentFrameLayout = (FrameLayout) view.findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getActivity().getLayoutInflater().inflate(R.layout.user_detail, contentFrameLayout);
        requestQueue= Volley.newRequestQueue(getActivity());
        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("name", "");
        final String usermail = sharedPreferences.getString("mail", "");
        final String usercontact = sharedPreferences.getString("contact", "");
        final String useraddress = sharedPreferences.getString("address", "");
        name_edit=(EditText)view.findViewById(R.id.user_name);
        mail_edit=(EditText)view.findViewById(R.id.user_mail);
        contact_edit=(EditText)view.findViewById(R.id.user_number);
        address_edit=(EditText)view.findViewById(R.id.user_address);
        name_edit.setText(username);
        mail_edit.setText(usermail);
        contact_edit.setText(usercontact);
        address_edit.setText(useraddress);
        name_edit.setEnabled(false);
        mail_edit.setEnabled(false);
        contact_edit.setEnabled(false);
        address_edit.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag)
                {
                    name_edit.setEnabled(true);
                    contact_edit.setEnabled(true);
                    address_edit.setEnabled(true);
                    button.setText("SAVE CHANGES");
                    flag=true;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mProgress = ProgressDialog.show(getActivity(),"Loading ...","Please Wait !!");
                            if(flag)
                            {
                                final String new_name=name_edit.getText().toString();
                                final String new_contact=contact_edit.getText().toString();
                                final String new_address=address_edit.getText().toString();
                                final String new_mail=usermail;
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        mProgress.dismiss();
                                        if(s.equalsIgnoreCase("1"))
                                        {
                                            flag1=true;
                                            Toast.makeText(getActivity(), "Account updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(s.equalsIgnoreCase("2"))
                                        {
                                            flag1=false;
                                            Toast.makeText(getActivity(), "Failed to process, please Try Again !!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity(), "Failed to process, please Try Again!!" , Toast.LENGTH_SHORT).show();
                                        flag1=false;
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> myparams = new HashMap<String, String>();
                                        myparams.put("name", new_name);
                                        myparams.put("email", new_mail);
                                        myparams.put("phone", new_contact);
                                        myparams.put("address",new_address);
                                        return myparams;
                                    }
                                };
                                requestQueue.add(stringRequest);
                                flag1=true;
                                if (flag1) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("Account updated Successfully");
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.remove("name");
                                    editor.remove("contact");
                                    editor.remove("address");
                                    editor.putString("name", new_name);
                                    editor.putString("contact", new_contact);
                                    editor.putString("address", new_address);
                                    editor.commit();
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getActivity(), ShopkeeperMainActivity.class);
                                            startActivity(intent);

                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Failed to process.TRY AGAIN",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
}
