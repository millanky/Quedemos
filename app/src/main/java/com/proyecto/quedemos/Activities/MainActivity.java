package com.proyecto.quedemos.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.proyecto.quedemos.R;

/**
 * Created by MartaMillan on 16/7/16.
 */
public class MainActivity extends AppCompatActivity {

    private String user;

    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setIcon(R.drawable.q_logo);

        FacebookSdk.sdkInitialize(getApplicationContext());


        if (isLogued()) {
            setContentView(R.layout.custom_view);
            getSupportActionBar().setTitle(user);

            tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            //tabs.setBackgroundColor(currentColor);
            //tabs.setTextColor(0xFFFFFF);
            pager = (ViewPager) findViewById(R.id.pager);
            adapter = new MyPagerAdapter(getSupportFragmentManager());

            pager.setAdapter(adapter);

            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            pager.setPageMargin(pageMargin);

            tabs.setViewPager(pager);

            changeColor(currentColor);
        } else {
            setContentView(R.layout.activity_main);
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

    private void changeColor(int newColor) {

        // change ActionBar color just if an ActionBar is available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            Drawable colorDrawable = new ColorDrawable(newColor);
            Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
            LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

            if (oldBackground == null) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    ld.setCallback(drawableCallback);
                } else {
                    getSupportActionBar().setBackgroundDrawable(ld);
                }

            } else {

                TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

                // workaround for broken ActionBarContainer drawable handling on
                // pre-API 17 builds
                // https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    td.setCallback(drawableCallback);
                } else {
                    getSupportActionBar().setBackgroundDrawable(td);
                }

                td.startTransition(200);

            }

            oldBackground = ld;

            // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

        }

        currentColor = newColor;

    }

    public void onColorClicked(View v) {

        int color = Color.parseColor(v.getTag().toString());
        tabs.setIndicatorColor(color);
        changeColor(color);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
        changeColor(currentColor);
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getSupportActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Calendario", "Grupos", "Opciones"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FragmentCalendar();
            }
            if (position == 1) {
                return new FragmentGroups();
            }
            else {
                return new FragmentOptions();
            }
            //return TabFragment.newInstance(position);
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
        if (id == R.id.logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //-----------------  DESLOGUEARSE  -------------

    public void logout() {
        LoginManager.getInstance().logOut();

        SharedPreferences prefs = this.getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
        /*SharedPreferences.Editor edit = prefs.edit();
        edit.clear();*/

        String pr_user = prefs.getString("user", "");
        Log.e("PRUEBA", pr_user);

        Intent intent = getIntent();
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


    //-------SALIR DE LA APLICACION AL PULSAR ATRAS

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Presiona otra vez para salir.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

}
