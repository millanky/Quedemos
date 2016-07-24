package com.proyecto.quedemos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class Utils
{
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_GRIS = 1;
    public final static int THEME_ROJO = 2;
    public final static int THEME_NARAJA = 3;
    public final static int THEME_AZULCLARITO = 4;
    public final static int THEME_VERDE = 5;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(AppCompatActivity activity, int theme) {
        sTheme = theme;
        Intent intent = new Intent(activity, activity.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(AppCompatActivity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme);
                break;
            case THEME_GRIS:
                activity.setTheme(R.style.grisTheme);
                break;
            case THEME_ROJO:
                activity.setTheme(R.style.rojoTheme);
                break;
            case THEME_NARAJA:
                activity.setTheme(R.style.naranjaTheme);
                break;
            case THEME_AZULCLARITO:
                activity.setTheme(R.style.azulClaroTheme);
                break;
            case THEME_VERDE:
                activity.setTheme(R.style.verdeTheme);
                break;
        }
    }
}
