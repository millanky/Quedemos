package com.proyecto.quedemos.RestAPI;

import com.proyecto.quedemos.RestAPI.model.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by MartaMillan on 10/7/16.
 */
public interface Endpoints {

    @FormUrlEncoded
    @POST(ConstantesRestAPI.KEY_POST_ID_TOKEN)
    Call<UsuarioResponse> registrarTokenID(@Field("token") String token, @Field("animal") String animal);
    //Indico el ANIMAL que REGISTRO

    @GET(ConstantesRestAPI.KEY_TOQUE_ANIMAL)
    Call<UsuarioResponse> toqueAnimal(@Path("id") String id, @Path("animal") String animal);

}
