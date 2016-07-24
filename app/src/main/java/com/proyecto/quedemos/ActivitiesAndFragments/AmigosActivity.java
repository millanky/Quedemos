package com.proyecto.quedemos.ActivitiesAndFragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.ArrayAdapters.AmigosAdapter;
import com.proyecto.quedemos.ArrayAdapters.EventosAdapter;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.RestAPI.Endpoints;
import com.proyecto.quedemos.RestAPI.adapter.RestApiAdapter;
import com.proyecto.quedemos.RestAPI.model.UsuarioResponse;
import com.proyecto.quedemos.SQLite.Amigo;
import com.proyecto.quedemos.SQLite.BaseDatosUsuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MartaMillan on 23/7/16.
 */
public class AmigosActivity extends AppCompatActivity {

    private MaterialDialog dialogFind;
    private ListView listadoAmigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listadoAmigos = (ListView)findViewById(R.id.listadoAmigos);
        BaseDatosUsuario db = new BaseDatosUsuario(this);
        mostrarListadoAmigos(db.mostrarAmigos());
    }

    public void buscarAmigos(View v){

        dialogFind = new MaterialDialog.Builder(this)
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

                if (response.body() != null){ //Guardamos en la bdd de amigos
                    Log.e("USUARIO",usuarioResponse.getNombre());
                    Log.e("TOKEN",usuarioResponse.getToken());
                    BaseDatosUsuario db = new BaseDatosUsuario(getApplicationContext());

                    if (db.existeAmigo(usuarioResponse.getNombre())){
                        Toast.makeText(getApplicationContext(), "Amigo ya añadido.", Toast.LENGTH_LONG).show();
                        dialogFind.show();
                    } else {
                        db.nuevoAmigo(usuarioResponse.getToken(), usuarioResponse.getNombre(), usuarioResponse.getUrl_img());
                        mostrarListadoAmigos(db.mostrarAmigos());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario no encontrado.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Usuario no encontrado.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void mostrarListadoAmigos (ArrayList<Amigo> listaAmigos) {
        AmigosAdapter aAdapter = new AmigosAdapter(this,R.layout.cell_amigos,listaAmigos);
        listadoAmigos.setAdapter(aAdapter);
    }

}
