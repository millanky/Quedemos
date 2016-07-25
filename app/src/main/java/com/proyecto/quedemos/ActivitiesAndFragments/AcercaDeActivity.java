package com.proyecto.quedemos.ActivitiesAndFragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.proyecto.quedemos.R;
import com.proyecto.quedemos.VisualResources.Utils;

/**
 * Created by MartaMillan on 25/7/16.
 */
public class AcercaDeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_acerca_de);



    }
}
