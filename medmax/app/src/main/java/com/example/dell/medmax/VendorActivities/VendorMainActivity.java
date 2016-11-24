package com.example.dell.medmax.VendorActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.medmax.AboutApp;
import com.example.dell.medmax.AboutUs;
import com.example.dell.medmax.LoginActivity;
import com.example.dell.medmax.R;
import com.example.dell.medmax.StartActivity;
import com.example.dell.medmax.UserDetail;

import java.util.ArrayList;

public class VendorMainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout = null;
    ImageView imageView = null,imageHome;
    SharedPreferences sharedPreferences=null;
    TextView txt1,txt2;
    ArrayList<DatasClass> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Vendor Recent Orders ");
        sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
//        imageHome=(ImageView)findViewById(R.id.homes);
        ListView list = (ListView) findViewById(R.id.nav_list);
        String[] names = {"HOME","ACCOUNT", "INVENTORY", "UPDATE","LOGOUT"};
        Integer[] imageId={R.drawable.home,R.drawable.account,R.drawable.inventory,R.drawable.document,R.drawable.logout};
//        imageView = (ImageView) findViewById(R.id.img);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawf);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

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
                        getSupportActionBar().setTitle(" Vendor Recent Orders ");
                        getFragmentManager().beginTransaction().replace(R.id.frame, new VendorMainFragment()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 2: {
                        getSupportActionBar().setTitle(" Vendor Info ");
                        getFragmentManager().beginTransaction().replace(R.id.frame, new UserDetail()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 3: {
                        getSupportActionBar().setTitle(" Vendor Inventory ");
                        getFragmentManager().beginTransaction().replace(R.id.frame, new InventoryDisplay()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 4: {
                        getSupportActionBar().setTitle(" Update Inventory ");
                        getFragmentManager().beginTransaction().replace(R.id.frame, new UpdateInventory()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 5: {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(VendorMainActivity.this);
                        builder.setMessage("Are you sure you want to Logout ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(VendorMainActivity.this, com.example.dell.medmax.LoginActivity.class);
                                startActivity(intent);
                                finish();
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
                        break;
                    }
                }
            }
        });
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.closeDrawers();
//                } else {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//            }
//        });
//        drawerLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_send));
//                } else {
//                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.images2));
//                }
//            }
//        });
//        imageHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.frame, new VendorMainFragment()).commit();
//            }
//        });
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leave the Application ?");
        builder.setMessage("Are you sure you want to Exit !!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                super.onBackPressed();
                //Intent backIntent = new Intent(VendorMainActivity.this, StartActivity.class);
                //startActivity(backIntent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        //add pop up here
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFragmentManager().beginTransaction().replace(R.id.frame, new VendorMainFragment()).commit();
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
            textView.setText(name);
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopkeeper_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent aboutAppIntent = new Intent(VendorMainActivity.this, AboutApp.class);
            startActivity(aboutAppIntent);
            return true;
        }else if (id == R.id.action_about_us){
            Intent aboutAppIntent = new Intent(VendorMainActivity.this, AboutUs.class);
            startActivity(aboutAppIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
