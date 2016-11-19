package com.example.dell.medmax;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FirstActivity extends Activity {
    DrawerLayout drawerLayout = null;
    ImageView imageView = null,imageHome;
    SharedPreferences sharedPreferences=null;
    TextView txt1,txt2;
    ArrayList<DatasClass> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
        imageHome=(ImageView)findViewById(R.id.homes);
        ListView list = (ListView) findViewById(R.id.nav_list);
        String[] names = {"HOME","ACCOUNT", "INVENTORY", "UPDATE","LOGOUT"};
        Integer[] imageId={R.drawable.home,R.drawable.account,R.drawable.inventory,R.drawable.document,R.drawable.logout};
        imageView = (ImageView) findViewById(R.id.img);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawf);
        for (int i = 0; i < names.length; i++) {
            DatasClass d = new DatasClass(names[i], imageId[i]);
            datas.add(d);
        }
        MyAdapter adapter = new MyAdapter(this, R.layout.list_for_headers, datas);
        list.setAdapter(adapter);
        list.addHeaderView(getLayoutInflater().inflate(R.layout.drawer_list_header, null, false));
        txt1=(TextView)findViewById(R.id.nav_name);
        txt2=(TextView)findViewById(R.id.nav_email);

        txt1.setText("" + sharedPreferences.getString("name", ""));
        txt2.setText(""+sharedPreferences.getString("mail", ""));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new DummyFragment()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 2: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new UserDetail()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 3: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new InventoryDisplay()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 4: {
                        getFragmentManager().beginTransaction().replace(R.id.frame, new MainFragment()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 5: {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(FirstActivity.this, com.example.dell.medmax.LoginActivity.class);
                        startActivity(intent);
                        finish();
                        finish();
                        break;
                    }
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        drawerLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_send));
                } else {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.images2));
                }
            }
        });
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame, new DummyFragment()).commit();
            }
        });
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
        //add pop up here
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFragmentManager().beginTransaction().replace(R.id.frame, new DummyFragment()).commit();
    }
    public class MyAdapter extends ArrayAdapter<DataClass> {
        Context context;
        int ResId;
        ArrayList<DataClass> data;
        MyAdapter(Context context, int ResId, ArrayList data) {
            super(context, ResId, data);
            this.context = context;
            this.ResId = ResId;
            this.data = data;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(ResId, parent, false);
            ImageView img = (ImageView) view.findViewById(R.id.drawerimage);
            DatasClass obj = datas.get(position);
            String name = obj.getName();
            Integer number = obj.getNumber();
            img.setImageDrawable(getDrawable(number));
            TextView textView = (TextView) view.findViewById(R.id.drawertext);
            textView.setText(name) ;
            return view;
        }
    }
}
