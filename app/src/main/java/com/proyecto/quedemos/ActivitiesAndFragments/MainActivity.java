package com.proyecto.quedemos.ActivitiesAndFragments;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.proyecto.quedemos.InicioSesion.FacebookLoginFragment;
import com.proyecto.quedemos.InicioSesion.GoogleSignInActivity;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.RestAPI.Endpoints;
import com.proyecto.quedemos.RestAPI.adapter.RestApiAdapter;
import com.proyecto.quedemos.RestAPI.model.UsuarioResponse;
import com.proyecto.quedemos.VisualResources.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MartaMillan on 16/7/16.
 */
public class MainActivity extends AppCompatActivity {

    private String user;
    private Profile profile = null;
    private SharedPreferences prefs;
    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private Drawable oldBackground = null;
    private int currentColor; //0xFF666666;
    private MaterialDialog cambiarColorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Utils.onActivityCreateSetTheme(this);
        construirDialogoApariencia();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.q_logo);

        FacebookSdk.sdkInitialize(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            profile = (Profile) bundle.getParcelable(FacebookLoginFragment.PARCEL_KEY);
        } else {
            profile = Profile.getCurrentProfile();
        }

        if (isLogued()) {
            //enviamos token
            enviarDatosUsuarioFirebase();

            //customizamos view
            setContentView(R.layout.activity_main_logued);
            getSupportActionBar().setTitle(user);

            tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            pager = (ViewPager) findViewById(R.id.pager);
            adapter = new MyPagerAdapter(getSupportFragmentManager());

            pager.setAdapter(adapter);

            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            pager.setPageMargin(pageMargin);

            tabs.setViewPager(pager);

            prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
            currentColor = prefs.getInt("themeColor", -11443780); //azul oscuro por defecto
            changeColor(currentColor);
            tabs.setIndicatorColor(currentColor);

        } else {
            setContentView(R.layout.activity_main);
            getSupportActionBar().setTitle("Quedemos!");
            RelativeLayout layoutContainer = (RelativeLayout) findViewById(R.id.layoutContainer);
            if (currentColor == -10066330) { //gris
                layoutContainer.setBackground(getResources().getDrawable(R.drawable.bg_gris));
            } else if (currentColor == -6903239) { //verde
                layoutContainer.setBackground(getResources().getDrawable(R.drawable.bg_verde));
            } else if (currentColor == -3716282) { //rojo
                layoutContainer.setBackground(getResources().getDrawable(R.drawable.bg_rojo));
            } else if (currentColor == -752595) { //naranja
                layoutContainer.setBackground(getResources().getDrawable(R.drawable.bg_naranja));
            } else if (currentColor == -12607520) { //azul claro
                layoutContainer.setBackground(getResources().getDrawable(R.drawable.bg_azulclaro));
            } else {
                layoutContainer.setBackground(getResources().getDrawable(R.drawable.bg_azuloscuro));
            }
        }

