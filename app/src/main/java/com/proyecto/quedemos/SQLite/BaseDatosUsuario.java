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
            " nombre TEXT, hora_ini TEXT, hora_fin TEXT, dd TEXT, mm TEXT, yyyy TEXT)";

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

    public void nuevoEvento(String nombre, String horaIni, String horaFin, String[] fecha) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) { //si hay algo escrito
            ContentValues evento = new ContentValues();
            evento.put("nombre", nombre);
            evento.put("hora_ini",horaIni);
            evento.put("hora_fin",horaFin);
            evento.put("dd",fecha[0]);
            evento.put("mm",fecha[1]);
            evento.put("yyyy",fecha[2]);
            db.insert("eventos", null, evento);
        }
        db.close();
    }


    public ArrayList<String> getEventosMes(String mes){

        if (mes.length() == 1) {
            mes = "0" + mes;
        }

        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"dd"};
        Cursor c = db.query("eventos", FIELDS, "mm=?", new String[]{mes}, null, null, "dd ASC", null);
        c.moveToFirst();

        //ARRAY DE EVENTOS:
        ArrayList<String> eventosGuardados = new ArrayList<String>();

        if (db != null && c.getCount()>0) { //si hay eventos guardados

            do {
                eventosGuardados.add(c.getString(0));
                Log.e("EVENTO DIA: ", c.getString(0));
            }while(c.moveToNext());

            db.close();
            c.close();
        }

        return eventosGuardados;
    }

/*
    public void getEventoPorFecha(String fecha) {

        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"nombre", "hora_ini", "hora_fin", "fecha"};

        Cursor c = db.query("eventos", FIELDS, "fecha=?", new String[]{fecha}, null, null, null, null);
        c.moveToFirst();

        if (c.getCount()>0) { //si hay usuarios registrados

            do {
                Log.e("Nombre: ", c.getString(0) + " / " + c.getString(1) + " / " +  c.getString(2) + " / " + c.getString(3));

            }while(c.moveToNext());

            db.close();
            c.close();
        }
    }*/

}
