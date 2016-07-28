package com.proyecto.quedemos.ActivitiesAndFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.gson.Gson;
import com.proyecto.quedemos.ArrayAdapters.AmigosAdapter;
import com.proyecto.quedemos.ArrayAdapters.EventosAdapter;
import com.proyecto.quedemos.ArrayAdapters.QuedadasAdapter;
import com.proyecto.quedemos.Calendar.CaldroidCustomFragment;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.RestAPI.Endpoints;
import com.proyecto.quedemos.RestAPI.adapter.RestApiAdapter;
import com.proyecto.quedemos.RestAPI.model.QuedadaResponse;
import com.proyecto.quedemos.RestAPI.model.UsuarioResponse;
import com.proyecto.quedemos.SQLite.Amigo;
import com.proyecto.quedemos.SQLite.BaseDatosUsuario;
import com.proyecto.quedemos.SQLite.Evento;
import com.proyecto.quedemos.SQLite.Quedada;
import com.roomorama.caldroid.CaldroidFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Usuario on 17/06/2016.
 */
public class FragmentQuedadas extends Fragment {

    private ListView quedadasList;
    private LinearLayout fragmentCalendar;
    private FloatingActionButton addQ;
    private FloatingActionButton back;
    private Button verParticipantes;
    private QuedadasAdapter qAdapter;
    private View positiveAction;
    private EditText fechaIni;
    private EditText fechaFin;
    private CheckBox soloFinde;
    private EditText nombreQuedada;
    private TextView tituloQuedada;
    private String nombreQ;
    private String horaIni;
    private String horaFin;
    private String fechaInicio;
    private String fechaFinal;
    private int soloFindes;
    private ArrayList<Amigo> amigosQuedada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_quedada, container, false);
        quedadasList = (ListView) view.findViewById(R.id.listQuedadas);
        addQ = (FloatingActionButton) view.findViewById(R.id.addQuedada);
        back = (FloatingActionButton) view.findViewById(R.id.back);
        fragmentCalendar = (LinearLayout) view.findViewById(R.id.quedadaCalendar);
        tituloQuedada = (TextView) view.findViewById(R.id.tituloQuedada);
        verParticipantes = (Button) view.findViewById(R.id.btnVerParticipantes);

        //MOSTRAR LISTADO QUEDADAS
        BaseDatosUsuario BD = new BaseDatosUsuario(getContext());
        mostrarListadoQuedadas(BD.mostrarQuedadas());

        //AÑADIR QUEDADA
        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              nuevaQuedada();
            }

        });

        //VISTA DE LISTADO
        fragmentCalendar.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        tituloQuedada.setVisibility(View.GONE);
        verParticipantes.setVisibility(View.GONE);
        addQ.setVisibility(View.VISIBLE);
        quedadasList.setVisibility(View.VISIBLE);

        //VER QUEDADA
        quedadasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int pos,long id) {

                tituloQuedada.setText(qAdapter.getItem(pos).getNombre());

                back.setVisibility(View.VISIBLE);
                tituloQuedada.setVisibility(View.VISIBLE);
                verParticipantes.setVisibility(View.VISIBLE);
                addQ.setVisibility(View.GONE);
                quedadasList.setVisibility(View.GONE);

                customizarCalendarioQuedada(savedInstanceState, qAdapter.getItem(pos));

                //VER PARTICIPANTES
                verParticipantes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mostrarParticipantes(qAdapter.getItem(pos).getParticipantes());
                    }
                });

                //VOLVER AL LISTADO
                back.setOnClickListener(new View.OnClickListener() { //VOLVER A VISTA DE LISTADO
                    @Override
                    public void onClick(View view) {
                        fragmentCalendar.setVisibility(View.GONE);
                        back.setVisibility(View.GONE);
                        tituloQuedada.setVisibility(View.GONE);
                        verParticipantes.setVisibility(View.GONE);
                        addQ.setVisibility(View.VISIBLE);
                        quedadasList.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
        return  view;
    }

    @Override
    public void setMenuVisibility(final boolean visible) { //fragmento seleccionado en el viewpager
        super.setMenuVisibility(visible);
        if (visible) {
            Log.e("QUEDADAS","VISIBLE"); //Vuelvo al listado
            fragmentCalendar.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            tituloQuedada.setVisibility(View.GONE);
            verParticipantes.setVisibility(View.GONE);
            addQ.setVisibility(View.VISIBLE);
            quedadasList.setVisibility(View.VISIBLE);

            //Actualizo por si ha habido cambios
            BaseDatosUsuario BD = new BaseDatosUsuario(getActivity().getApplicationContext());
            mostrarListadoQuedadas(BD.mostrarQuedadas());
        }
    }

    //----------------------- ADAPTER * LISTAR QUEDADAS -----------------------

    public void mostrarListadoQuedadas (ArrayList<Quedada> listaQuedadas) {

        //Orden de la última añadida a la primera
        ArrayList<Quedada> quedadasOrdenadas = new ArrayList<>();
        ListIterator<Quedada> listIter = listaQuedadas.listIterator(listaQuedadas.size());
        while (listIter.hasPrevious()) {
            quedadasOrdenadas.add(listIter.previous());
        }

        qAdapter = new QuedadasAdapter(getContext(),R.layout.cell_quedada,quedadasOrdenadas);
        quedadasList.setAdapter(qAdapter);
    }

    //----------------------- MODAL VIEW * NUEVA QUEDADA -----------------------

    public void nuevaQuedada() {

        //valores iniciales por defecto
        horaIni = "09:00";
        horaFin = "11:00";
        amigosQuedada = new ArrayList<Amigo>();
        final String dateFormat = "dd/MM/yyyy";

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

                       addAmigosQuedada(new Amigo());
                    }
                })
                .negativeText("Cancelar")
                .build();

        positiveAction = newQuedada.getActionButton(DialogAction.POSITIVE);
        nombreQuedada = (EditText) newQuedada.getCustomView().findViewById(R.id.nombre);
        fechaIni = (EditText) newQuedada.getCustomView().findViewById(R.id.inputFechaIni);
        fechaFin = (EditText) newQuedada.getCustomView().findViewById(R.id.inputFechaFin);
        soloFinde = (CheckBox) newQuedada.getCustomView().findViewById(R.id.checkFinde);

        nombreQuedada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                positiveAction.setEnabled(nombreQuedada.getText().toString().length()>0
                        && validDateFormat(dateFormat,fechaIni.getText().toString())
                        && validDateFormat(dateFormat,fechaFin.getText().toString()));
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
                        && validDateFormat(dateFormat,fechaIni.getText().toString())
                        && validDateFormat(dateFormat,fechaFin.getText().toString()));
                       // && fechaIni.getText().toString().length()>0
                        //&& fechaFin.getText().toString().length()>0);
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
                        && validDateFormat(dateFormat,fechaIni.getText().toString())
                        && validDateFormat(dateFormat,fechaFin.getText().toString()));
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

    public void addAmigosQuedada(Amigo amigo) {

        final BaseDatosUsuario BD = new BaseDatosUsuario(getContext());
        if (amigo.getNombre() != null) {
            amigosQuedada.add(amigo);
        }

        final MaterialDialog addFriendsToQuedada = new MaterialDialog.Builder(getContext())
                .title("Participantes")
                .customView(R.layout.modal_add_friends_to_quedada, false) //true indica con ScrollView
                .positiveText("Finalizar")
                .negativeText("Cancelar")
                .icon(getResources().getDrawable(R.drawable.grupos))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(amigosQuedada.size() == 0){
                            Toast.makeText(getContext(), "No se han añadido amigos", Toast.LENGTH_SHORT).show();
                            addAmigosQuedada(new Amigo());
                        } else {
                            //Añado al propio usuario a la quedada:
                            SharedPreferences prefs = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
                            Amigo usuario = new Amigo(prefs.getString("user",""), prefs.getString("picture",""), prefs.getString("databaseID",""));
                            amigosQuedada.add(usuario);

                            long idSQL = BD.nuevaQuedada(nombreQ,fechaInicio,fechaFinal,horaIni,horaFin,soloFindes,amigosQuedada,"");
                            postQuedadaFirebase(idSQL);

                            mostrarListadoQuedadas(BD.mostrarQuedadas());
                        }

                    }
                })
                .build();


        ListView friendsListView = (ListView) addFriendsToQuedada.getCustomView().findViewById(R.id.listadoAmigosAdded);
        AmigosAdapter amigosAdapter = new AmigosAdapter(getContext(), R.layout.cell_amigos, amigosQuedada);
        friendsListView.setAdapter(amigosAdapter);

        Button addFriendsBtn = (Button) addFriendsToQuedada.getCustomView().findViewById(R.id.buttonAddFriends);
        addFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarAmigos();
                addFriendsToQuedada.dismiss();
            }
        });

        addFriendsToQuedada.show();
    }

    //----------------------- MODAL VIEW * MOSTRAR AMIGOS -----------------------

    public void mostrarAmigos() {
        final BaseDatosUsuario BD = new BaseDatosUsuario(getContext());

        final MaterialDialog showFriendsForQuedada = new MaterialDialog.Builder(getContext())
                .title("Añade un amigo")
                .customView(R.layout.modal_ver_amigos, false) //true indica con ScrollView
                .icon(getResources().getDrawable(R.drawable.grupos))
                .negativeText("Atrás")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addAmigosQuedada(new Amigo());
                    }
                })
                .build();

        ArrayList<Amigo> friendsList = BD.mostrarAmigos();
        Collections.sort(friendsList, new ComparadorAmigos());
        ListView friendsListView = (ListView) showFriendsForQuedada.getCustomView().findViewById(R.id.verAmigos);
        final AmigosAdapter amigosAdapter = new AmigosAdapter(getContext(), R.layout.cell_amigos, friendsList);
        friendsListView.setAdapter(amigosAdapter);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,long id) {
                String nombreUsuario = amigosAdapter.getItem(pos).getNombre();
                String imgUsuario = amigosAdapter.getItem(pos).getUrlimg();
                String idUsuario = amigosAdapter.getItem(pos).getId();
                Amigo amigo = new Amigo(nombreUsuario,imgUsuario,idUsuario);

                addAmigosQuedada(amigo);
                showFriendsForQuedada.dismiss();
            }
        });

        showFriendsForQuedada.show();
    }

    class ComparadorAmigos implements Comparator<Amigo> {
        @Override
        public int compare(Amigo a1, Amigo a2) {
            return a1.getNombre().compareTo(a2.getNombre());
        }
    }

    //----------------------- MODAL VIEW * MOSTRAR PARTICIPANTES -----------------------

    public void mostrarParticipantes(ArrayList<Amigo> participantesList) {

        final MaterialDialog showParticipants = new MaterialDialog.Builder(getContext())
                .title("Participantes")
                .customView(R.layout.modal_ver_amigos, false) //true indica con ScrollView
                .icon(getResources().getDrawable(R.drawable.grupos))
                .positiveText("Atrás")
                .build();

        ListView friendsListView = (ListView) showParticipants.getCustomView().findViewById(R.id.verAmigos);
        final AmigosAdapter amigosAdapter = new AmigosAdapter(getContext(), R.layout.cell_amigos, participantesList);
        friendsListView.setAdapter(amigosAdapter);


        showParticipants.show();
    }

    //----------------------- POST QUEDADA EN FIREBASE (REST) ---------------------

    private void postQuedadaFirebase(final long idSQL) {

        final BaseDatosUsuario BD = new BaseDatosUsuario(getContext());
        Quedada q = BD.getQuedadaById(idSQL);

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();

        int solofinde = 0;
        if (q.isSoloFinde()) {solofinde=1;}
        Gson gson = new Gson();
        String participantesString= gson.toJson(q.getParticipantes());

        Call<QuedadaResponse> quedadaResponseCall = endpoints.registrarQuedada(q.getNombre(), q.getFechaIni(), q.getFechaFin(),
                q.getHoraIni(),q.getHoraFin(),solofinde,participantesString);

        quedadaResponseCall.enqueue(new Callback<QuedadaResponse>() {
            @Override
            public void onResponse(Call<QuedadaResponse> call, Response<QuedadaResponse> response) {
                QuedadaResponse quedadaResponse = response.body();
                Log.e("IdQuedada FIREBASE", quedadaResponse.getId());

                BD.addIdFirebase(idSQL,quedadaResponse.getId()); //Guardo en mi bbdd sql el ID de Firebase

                enviarNotifPushQuedada(idSQL, quedadaResponse.getId());
            }
            @Override
            public void onFailure(Call<QuedadaResponse> call, Throwable t) {

            }
        });
    }

    //----------------------- ENVIAR NOTIFICACION PUSH QUEDADA - FIREBASE CLOUD MESSAGING ------------------------

    private void enviarNotifPushQuedada (final long idSQL, final String idFirebase) {

        final BaseDatosUsuario BD = new BaseDatosUsuario(getContext());
        ArrayList<Amigo> participantes = BD.getParticipantesQuedada(idSQL);

        SharedPreferences prefs = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        String emisor = prefs.getString("user","alguien");

        for (Amigo p : participantes){ //mando notificacion push ha cada participante excepto al usuario creador
            if (!p.getNombre().equals(emisor)) {
                notifPushQuedada(p.getId(), emisor, idFirebase);
            }
        }
    }

    private void notifPushQuedada(final String idReceptor,final String emisor,final String idQuedada) {

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCall = endpoints.pushQuedada(idReceptor, emisor, idQuedada);

        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse1 = response.body();
                Log.e("TOQUE ENVIADO A", usuarioResponse1.getNombre());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }

    //--------------------- FRAGMENT CALENDARIO QUEDADA * CALDROID -----------------------


    private void customizarCalendarioQuedada(Bundle savedInstanceState, Quedada quedada) {

        String fIni = quedada.getFechaIni();
        final String[] fechaIni = fIni.split("/");
        String fFin = quedada.getFechaFin();
        final String[] fechaFin = fFin.split("/");

        CaldroidFragment caldroidFragment = new CaldroidCustomFragment();

        //Si se crea después de haber rotado
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH,Integer.valueOf(fechaIni[1]));
            args.putInt(CaldroidFragment.YEAR, Integer.valueOf(fechaIni[2]));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
            args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefault);
            caldroidFragment.setArguments(args);
        }

        //Customizamos las fechas disponibles
        Calendar cal = Calendar.getInstance();

        // Min date = fechaIni
        cal.set(Integer.valueOf(fechaIni[2]),Integer.valueOf(fechaIni[1])-1,Integer.valueOf(fechaIni[0]));
        Date minDate = cal.getTime();

        // Max date = fechaFin
        cal = Calendar.getInstance();
        cal.set(Integer.valueOf(fechaFin[2]),Integer.valueOf(fechaFin[1])-1,Integer.valueOf(fechaFin[0]));
        Date maxDate = cal.getTime();

        caldroidFragment.setMinDate(minDate);
        caldroidFragment.setMaxDate(maxDate);


        android.support.v4.app.FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.quedadaCalendar, caldroidFragment);
        t.commit();

        fragmentCalendar.setVisibility(View.VISIBLE);
    }

    public static boolean validDateFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }


}