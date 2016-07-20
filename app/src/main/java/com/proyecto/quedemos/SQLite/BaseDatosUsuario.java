package com.proyecto.quedemos.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by MartaMillan on 18/7/16.
 */

public class BaseDatosUsuario extends SQLiteOpenHelper {

    private static final String nombre = "eventos.db";

    private static final int version = 1;

    private static final String tabla = "CREATE TABLE eventos (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nombre TEXT, hora_ini TEXT, hora_fin TEXT, dd TEXT, mm TEXT, yyyy TEXT, quedada INTEGER)";

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

    public void nuevoEvento(String nombre, String horaIni, String horaFin, String[] fecha, int quedada) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) { //si hay algo escrito
            ContentValues evento = new ContentValues();
            evento.put("nombre", nombre);
            evento.put("hora_ini",horaIni);
            evento.put("hora_fin",horaFin);
            evento.put("dd",fecha[0]);
            evento.put("mm",fecha[1]);
            evento.put("yyyy",fecha[2]);
            evento.put("quedada", quedada);
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

        if (db != null && c.getCount()>0) {

            do {
                eventosGuardados.add(c.getString(0));
            }while(c.moveToNext());

            db.close();
            c.close();
        }

        return eventosGuardados;
    }

    public int nEventosDia (int dia, int mes, int year) {

        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"dd"};
        String day = String.valueOf(dia);
        String month = String.valueOf(mes);
        String cyear = String.valueOf(year);
        if (day.length() == 1) {
            day =  "0" + day;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }
        Cursor c = db.query("eventos", FIELDS, "dd=? AND mm=? AND yyyy=?", new String[]{day, month, cyear}, null, null, null, null);
        c.moveToFirst();

        int count = c.getCount();

        db.close();
        c.close();

        return count;
    }

    public boolean existeEvento (String[] fecha, String horaIni, String horaFin) {

        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"hora_ini","hora_fin"};
        Cursor c = db.query("eventos", FIELDS, "dd=?", new String[]{fecha[0]}, null, null, null, null);
        c.moveToFirst();

        boolean eventoExist = false;

        if (db != null && c.getCount()>0) {

            do {

                if (compareDates(horaIni, horaFin, c.getString(0),c.getString(1))) {
                    eventoExist = true;
                }

            }while(c.moveToNext());

            db.close();
            c.close();
        }

        return eventoExist;
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

    public static final String inputFormat = "HH:mm";

    private Date horaCIni;
    private Date horaCFin;
    private Date inicio;
    private Date fin;

    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    private boolean compareDates(String horaCompIni, String horaCompFin, String horaIni, String horaFin){

        horaCIni = parseDate(horaCompIni);
        horaCFin = parseDate(horaCompFin);
        inicio = parseDate(horaIni);
        fin = parseDate(horaFin);

        boolean entreAmbos = false;

        if ( inicio.before( horaCIni ) && fin.after(horaCIni)) {
            entreAmbos = true;
        } else if (inicio.before( horaCFin ) && fin.after(horaCFin)){
            entreAmbos = true;
        } else if (horaCIni.before( inicio ) && fin.after(horaCIni)) {
            entreAmbos = true;
        }
        return entreAmbos;
    }

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

}
