package com.proyecto.quedemos.PushNotifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.proyecto.quedemos.ActivitiesAndFragments.MainActivity;

/**
 * Created by MartaMillan on 10/7/16.
 */
public class NotificationIDTokenService extends FirebaseInstanceIdService {


    private static final String TAG="FIREBASE_TOKEN";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        enviarTokenRegistro(token);
    }

    private void enviarTokenRegistro(String token) { //guardar en las shared preferences para cuando el usuario haya iniciado sesion
        SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);

        prefs.edit().putString("token", token).commit(); //guardo el token cada vez que recibo uno nuevo

        String idFirebase = prefs.getString("databaseID","");

        if (!idFirebase.equals("")) {//si hay ID almacenado es que ya se han subido datos a firebase
            MainActivity.actualizarTokenRegistro(idFirebase,token);
        }
        //Si no se han subido datos a firebase, se subirá el token almacenado en las shared preferences cuando se inicie sesión

        Log.e(TAG,token);
    }
}
