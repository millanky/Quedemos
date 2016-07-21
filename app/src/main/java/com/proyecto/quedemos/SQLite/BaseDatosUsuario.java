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
        if (day.length() == 1) {day =  "0" + day;}
        if (month.length() == 1) {month = "0" + month;}
        Cursor c = db.query("eventos", FIELDS, "dd=? AND mm=? AND yyyy=?", new String[]{day, month, cyear}, null, null, null, null);
        c.moveToFirst();
        int count = c.getCount();

        db.close();c.close();

        return count;
    }

    public ArrayList<Evento> listadoEventosDia (String[] fecha) {
        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"nombre","hora_ini","hora_fin"};
        String day = fecha[0];
        String month = fecha[1];
        String year = fecha[2];

        ArrayList<Evento> listadoEventos = new ArrayList<Evento>();

        Cursor c = db.query("eventos", FIELDS, "dd=? AND mm=? AND yyyy=?", new String[]{day, month, year}, null, null, null, null);
        c.moveToFirst();
        if (db != null && c.getCount()>0) {
            do {
                Evento e = new Evento(c.getString(0), c.getString(1), c.getString(2), day, month, year);
                listadoEventos.add(e);
            } while (c.moveToNext());
        }
        return listadoEventos;
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

    /************** MÉTODOS COMPARACIÓN HORAS ****************/

    private boolean compareDates(String horaCompIni, String horaCompFin, String horaIni, String horaFin){

        Date horaCIni = parseDate(horaCompIni);
        Date horaCFin = parseDate(horaCompFin);
        Date inicio = parseDate(horaIni);
        Date fin = parseDate(horaFin);

        boolean conflicto = false;

        if ( inicio.before( horaCIni ) && fin.after(horaCIni)) {
            conflicto = true;
        } else if (inicio.before( horaCFin ) && fin.after( horaCFin )){
            conflicto = true;
        } else if (horaCIni.after( inicio ) && horaCFin.before( fin )) {
            conflicto = true;
        } else if (horaCIni.before( inicio ) && horaCFin.after( fin )){
            conflicto = true;
        } else if (horaCIni.equals( inicio ) && horaCFin.equals( fin )) {
            conflicto = true;
        } else if (horaCIni.equals( inicio ) && horaCFin.after( fin )) {
            conflicto = true;
        } else if (horaCIni.before( inicio ) && horaCFin.equals( fin )) {
            conflicto = true;
        }
        return conflicto;
    }

    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

}
