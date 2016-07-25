package com.proyecto.quedemos.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

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

    private static final String nombre = "user.db";

    private static final int version = 1;

    private static final String tablaEventos = "CREATE TABLE eventos (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nombre TEXT, hora_ini TEXT, hora_fin TEXT, dd TEXT, mm TEXT, yyyy TEXT, quedada INTEGER)";
    private static final String tablaAmigos = "CREATE TABLE amigos (" +
            " nombre TEXT, id TEXT, urlimg TEXT)";
    private static final String tablaQuedadas = "CREATE TABLE quedadas (" +
            " nombre TEXT, fecha_ini TEXT, fecha_fin TEXT, hora_ini TEXT, hora_fin TEXT, solo_finde INTEGER, participantes TEXT)";

    //CONSTRUCTOR
    public BaseDatosUsuario (Context context) {
        super(context, nombre, null, version);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tablaEventos);
        db.execSQL(tablaAmigos);
        db.execSQL(tablaQuedadas);
    }

    public void onUpgrade(SQLiteDatabase db, int Oversion, int Nversion) {
        db.execSQL("DROP TABLE IF EXISTS" + tablaEventos); //tiramos la tabla y la volvemos a crear
        db.execSQL("DROP TABLE IF EXISTS" + tablaAmigos);
        db.execSQL("DROP TABLE IF EXISTS" + tablaQuedadas);
        onCreate(db);
    }

    //----------------------------- A M I G O S --------------------------------------//

    public void nuevoAmigo (String id, String nombre, String urlimg) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues amigo = new ContentValues();
            amigo.put("nombre", nombre);
            amigo.put("urlimg", urlimg);
            amigo.put("id", id);
            db.insert("amigos", null, amigo);
        }
        db.close();
    }

    public boolean existeAmigo (String nombre) {
        boolean existe = false;
        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"nombre"};
        Cursor c = db.query("amigos", FIELDS, "nombre=?", new String[]{nombre}, null, null, null, null);
        c.moveToFirst();

        if (c.getCount() > 0) {

            existe = true;
        }
        c.close();db.close();

        return existe;
    }

    public Amigo datosAmigo (String nombre) {
        Amigo a = new Amigo();
        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"nombre","id", "urlimg"};
        Cursor c = db.query("amigos", FIELDS, "nombre=?", new String[]{nombre}, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0){
            a.setNombre(c.getString(0));
            a.setId(c.getString(1));
            a.setUrlimg(c.getString(2));
        }
        c.close();db.close();
        return a;
    }

    public ArrayList<Amigo> mostrarAmigos () {
        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"nombre","urlimg","id"};

        ArrayList<Amigo> listadoAmigos = new ArrayList<Amigo>();

        Cursor c = db.query("amigos", FIELDS, null, null, null, null, null, null);
        c.moveToFirst();

        if (db != null && c.getCount()>0) {
            do {
                Amigo a = new Amigo(c.getString(0), c.getString(1), c.getString(2));
                listadoAmigos.add(a);
            } while (c.moveToNext());
        }
        c.close();db.close();

        return listadoAmigos;
    }

    //--------------------------- Q U E D A D A S ------------------------------------//

    public void nuevaQuedada (String nombre, String fechaIni, String fechaFin, String horaIni, String horafin, int soloFinde,  ArrayList<Amigo> participantes) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues quedada = new ContentValues();
            quedada.put("nombre",nombre);
            quedada.put("fecha_ini",fechaIni);
            quedada.put("fecha_fin",fechaFin);
            quedada.put("hora_ini",horaIni);
            quedada.put("hora_fin",horafin);
            quedada.put("solo_finde",soloFinde);

            Gson gson = new Gson();
            String participantesString= gson.toJson(participantes);
            quedada.put("participantes",participantesString);

            db.insert("quedadas",null,quedada);
        }
        db.close();
    }

    public ArrayList<Quedada> mostrarQuedadas () {
        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"nombre","fecha_ini","fecha_fin","hora_ini","hora_fin","solo_finde","participantes"};

        ArrayList<Quedada> listadoQuedadas = new ArrayList<Quedada>();

        Cursor c = db.query("quedadas", FIELDS, null, null, null, null, null, null);
        c.moveToFirst();

        if (db != null && c.getCount()>0) {
            do {
                //Quedada q = new Quedada(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getString(6));
              //  listadoAmigos.add(a);
            } while (c.moveToNext());
        }
        c.close();db.close();

        return listadoQuedadas;
    }

    //--------------------------- E V E N T O S --------------------------------------//

    public void nuevoEvento(String nombre, String horaIni, String horaFin, String[] fecha, int quedada) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
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
