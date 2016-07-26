package com.proyecto.quedemos.RestAPI.model;

/**
 * Created by MartaMillan on 26/7/16.
 */
public class QuedadaResponse {

    private String id;
    private String nombre;
    private String fechaini;
    private String fechafin;
    private String horaini;
    private String horafin;
    private int solofinde;
    private String participantes;

    public QuedadaResponse (String id, String nombre, String fechaini, String fechafin, String horaini, String horafin, int solofinde, String participantes) {
        this.id = id;
        this.nombre = nombre;
        this.fechaini = fechaini;
        this.fechafin = fechafin;
        this.horaini = horaini;
        this.horafin = horafin;
        this.solofinde = solofinde;
        this.participantes = participantes;
    }

    public QuedadaResponse (){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaini() {
        return fechaini;
    }

    public void setFechaini(String fechaini) {
        this.fechaini = fechaini;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

    public String getHoraini() {
        return horaini;
    }

    public void setHoraini(String horaini) {
        this.horaini = horaini;
    }

    public String getHorafin() {
        return horafin;
    }

    public void setHorafin(String horafin) {
        this.horafin = horafin;
    }

    public int getSolofinde() {
        return solofinde;
    }

    public void setSolofinde(int solofinde) {
        this.solofinde = solofinde;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }
}
