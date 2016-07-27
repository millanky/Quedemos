package com.proyecto.quedemos.ActivitiesAndFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.ArrayAdapters.AmigosAdapter;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.RestAPI.Endpoints;
import com.proyecto.quedemos.RestAPI.adapter.RestApiAdapter;
import com.proyecto.quedemos.RestAPI.model.UsuarioResponse;
import com.proyecto.quedemos.SQLite.Amigo;
import com.proyecto.quedemos.SQLite.BaseDatosUsuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usuario on 17/06/2016.
 */

public class FragmentAmigos extends Fragment {

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        return view;
    }*/
    private MaterialDialog dialogFind;
    private MaterialDialog dialogBuscando;
    private ListView listadoAmigos;
    private AmigosAdapter aAdapter;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);
        prefs = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        buscando(); //Crear ventana modal

        listadoAmigos = (ListView)view.findViewById(R.id.listadoAmigos);
        BaseDatosUsuario db = new BaseDatosUsuario(getContext());
        mostrarListadoAmigos(db.mostrarAmigos());

        listadoAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,long id) {
                String idReceptor = aAdapter.getItem(pos).getId();
                String receptor = aAdapter.getItem(pos).getNombre();
                String emisor = prefs.getString("user","Un usuario");
                darUnToque(idReceptor, emisor, receptor);
            }
        });

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.buttonAddFriends);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               buscarAmigos();

            }
        });

        return view;
    }

    //****************** ADAPTER - LISTAR AMIGOS ALFABÉTICAMENTE ****************//

    public void mostrarListadoAmigos (ArrayList<Amigo> listaAmigos) {
        Collections.sort(listaAmigos, new ComparadorAmigos());
        aAdapter = new AmigosAdapter(getContext(),R.layout.cell_amigos,listaAmigos);
        listadoAmigos.setAdapter(aAdapter);
    }
    class ComparadorAmigos implements Comparator<Amigo> {
        @Override
        public int compare(Amigo a1, Amigo a2) {
            return a1.getNombre().compareTo(a2.getNombre());
        }
    }

    //************************** MODAL VIEWS ****************************//

    public void buscarAmigos(){

        dialogFind = new MaterialDialog.Builder(getContext())
                .title("Buscar amigo")
                .icon(getResources().getDrawable(R.drawable.grupos))
                .positiveText("Buscar")
                .input("Nombre", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        findFriends(input.toString());
                        dialogBuscando.show();
                    }
                })
                .negativeText("Cancelar")
                .build();

        dialogFind.show();
    }

    public void darUnToque(String idReceptor, String emisor, String receptor) {
        final String emisorToque = emisor;
        final String idPush = idReceptor;
        MaterialDialog dialogToque = new MaterialDialog.Builder(getContext())
                .title("Dar un toque a "+receptor)
                .icon(getResources().getDrawable(R.drawable.q_logo_sin_fondo))
                .positiveText("Aceptar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        toqueAmigo(idPush, emisorToque);
                    }
                })
                .negativeText("Cancelar")
                .build();

        dialogToque.show();
    }

    public void buscando(){

        dialogBuscando = new MaterialDialog.Builder(getContext())
                .title("Buscando amigo")
                .content("Esta acción puede tardar unos segundos")
                .progress(true, 0)
                .build();
    }

    //********************* REST - BUSCAR AMIGO EN FIREBASE *****************//

    private void findFriends (String nombre) {

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCallFindFriends = endpoints.buscarAmigos(nombre);

        usuarioResponseCallFindFriends.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();

                if (response.body() != null){ //Guardamos en la bdd de amigos
                    dialogBuscando.dismiss();
                    Log.e("USUARIO",usuarioResponse.getNombre());
                    Log.e("ID",usuarioResponse.getId());
                    BaseDatosUsuario db = new BaseDatosUsuario(getContext());

                    if (db.existeAmigo(usuarioResponse.getNombre())){
                        Toast.makeText(getContext(), "Amigo ya añadido.", Toast.LENGTH_LONG).show();
                        dialogFind.show();
                    } else {
                        db.nuevoAmigo(usuarioResponse.getId(), usuarioResponse.getNombre(), usuarioResponse.getUrl_img());
                        mostrarListadoAmigos(db.mostrarAmigos());
                    }
                } else {
                    dialogBuscando.dismiss();
                    dialogFind.show();
                    Toast.makeText(getContext(), "Usuario no encontrado.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                dialogBuscando.dismiss();
                dialogFind.show();
                Toast.makeText(getContext(), "Usuario no encontrado.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    //********************** TOQUE PUSH NOTIFICATION - FIREBASE CLOUD MESSAGING **************************//

    public void toqueAmigo(String idReceptor, String emisor) {

        String idReceptorPush = idReceptor;
        String emisorPush = emisor;

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCall = endpoints.toqueAmigo(idReceptorPush, emisorPush);

        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.body() != null) {
                    UsuarioResponse usuarioResponse1 = response.body();
                    Log.e("TOQUE ENVIADO A", usuarioResponse1.getNombre());
                    Toast.makeText(getContext(), "Toque enviado a "+usuarioResponse1.getNombre(),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Ha habido un problema con el servidor, inténtalo más tarde",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Ha habido un problema con el servidor, inténtalo más tarde",
                        Toast.LENGTH_LONG).show();
            }
        });
    }




}
