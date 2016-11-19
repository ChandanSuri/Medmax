package com.example.dell.medmax;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainFragment extends Fragment {

    public static final String JSON_URL = "http://medmax.pe.hu/showInventory.php";

    private Button addItem;
    String usermail;
    SharedPreferences sharedPreferences;

    private ListView listView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        usermail = sharedPreferences.getString("mail","");
        listView = (ListView) view.findViewById(R.id.itemList);
        sendRequest();
        addItem = (Button) view.findViewById(R.id.add);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.dell.medmax.Additem.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getActivity(), com.example.dell.medmax.Editdelete.class);
                i.putExtra("pos",position);
                startActivity(i);
            }
        });
        return view;
    }

    private void sendRequest() {

        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"please check your internet", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON(usermail);
        CustomList cl = new CustomList(getActivity(), ParseJSON.company, ParseJSON.item, ParseJSON.price);
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
                            Toast.makeText(MainFragment.this, "Item deleted" , Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(MainFragment.this, "problem while deleting item" , Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    if(error!=null){
                        Toast.makeText(MainFragment.this, "something went wrong" , Toast.LENGTH_LONG).show();

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