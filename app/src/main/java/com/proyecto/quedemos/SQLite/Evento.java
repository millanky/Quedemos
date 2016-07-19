package com.proyecto.quedemos.SQLite;

/**
 * Created by MartaMillan on 18/7/16.
 */
public class Evento {

    private String evento;
    private String horaIni;
    private String horaFin;
    private String diaFecha;
    private String mesFecha;
    private String yearFecha;

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
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

}
