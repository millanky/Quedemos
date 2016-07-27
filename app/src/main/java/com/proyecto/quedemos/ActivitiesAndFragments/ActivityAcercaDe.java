package com.proyecto.quedemos.ActivitiesAndFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ScrollingTabContainerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.proyecto.quedemos.R;
import com.proyecto.quedemos.VisualResources.Utils;

/**
 * Created by MartaMillan on 25/7/16.
 */
public class ActivityAcercaDe extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_acerca_de);
        ScrollView container = (ScrollView) findViewById(R.id.acercaContainer);

        SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);

        int currentColor = prefs.getInt("themeColor", -11443780);
        if (currentColor == -10066330) { //gris
            container.setBackground(getResources().getDrawable(R.drawable.bg_gris));
        } else if (currentColor == -6903239) { //verde
            container.setBackground(getResources().getDrawable(R.drawable.bg_verde));
        } else if (currentColor == -3716282) { //rojo
            container.setBackground(getResources().getDrawable(R.drawable.bg_rojo));
        } else if (currentColor == -752595) { //naranja
            container.setBackground(getResources().getDrawable(R.drawable.bg_naranja));
        } else if (currentColor == -12607520) { //azul claro
            container.setBackground(getResources().getDrawable(R.drawable.bg_azulclaro));
        } else {
            container.setBackground(getResources().getDrawable(R.drawable.bg_azuloscuro));
        }

    }

    public void enviarEmail (View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"quedemosapp@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Pregunta de usuario app");
        try {
            startActivity(Intent.createChooser(i, "Enviar email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No tiene aplicaciones para envío de emails instaladas.", Toast.LENGTH_SHORT).show();
        }
    }

    public void abrirFacebook (View v) {

        Intent i = newFacebookIntent(this.getPackageManager(),"https://www.facebook.com/quedemosapp/");
        startActivity(i);
    }
    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public void abrirTwitter (View v) {
        //"twitter://user?user_id=757588414879395845"));
        Intent intent = null;
        try {
            //Twitter app
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=QuedemosApp"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            //Explorador
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/QuedemosApp"));
        }
        startActivity(intent);
    }
}
