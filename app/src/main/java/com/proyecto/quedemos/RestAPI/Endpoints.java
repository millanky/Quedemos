package com.proyecto.quedemos.RestAPI;

import com.proyecto.quedemos.RestAPI.model.QuedadaResponse;
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
    Call<UsuarioResponse> registrarTokenID(@Field("token") String token, @Field("nombre") String nombre, @Field("urlimg") String urlimg, @Field("facebookid") String facebookid);

    @FormUrlEncoded
    @POST(ConstantesRestAPI.KEY_ACTUALIZAR_TOKEN)
    Call<UsuarioResponse> actualizarTokenID(@Path("id") String id, @Field("id") String id2, @Field("token") String token);

    @FormUrlEncoded
    @POST(ConstantesRestAPI.KEY_POST_QUEDADA)
    Call<QuedadaResponse> registrarQuedada(@Field("nombre") String nombre, @Field("fechaini") String fechaini, @Field("fechafin") String fechafin,
                                           @Field("horaini") String horaini, @Field("horafin") String horafin, @Field("solofinde") int solofinde,
                                           @Field("participantes") String participantes);

    @GET(ConstantesRestAPI.KEY_FIND_FRIEND)
    Call<UsuarioResponse> buscarAmigos (@Path("nombre") String nombre);

    @GET(ConstantesRestAPI.KEY_FIND_SELF_USER)
    Call<UsuarioResponse> buscarIdPropia (@Path("facebookid") String facebookid);

    @GET(ConstantesRestAPI.KEY_TOQUE_AMIGO)
    Call<UsuarioResponse> toqueAmigo (@Path("id") String id, @Path("nombre") String nombre);

    @GET(ConstantesRestAPI.KEY_FIND_QUEDADA)
    Call<QuedadaResponse> buscarQuedada (@Path("id") String id);

    @GET(ConstantesRestAPI.KEY_PUSH_QUEDADA)
    Call<UsuarioResponse> pushQuedada (@Path("id") String id, @Path("nombre") String nombre, @Path("idquedada") String idquedada);
}
