package com.proyecto.quedemos.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.Calendar.EventosAdapter;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.RestAPI.Endpoints;
import com.proyecto.quedemos.RestAPI.adapter.RestApiAdapter;
import com.proyecto.quedemos.RestAPI.model.UsuarioResponse;
import com.proyecto.quedemos.SQLite.Evento;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MartaMillan on 23/7/16.
 */
public class AmigosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void buscarAmigos(View v){

        MaterialDialog dialogFind = new MaterialDialog.Builder(this)
                .title("Buscar amigo")
                .icon(getResources().getDrawable(R.drawable.grupos))
                .positiveText("Buscar")
                .input("Nombre", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        //Log.e("Input", input.toString());
                        findFriends(input.toString());
                    }
                })
                .negativeText("Cancelar")
                .build();

        dialogFind.show();

    }

    private void findFriends (String nombre) {

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCallFindFriends = endpoints.buscarAmigos(nombre);

        usuarioResponseCallFindFriends.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();
                Log.e("USUARIO",usuarioResponse.getNombre());
                Log.e("TOKEN",usuarioResponse.getToken());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }
}
