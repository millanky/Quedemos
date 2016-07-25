package com.proyecto.quedemos.ActivitiesAndFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.proyecto.quedemos.R;
import com.proyecto.quedemos.VisualResources.Utils;

/**
 * Created by Usuario on 25/05/2016.
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.splash);
        RelativeLayout splashContainer = (RelativeLayout) findViewById(R.id.splashContainer);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.q_logo);

        SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        int currentColor = prefs.getInt("themeColor", -11443780); //azul oscuro por defecto
        if (currentColor == -10066330) { //gris
            splashContainer.setBackground(getResources().getDrawable(R.drawable.bg_gris));
        } else if (currentColor == -6903239) { //verde
            splashContainer.setBackground(getResources().getDrawable(R.drawable.bg_verde));
        } else if (currentColor == -3716282) { //rojo
            splashContainer.setBackground(getResources().getDrawable(R.drawable.bg_rojo));
        } else if (currentColor == -752595) { //naranja
            splashContainer.setBackground(getResources().getDrawable(R.drawable.bg_naranja));
        } else if (currentColor == -12607520) { //azul claro
            splashContainer.setBackground(getResources().getDrawable(R.drawable.bg_azulclaro));
        } else {
            splashContainer.setBackground(getResources().getDrawable(R.drawable.bg_azuloscuro));
        }


        Thread timerThread = new Thread (){ //nuevo hilo, se ejecuta en background

            public void run() {
                try {
                    sleep(3000); //se duerme 3 segundos, doy tiempo a recuperar Token
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally { //lanzar la MainActivity
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause(){ //poner en pausa si se lanza este metodo
        super.onPause();
        finish();
    }

}