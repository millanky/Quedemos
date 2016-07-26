package com.proyecto.quedemos.SQLite;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by MartaMillan on 25/7/16.
 */
public class Quedada {

    private String nombre;
    private String fechaIni;
    private String fechaFin;
    private String horaIni;
    private String horaFin;
    private boolean soloFinde;
    private ArrayList<Amigo> participantes;
    private String idFirebaseQuedada;

    public Quedada (String nombre, String fechaIni, String fechaFin, String horaIni, String horaFin, boolean soloFinde, ArrayList<Amigo> participantes){
        this.nombre = nombre;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.soloFinde = soloFinde;
        this.participantes = participantes;
    }

    public Quedada(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public ArrayList<Amigo> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Amigo> participantes) {
        this.participantes = participantes;
    }

    public boolean isSoloFinde() {
        return soloFinde;
    }

    public void setSoloFinde(boolean soloFinde) {
        this.soloFinde = soloFinde;
    }

    public String getIdFirebaseQuedada() {
        return idFirebaseQuedada;
    }

    public void setIdFirebaseQuedada(String idFirebaseQuedada) {
        this.idFirebaseQuedada = idFirebaseQuedada;
    }
}
