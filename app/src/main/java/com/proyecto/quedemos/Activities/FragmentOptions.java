package com.proyecto.quedemos.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.R;

/**
 * Created by Usuario on 17/06/2016.
 */

public class FragmentOptions extends Fragment {

    Button btnApariencia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        return view;
    }

}