/*
           // new downloadTask().execute(picture);
        }*/
    }

    //------------- COMPROBAR SI EL USUARIO ESTA LOGUEADO  --------

    public boolean isLogued() {
        prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        user = prefs.getString("user", "");

        if (user.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    // ----------- LOGUEAR CON GMAIL -----------
    public void sesionGmail(View view) {
        Intent i = new Intent(this, GoogleSignInActivity.class);
        startActivity(i);
    }

    // ----------- GUARDAR TOKEN ------------

    public void enviarDatosUsuarioFirebase() {

        //SOLICITAMOS EL TOKEN
        //String token = FirebaseInstanceId.getInstance().getToken();
        String tokenRecibido = prefs.getString("token","");
        String tokenAlmacenado = prefs.getString("tokenUltimo","");

        if (tokenRecibido == "") {
            Toast.makeText(this, "No se ha podido recibir el Token del dispositivo, no podrá recibir notificaciones.",
                    Toast.LENGTH_SHORT).show();
        }else if (tokenAlmacenado == "") {
            enviarTokenRegistro(tokenRecibido, prefs.getString("user",""),prefs.getString("picture",""));
        } else if (!tokenAlmacenado.equals(tokenRecibido)) {
            actualizarTokenRegistro(prefs.getString("databaseID",""),tokenRecibido);
        }
    }

    private void enviarTokenRegistro(String token, String user, String pic) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCall = endpoints.registrarTokenID(token, user, pic);

        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();

                prefs.edit().putString("tokenUltimo", usuarioResponse.getToken()).apply();
                prefs.edit().putString("databaseID", usuarioResponse.getId()).apply();

                Log.d("ID_FIREBASE", usuarioResponse.getId());
                Log.d("TOKEN_FIREBASE", usuarioResponse.getToken());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }

    private void actualizarTokenRegistro(String id,String token) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCallEdit = endpoints.actualizarTokenID(id, id, token);

        usuarioResponseCallEdit.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();

                prefs.edit().putString("token", usuarioResponse.getToken()).apply();
                prefs.edit().putString("databaseID", usuarioResponse.getId()).apply();

                Log.d("ID_FIREBASE", usuarioResponse.getId());
                Log.d("TOKEN_FIREBASE", usuarioResponse.getToken());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }

    //------------------- C A M B I A R  C O L O R ----------------

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

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        currentColor = newColor;

        SharedPreferences prefs = this.getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("themeColor", currentColor);
        edit.commit();

        if (cambiarColorDialog.isShowing()) {
            cambiarColorDialog.dismiss();
        }

        if (currentColor == -10066330) { //gris
            Utils.changeToTheme(this, Utils.THEME_GRIS);
            pager.setBackground(getResources().getDrawable(R.drawable.bg_gris));
        } else if  (currentColor == -6903239) { //verde
            Utils.changeToTheme(this, Utils.THEME_VERDE);
            pager.setBackground(getResources().getDrawable(R.drawable.bg_verde));
        } else if  (currentColor == -3716282) { //rojo
            Utils.changeToTheme(this, Utils.THEME_ROJO);
            pager.setBackground(getResources().getDrawable(R.drawable.bg_rojo));
        } else if  (currentColor == -752595) { //naranja
            Utils.changeToTheme(this, Utils.THEME_NARAJA);
            pager.setBackground(getResources().getDrawable(R.drawable.bg_naranja));
        } else if  (currentColor == -12607520) { //azul claro
            Utils.changeToTheme(this, Utils.THEME_AZULCLARITO);
            pager.setBackground(getResources().getDrawable(R.drawable.bg_azulclaro));
        } else {
            Utils.changeToTheme(this, Utils.THEME_DEFAULT);
            pager.setBackground(getResources().getDrawable(R.drawable.bg_azuloscuro));
        }
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
        public void scheduleDrawable(Drawable who, Runnable what, long when) {handler.postAtTime(what, when);}
        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {handler.removeCallbacks(what);}
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Calendario", "Quedadas", "Amigos"};

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
                return new FragmentAmigos();
            }
            //return TabFragment.newInstance(position);
        }

    }

    //-------------- MODALVIEW CAMBIAR APARIENCIA ---------

    public void cambiarApariencia () {
        cambiarColorDialog.show();
    }

    private void construirDialogoApariencia () {
        cambiarColorDialog = new MaterialDialog.Builder(this)
                .title("Elige un color")
                .icon(getResources().getDrawable(R.drawable.apariencia))
                .customView(R.layout.modal_paleta_colores, true)
                .positiveText("Cerrar")
                .build();
    }

    //-------------- M E N U ----------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //DISTINTO MENU SEGUN USUARIO LOGUEADO
        if (isLogued()) {
            getMenuInflater().inflate(R.menu.menu_logued, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            logout();
            return true;
        } else if (id == R.id.acerca){
            //Intent i = new Intent(this, MiCuentaActivity.class);
            //startActivity(i);
        }
        else if (id == R.id.apariencia){
            //Intent i = new Intent(this, MiCuentaActivity.class);
            //startActivity(i);
            cambiarApariencia();
        }
        else if (id == R.id.cuenta){
            Intent i = new Intent(this, MiCuentaActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    //-----------------  DESLOGUEARSE  -------------

    public void logout() {
        LoginManager.getInstance().logOut();

        SharedPreferences prefs = this.getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        prefs.edit().putString("user","").commit();

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
