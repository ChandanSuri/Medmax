package com.example.dell.medmax;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dell.medmax.R;

/**
 * Created by Bhaskar Majithia on 10/10/2016.
 */

public class CustomList extends ArrayAdapter<String> {
    private String[] companyName;
    private String[] itemName;
    private String[] itemPrice;
    private Activity context;
    public CustomList(Activity context, String[] companyName, String[] itemName, String[] itemPrice) {
        super(context, R.layout.item, companyName);
        this.context = context;
        this.companyName = companyName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    String company;
    String item;
    String price;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.item, null, true);
        TextView comanyname = (TextView) listViewItem.findViewById(R.id.textView4);
        TextView itemname = (TextView) listViewItem.findViewById(R.id.textView2);
        TextView itemprice = (TextView) listViewItem.findViewById(R.id.textView3);
        comanyname.setText(companyName[position]);
        itemname.setText(itemName[position]);
        itemprice.setText(itemPrice[position]);
//        comanyname.setText("hgfghfh");
//        itemname.setText("ggfghg");
//        itemprice.setText("666");
        return listViewItem;
    }
}