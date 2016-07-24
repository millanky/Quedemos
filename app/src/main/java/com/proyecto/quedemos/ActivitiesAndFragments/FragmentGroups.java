package com.proyecto.quedemos.ActivitiesAndFragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.ArrayAdapters.EventosAdapter;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.SQLite.BaseDatosUsuario;
import com.proyecto.quedemos.SQLite.Evento;

import java.util.ArrayList;


/**
 * Created by Usuario on 17/06/2016.
 */
public class FragmentGroups extends Fragment {

    private ArrayList<Evento> eventosList;
    private EventosAdapter eAdapter;
    private ListView eventosDiaList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
           View view = inflater.inflate(R.layout.fragment_groups, container, false);

        BaseDatosUsuario eventosBD = new BaseDatosUsuario(getContext());
        eventosDiaList = (ListView) view.findViewById(R.id.listEventosDia);
        String dia = "15-07-2016";
        ArrayList<Evento> eventosList = eventosBD.listadoEventosDia(dia.split("-"));
        eAdapter = new EventosAdapter(getContext(),R.layout.cell_eventos,eventosList);
        eventosDiaList.setAdapter(eAdapter);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new MaterialDialog.Builder(getContext())
                        .title("Hola")
                        .customView(R.layout.custom_view, true) //true indica con ScrollView
                        .positiveText("Aceptar")
                        .show();

            }
        });

        return  view;
    }
}