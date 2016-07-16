package com.proyecto.quedemos.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.R;


/**
 * Created by Usuario on 17/06/2016.
 */
public class FragmentGroups extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_groups, container, false);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new MaterialDialog.Builder(getContext())
                        .title("Hola")
                        .customView(R.layout.custom_view, true) //true indica con ScrollView
                        .positiveText("Aceptar")
                        .show();

            }
        });

        return  view;
    }
}