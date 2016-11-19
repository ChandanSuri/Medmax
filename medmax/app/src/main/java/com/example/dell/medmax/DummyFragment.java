package com.example.dell.medmax;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Aggarwal on 08-10-2016.
 */
public class DummyFragment extends Fragment {
    TextView chkProfile,chkInventory;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy, container, false);
        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        chkProfile=(TextView)view.findViewById(R.id.chkbtn);
        chkInventory=(TextView)view.findViewById(R.id.chkinventory);
        chkProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame, new UserDetail()).commit();

            }
        });
        chkInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame, new MainFragment()).commit();

            }
        });
        return view;
    }
}
