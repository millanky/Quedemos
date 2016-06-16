package com.proyecto.quedemos;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {

    //Comprobar si el usuario está logueado
    public boolean isLogued() {
        SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        String usuario = prefs.getString("user", "");
        //TODO: cambiar esto
        if (!usuario.equals("545")) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (isLogued()) {
            setContentView(R.layout.activity_main);
        } else {
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_main_not_logued);
        }

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    //-------------- M E N U ----------------------------
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //DISTINTO MENU SEGUN USUARIO LOGUEADO? *******************************
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
