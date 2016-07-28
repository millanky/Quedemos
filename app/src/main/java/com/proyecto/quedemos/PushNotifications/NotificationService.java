package com.proyecto.quedemos.PushNotifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.proyecto.quedemos.ActivitiesAndFragments.MainActivity;
import com.proyecto.quedemos.R;

/**
 * Created by MartaMillan on 5/7/16.
 */
public class NotificationService extends FirebaseMessagingService {

    public static final String TAG = "FIREBASE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification().getTitle().equals("Quedemos!")) {  //UN TOQUE

            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("toOpen", 2); //que abra el fragment Amigos
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

            String stringColor = remoteMessage.getNotification().getColor();
            int color = Integer.parseInt(stringColor.replaceFirst("^#", ""), 16);
            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //lo que el usuario tenga puesto para notifiacioes

            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.q_logo_sin_fondo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSound(sonido)
                    .setContentIntent(pendingIntent)
                    .setColor(color)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificacion.build());

        } else if (remoteMessage.getNotification().getTitle().equals("Inivitación quedada")) {  //NUEVA QUEDADA

            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("toOpen", 1); //que abra el fragment quedadas
            String idquedada = remoteMessage.getData().get("idquedada");
            i.putExtra("idQuedada",idquedada); //id de la quedada en Firebase
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

            String stringColor = remoteMessage.getNotification().getColor();
            int color = Integer.parseInt(stringColor.replaceFirst("^#", ""), 16);
            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.q_logo_sin_fondo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSound(sonido)
                    .setContentIntent(pendingIntent)
                    .setColor(color)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificacion.build());

        } else { //CUALQUIER OTRA NOTIFICACION

            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

            Intent i = new Intent(this, MainActivity.class); //a donde dirijo al hacer click en la notificacion
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

            String stringColor = "#b2ff66";
            int color = Integer.parseInt(stringColor.replaceFirst("^#", ""), 16);

            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //lo que el usuario tenga puesto para notifiacioes
            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.q_logo_sin_fondo)
                    .setContentTitle("Notificacion")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSound(sonido)
                    .setColor(color)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true); //que se pueda cancelar

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificacion.build()); //construye la notificacion que hemos creado arriba con un identificador (0
        }
    }

}
