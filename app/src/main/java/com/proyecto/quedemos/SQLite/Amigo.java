package com.proyecto.quedemos.SQLite;

/**
 * Created by MartaMillan on 24/7/16.
 */
public class Amigo {

    private String nombre;
    private String urlimg;
    private String token;

    public Amigo (String nombre, String urlimg, String token){
        this.nombre = nombre;
        this.urlimg = urlimg;
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
