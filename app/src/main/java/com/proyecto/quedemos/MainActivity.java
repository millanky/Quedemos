package com.proyecto.quedemos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookSdk;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main_not_logued);

        if (isLogued()) {
            getSupportActionBar().setTitle(user);
        } else {
            getSupportActionBar().setTitle("Quedemos!");
        }


/*
           // new downloadTask().execute(picture);
        }*/
    }

    //Comprobar si el usuario está logueado
    public boolean isLogued() {
        SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        user = prefs.getString("user", "");

        if (user.equals("")) {
            return false;
        } else {
            return true;
        }
    }


    /*********** TAREAS ASÍNCRONAS *************
    class downloadTask extends AsyncTask<String, Void, Drawable> {

        private Exception exception;
        private Drawable logo = null;

        protected Drawable doInBackground(String ... urls) {
            try {
                Bitmap x;

                HttpURLConnection connection = (HttpURLConnection) new URL(urls[0]).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();

                x = BitmapFactory.decodeStream(input);
                logo = new BitmapDrawable(x);
                onPostExecute(urls[0]);
                return logo;

            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String url) {
            Log.e("Completado"," OK");
            getSupportActionBar().setIcon(logo);
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }*/


    //-------------- M E N U ----------------------------
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //DISTINTO MENU SEGUN USUARIO LOGUEADO? *******************************
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            //TODO: no hay HomeFragment con los datos así que no puede hacer logout

            FacebookSdk.sdkInitialize(getApplicationContext());
            HomeFragment hf = new HomeFragment();
            hf.logout();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
