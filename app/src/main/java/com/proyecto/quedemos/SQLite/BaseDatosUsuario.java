package com.proyecto.quedemos.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by MartaMillan on 18/7/16.
 */

public class BaseDatosUsuario extends SQLiteOpenHelper {

    private static final String nombre = "eventos.db";

    private static final int version = 1;

    private static final String tabla = "CREATE TABLE eventos (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nombre TEXT, hora_ini TEXT, hora_fin TEXT, fecha TEXT)";

    //CONSTRUCTOR
    public BaseDatosUsuario (Context context) {
        super(context, nombre, null, version);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tabla);
    }

    public void onUpgrade(SQLiteDatabase db, int Oversion, int Nversion) {
        db.execSQL("DROP TABLE IF EXISTS" + tabla); //tiramos la tabla y la volvemos a crear
        onCreate(db);
    }

    public void nuevoEvento(String nombre, String horaIni, String horaFin, String fecha) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) { //si hay algo escrito
            ContentValues evento = new ContentValues();
            evento.put("nombre", nombre);
            evento.put("hora_ini",horaIni);
            evento.put("hora_fin",horaFin);
            evento.put("fecha",fecha);
            db.insert("eventos", null, evento);
        }
        db.close();
    }

    /*
    public ArrayList<Evento> getEventosMes(String mes){

        SQLiteDatabase db = getReadableDatabase();
        String[] valores = {"nombre", "hora_ini", "hora_fin", "fecha"};
        Cursor c
        c.moveToFirst();

        //ARRAY DE EVENTOS:
        ArrayList<Evento> = new A

        if (c.getCount()>0) { //si hay usuarios registrados

            do {
                Log.e("Nombre: ", c.getString(0) + " / " + c.getString(1) + " / " +  c.getString(2) + " / " + c.getString(3));

            }while(c.moveToNext());

            db.close();
            c.close();
        }


    }*/


    public void getEventoPorFecha(String fecha) {

        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"nombre", "hora_ini", "hora_fin", "fecha"};

        Cursor c = db.query("eventos", FIELDS, "fecha=?", new String[]{fecha}, null, null, null, null);
        c.moveToFirst();

        if (db!=null & c.getCount()>0) { //si hay usuarios registrados

            do {
                Log.e("Nombre: ", c.getString(0) + " / " + c.getString(1) + " / " +  c.getString(2) + " / " + c.getString(3));

            }while(c.moveToNext());

            db.close();
            c.close();
        }
    }

}
