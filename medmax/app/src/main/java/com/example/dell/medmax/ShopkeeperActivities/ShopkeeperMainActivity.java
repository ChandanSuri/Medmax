package com.example.dell.medmax.ShopkeeperActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.medmax.AboutApp;
import com.example.dell.medmax.AboutUs;
import com.example.dell.medmax.R;
import com.example.dell.medmax.UserDetail;

public class ShopkeeperMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    //Activity Names
    public static final String PLACE_ORDER_1 = "Vendors";
    public static final String RECENT_ORDERS = "Recent Orders";
    public static final String USER_DETAILS = "Account Info";

    private TextView shopNameNavTv, shopMailNavTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.shopkeeper_main,new ShopkeeperPlaceOrder1Fragment()).commit();
                getSupportActionBar().setTitle(PLACE_ORDER_1);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.shop_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        shopMailNavTv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.shopkeeper_mail_nav);
        shopNameNavTv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.shopkeeper_name_nav);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String shopEmail = sharedPreferences.getString("mail","Sorry ! Not Able to retrieve !!");
        String shopName = sharedPreferences.getString("name","Could Not be Retrieved!");
        shopMailNavTv.setText(shopEmail);
        shopNameNavTv.setText(shopName);
    }

    @Override
    protected void onStart(){
        super.onStart();
        getFragmentManager().beginTransaction().replace(R.id.shopkeeper_main, new ShopkeeperMainFragment()).commit();
        getSupportActionBar().setTitle(RECENT_ORDERS);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Want to leave ?");
            builder.setMessage("Are You Sure that you want to close the application ?");
            builder.setCancelable(true);
            builder.setPositiveButton("YEAHH !", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setNegativeButton("NO, I WANNA STAY !", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
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
            Intent aboutAppIntent = new Intent(ShopkeeperMainActivity.this, AboutApp.class);
            startActivity(aboutAppIntent);
            return true;
        }else if (id == R.id.action_about_us){
            Intent aboutAppIntent = new Intent(ShopkeeperMainActivity.this, AboutUs.class);
            startActivity(aboutAppIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.order_history) {
            getFragmentManager().beginTransaction().replace(R.id.shopkeeper_main,new ShopkeeperMainFragment()).commit();
            getSupportActionBar().setTitle(RECENT_ORDERS);
            drawer.closeDrawers();
        } else if (id == R.id.new_order) {
            getFragmentManager().beginTransaction().remove(new ShopkeeperMainFragment()).commit();
            getFragmentManager().beginTransaction().replace(R.id.shopkeeper_main,new ShopkeeperPlaceOrder1Fragment()).commit();
            getSupportActionBar().setTitle(PLACE_ORDER_1);
            drawer.closeDrawers();
        } else if (id == R.id.account) {
            getFragmentManager().beginTransaction().replace(R.id.shopkeeper_main,new UserDetail()).commit();
            getSupportActionBar().setTitle(USER_DETAILS);
            drawer.closeDrawers();
        } else if (id == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShopkeeperMainActivity.this);
            builder.setTitle("LOGOUT !!");
            builder.setMessage("Are You Sure You want to Logout !!");
            builder.setCancelable(true);
            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).setNegativeButton("YES, LOGOUT!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(ShopkeeperMainActivity.this, com.example.dell.medmax.LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(ShopkeeperMainActivity.this, "Logout!!", Toast.LENGTH_SHORT).show();
                }
            }).create().show();
            drawer.closeDrawers();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
