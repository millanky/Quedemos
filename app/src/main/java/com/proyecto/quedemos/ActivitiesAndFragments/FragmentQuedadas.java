package com.proyecto.quedemos.ActivitiesAndFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.proyecto.quedemos.ArrayAdapters.AmigosAdapter;
import com.proyecto.quedemos.ArrayAdapters.EventosAdapter;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.SQLite.Amigo;
import com.proyecto.quedemos.SQLite.BaseDatosUsuario;
import com.proyecto.quedemos.SQLite.Evento;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Usuario on 17/06/2016.
 */
public class FragmentQuedadas extends Fragment {

    private ArrayList<Evento> eventosList;
    private EventosAdapter eAdapter;
    private ListView eventosDiaList;
    private View positiveAction;
    private EditText fechaIni;
    private EditText fechaFin;
    private CheckBox soloFinde;
    private EditText nombreQuedada;
    private String nombreQ;
    private String horaIni;
    private String horaFin;
    private String fechaInicio;
    private String fechaFinal;
    private int soloFindes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
           View view = inflater.inflate(R.layout.fragment_quedada, container, false);

        //MOSTRAR LISTADO QUEDADAS
        /*
        BaseDatosUsuario eventosBD = new BaseDatosUsuario(getContext());
        eventosDiaList = (ListView) view.findViewById(R.id.listEventosDia);
        String dia = "15-07-2016";
        ArrayList<Evento> eventosList = eventosBD.listadoEventosDia(dia.split("-"));
        eAdapter = new EventosAdapter(getContext(),R.layout.cell_eventos,eventosList);
        eventosDiaList.setAdapter(eAdapter);*/


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              nuevaQuedada();
            }

        });

        return  view;
    }

    //----------------------- MODAL VIEW * NUEVA QUEDADA -----------------------

    public void nuevaQuedada() {

        //valores iniciales por defecto
        horaIni = "09:00";
        horaFin = "11:00";

        MaterialDialog newQuedada = new MaterialDialog.Builder(getContext())
                .title("Nueva quedada")
                .customView(R.layout.modal_insertar_quedada, true) //true indica con ScrollView
                .positiveText("Guardar")
                .icon(getResources().getDrawable(R.drawable.calendario))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        nombreQ = nombreQuedada.getText().toString();
                        fechaInicio = fechaIni.getText().toString();
                        fechaFinal = fechaFin.getText().toString();

                        soloFindes = 0;
                        if (soloFinde.isChecked()){
                            soloFindes = 1;
                        } else { soloFindes = 0; }

                       addAmigosQuedada();
                    }
                })
                .negativeText("Cancelar")
                .build();

        positiveAction = newQuedada.getActionButton(DialogAction.POSITIVE);
        nombreQuedada = (EditText) newQuedada.getCustomView().findViewById(R.id.nombre);
        fechaIni = (EditText) newQuedada.getCustomView().findViewById(R.id.inputFechaIni);
        fechaFin = (EditText) newQuedada.getCustomView().findViewById(R.id.inputFechaIni);
        soloFinde = (CheckBox) newQuedada.getCustomView().findViewById(R.id.checkFinde);

        nombreQuedada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                positiveAction.setEnabled(nombreQuedada.getText().toString().length()>0
                        && fechaIni.getText().toString().length()>0
                        && fechaFin.getText().toString().length()>0);
            }
            @Override
            public void afterTextChanged(Editable editable) {  }
        });
        fechaIni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                positiveAction.setEnabled(nombreQuedada.getText().toString().length()>0
                        && fechaIni.getText().toString().length()>0
                        && fechaFin.getText().toString().length()>0);
            }
            @Override
            public void afterTextChanged(Editable editable) {  }
        });
        fechaFin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                positiveAction.setEnabled(nombreQuedada.getText().toString().length()>0
                        && fechaIni.getText().toString().length()>0
                        && fechaFin.getText().toString().length()>0);
            }
            @Override
            public void afterTextChanged(Editable editable) {  }
        });

        RadioGroup radioQuedada = (RadioGroup) newQuedada.getCustomView().findViewById(R.id.radioHorario);
        radioQuedada.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.timeDesayuno) {
                    horaIni = "09:00";
                    horaFin = "11:00";
                } else if (i == R.id.timeAlmuerzo) {
                    horaIni = "11:00";
                    horaFin = "13:00";
                } else if (i == R.id.timeComida) {
                    horaIni = "13:00";
                    horaFin = "16:00";
                } else if (i == R.id.timeCafe) {
                    horaIni = "16:00";
                    horaFin = "18:00";
                } else if (i == R.id.timeCopas) {
                    horaIni = "18:00";
                    horaFin = "20:00";
                } else if (i == R.id.timeCena) {
                    horaIni = "20:00";
                    horaFin = "24:00";
                }
            }
        });


        newQuedada.show();
        positiveAction.setEnabled(false);
    }

    //----------------------- MODAL VIEW * ADD FRIENDS -----------------------

    public void addAmigosQuedada() {

        final BaseDatosUsuario BD = new BaseDatosUsuario(getContext());

        MaterialDialog addFriendsToQuedada = new MaterialDialog.Builder(getContext())
                .title("Participantes")
                .customView(R.layout.modal_add_friends_to_quedada, false) //true indica con ScrollView
                .positiveText("Terminar")
                .icon(getResources().getDrawable(R.drawable.grupos))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        addAmigosQuedada();
                    }
                })
                .build();

        ArrayList<Amigo> friendsList = BD.mostrarAmigos();
        ListView friendsListView = (ListView) addFriendsToQuedada.getCustomView().findViewById(R.id.listadoAmigosAdded);
        AmigosAdapter amigosAdapter = new AmigosAdapter(getContext(), R.layout.cell_amigos, friendsList);
        friendsListView.setAdapter(amigosAdapter);

        addFriendsToQuedada.show();
        positiveAction.setEnabled(false);
    }
}