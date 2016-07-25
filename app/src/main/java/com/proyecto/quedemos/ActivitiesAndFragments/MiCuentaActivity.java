package com.proyecto.quedemos.ActivitiesAndFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyecto.quedemos.R;
import com.proyecto.quedemos.VisualResources.ImageTransformation;
import com.proyecto.quedemos.VisualResources.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by MartaMillan on 23/7/16.
 */
public class MiCuentaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_mi_cuenta);

        ImageView imagen = (ImageView) findViewById(R.id.imagenPerfil);
        TextView nombre = (TextView) findViewById(R.id.nombreUsuario);

        SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        nombre.setText(prefs.getString("user",""));

        Picasso.with(this)
                .load(prefs.getString("picture",""))
                .placeholder(R.drawable.q_logo)
                .error(R.drawable.q_logo)
                .resize(400, 400)
                .transform(new ImageTransformation())
                .into(imagen);
    }

}
