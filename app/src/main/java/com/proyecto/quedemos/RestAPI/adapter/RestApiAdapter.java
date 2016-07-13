package com.proyecto.quedemos.RestAPI.adapter;

import com.proyecto.quedemos.RestAPI.ConstantesRestAPI;
import com.proyecto.quedemos.RestAPI.Endpoints;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MartaMillan on 10/7/16.
 */
public class RestApiAdapter {

    public Endpoints establecerConexionRestAPI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestAPI.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return  retrofit.create(Endpoints.class);
    }
}
