package com.proyecto.quedemos.RestAPI;

/**
 * Created by MartaMillan on 10/7/16.
 */
public final class ConstantesRestAPI {
    public static final String ROOT_URL = "https://lit-sea-80485.herokuapp.com/";
    public static final String KEY_POST_ID_TOKEN = "token-device/";
    public static final String KEY_ACTUALIZAR_TOKEN = "token-device/{id}";
    public static final String KEY_FIND_FRIEND = "find-friends/{nombre}";
    public static final String KEY_FIND_SELF_USER = "find-self/{facebookid}";
    public static final String KEY_TOQUE_AMIGO = "toque-amigo/{id}/{nombre}";
    public static final String KEY_POST_QUEDADA = "quedadas/";
    public static final String KEY_PUSH_QUEDADA = "quedada-push/{id}/{nombre}/{idquedada}";
    //public static final String KEY_TOQUE_ANIMAL = "toque-animal/{id}/{animal}"; //enviar parametro sobre la url
}
