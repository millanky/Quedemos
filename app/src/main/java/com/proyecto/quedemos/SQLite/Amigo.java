package com.proyecto.quedemos.SQLite;

/**
 * Created by MartaMillan on 24/7/16.
 */
public class Amigo {

    private String nombre;
    private String urlimg;
    private String id;

    public Amigo (String nombre, String urlimg, String id){
        this.nombre = nombre;
        this.urlimg = urlimg;
        this.id = id;
    }

    public Amigo() {}

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
