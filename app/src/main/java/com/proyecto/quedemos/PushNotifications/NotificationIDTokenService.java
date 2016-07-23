package com.proyecto.quedemos.PushNotifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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

    private void enviarTokenRegistro(String token) { //enviar registro al servidor
        Log.e(TAG,token);
    }
}
