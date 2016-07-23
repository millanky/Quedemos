package com.proyecto.quedemos.RestAPI.model;

/**
 * Created by MartaMillan on 10/7/16.
 */
public class UsuarioResponse {

    private  String id;
    private String token;
    private String nombre;
    private String urlimg;

    public UsuarioResponse(String token, String id, String nombre) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
    }

    public UsuarioResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public String getUrl_img() {
        return urlimg;
    }

    public void setUrl_img(String url_img) {
        this.urlimg = url_img;
    }
}
