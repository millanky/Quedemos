package com.proyecto.quedemos.RestAPI.model;

/**
 * Created by MartaMillan on 10/7/16.
 */
public class UsuarioResponse {

    private  String id;
    private String token;
    private String animal;

    public UsuarioResponse(String token, String id, String animal) {
        this.token = token;
        this.id = id;
        this.animal = animal;
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

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }
}
