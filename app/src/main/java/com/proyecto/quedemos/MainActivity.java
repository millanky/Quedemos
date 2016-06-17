package com.proyecto.quedemos;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String usuario;
    String picture;
    TabLayout tabLayout;
    ViewPager viewPager;


    //Comprobar si el usuario está logueado
    public boolean isLogued() {
        SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        usuario = prefs.getString("user", "");
        picture = prefs.getString("picture", "");

        //TODO: cambiar esto
        if (usuario.equals("")) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        if (isLogued()) {

            setContentView(R.layout.activity_main);

            viewPager = (ViewPager) findViewById(R.id.viewPager);
            viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));

            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            });
        } else {
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_main_not_logued);
            setTitle(usuario);


           // new downloadTask().execute(picture);
        }
    }

    /*********** FRAGMENT PAGER ADAPTER *********/
    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments[] = {"Mi Calendario", "Grupos", "Ajustes"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position){
            //create fragment
            switch (position){
                case 0:
                    return new FragmentCalendar();
                case 1:
                    return new FragmentGroups();
                case 2:
                    return new FragmentOptions();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return fragments[position];
        }
    }

    /*********** TAREAS ASÍNCRONAS *************/
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
    }


    //-------------- M E N U ----------------------------
    @Override
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

