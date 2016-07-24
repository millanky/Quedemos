package com.proyecto.quedemos.ActivitiesAndFragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.Calendar.CaldroidCustomFragment;
import com.proyecto.quedemos.ArrayAdapters.EventosAdapter;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.SQLite.BaseDatosUsuario;
import com.proyecto.quedemos.SQLite.Evento;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressLint("SimpleDateFormat")

/**
 * Created by Usuario on 17/06/2016.
 */
public class FragmentCalendar extends Fragment {

    private boolean undo = false;
    private ArrayList<Evento> eventosList;
    private EventosAdapter eAdapter;
    private ListView eventosDiaList;
    private View positiveAction;
    private EditText nombreEvento;
    private EditText horaIni;
    private EditText horaFin;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;


    private void setCustomResourceForDates() {

        Calendar cal = Calendar.getInstance();
        BaseDatosUsuario eventosBD = new BaseDatosUsuario(getContext());
        ArrayList<String> diaEventoMes = eventosBD.getEventosMes(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1));
        Date blueDate;
        ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.green));
        //ColorDrawable green = new ColorDrawable(Color.GREEN);

        if (caldroidFragment != null) {
            for (String evento : diaEventoMes) {
                cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Integer.parseInt(evento));
                blueDate = cal.getTime();
                caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
                //caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        final View modalView = inflater.inflate(R.layout.modal_insertar_evento,container,false);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        caldroidFragment = new CaldroidCustomFragment();

        //Si se crea después de haber rotado
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
            //args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false); //MODO COMPACTO
            args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefault); //MI PROPIO THEME
            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        android.support.v4.app.FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.cal, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                BaseDatosUsuario eventosBD = new BaseDatosUsuario(getContext());
                eventosList = eventosBD.listadoEventosDia(formatter.format(date).split("-"));
                if (eventosList.size() > 0) {

                    showListView(eventosList, formatter.format(date));

                } else {

                    showAddView(formatter.format(date));
                }

            }

            @Override
            public void onChangeMonth(int month, int year) {
                setCustomResourceForDates();
                caldroidFragment.refreshView();

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                }
            }

        };

        // Customizacion Caldroid adicional
        caldroidFragment.setCaldroidListener(listener);

        final TextView textView = (TextView) view.findViewById(R.id.textview);

        final Button customizeButton = (Button) view.findViewById(R.id.customize_button);

        // Customize the calendar --> Días seleccionados
        customizeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (undo) {
                    customizeButton.setText(getString(R.string.customize));
                    textView.setText("");

                    // Reset calendar
                    caldroidFragment.clearDisableDates();
                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setMinDate(null);
                    caldroidFragment.setMaxDate(null);
                    caldroidFragment.setShowNavigationArrows(true);
                    caldroidFragment.setEnableSwipe(true);
                    caldroidFragment.refreshView();
                    undo = false;
                    return;
                }

                // Else
                undo = true;
                customizeButton.setText(getString(R.string.undo));
                Calendar cal = Calendar.getInstance();

                // Min date is last 7 days
                cal.add(Calendar.DATE, -7);
                Date minDate = cal.getTime();

                // Max date is next 7 days
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 14);
                Date maxDate = cal.getTime();

                // Set selected dates
                // From Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 2);
                Date fromDate = cal.getTime();

                // To Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 3);
                Date toDate = cal.getTime();

                // Set disabled dates
                ArrayList<Date> disabledDates = new ArrayList<Date>();
                for (int i = 5; i < 8; i++) {
                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, i);
                    disabledDates.add(cal.getTime());
                }

                // Customize
                caldroidFragment.setMinDate(minDate);
                caldroidFragment.setMaxDate(maxDate);
                caldroidFragment.setDisableDates(disabledDates);
                caldroidFragment.setSelectedDates(fromDate, toDate);
                caldroidFragment.setShowNavigationArrows(false);
                caldroidFragment.setEnableSwipe(false);

                caldroidFragment.refreshView();

                // Move to date
                // cal = Calendar.getInstance();
                // cal.add(Calendar.MONTH, 12);
                // caldroidFragment.moveToDate(cal.getTime());

                String text = "Today: " + formatter.format(new Date()) + "\n";
                text += "Min Date: " + formatter.format(minDate) + "\n";
                text += "Max Date: " + formatter.format(maxDate) + "\n";
                text += "Select From Date: " + formatter.format(fromDate)
                        + "\n";
                text += "Select To Date: " + formatter.format(toDate) + "\n";
                for (Date date : disabledDates) {
                    text += "Disabled Date: " + formatter.format(date) + "\n";
                }

                textView.setText(text);
            }
        });

        //---------CALENDARIO EN POPUP
        Button showDialogButton = (Button) view.findViewById(R.id.show_dialog_button);

        final Bundle state = savedInstanceState;
        showDialogButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Setup caldroid to use as dialog
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(listener);

                // If activity is recovered from rotation
                final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                if (state != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getFragmentManager(), state,
                            "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    // Setup arguments
                    Bundle bundle = new Bundle();
                    // Setup dialogTitle
                    dialogCaldroidFragment.setArguments(bundle);
                }

                dialogCaldroidFragment.show(getFragmentManager(),
                        dialogTag);
            }
        });

        return view;
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    /***** MOSTRAR EVENTOS ****/

    public void showListView (ArrayList<Evento> eventosList, String date) {

        final String[] eventDate = date.split("-");
        final String fecha = date;

        MaterialDialog dialogView = new MaterialDialog.Builder(getContext())
                .title("Eventos " + eventDate[0] + "/" +eventDate[1])
                .customView(R.layout.modal_ver_eventos, false) //true indica con ScrollView
                .icon(getResources().getDrawable(R.drawable.ver))
                .positiveText("Nuevo evento")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        showAddView(fecha);
                    }
                })
                .negativeText("Cerrar")
                .build();

        eventosDiaList = (ListView) dialogView.getCustomView().findViewById(R.id.listadoEventos);
        eAdapter = new EventosAdapter(getContext(),R.layout.cell_eventos,eventosList);
        eventosDiaList.setAdapter(eAdapter);

        dialogView.show();

    }

    /***** AÑADIR EVENTOS ****/

    public void showAddView(String date) {

        final BaseDatosUsuario eventosBD = new BaseDatosUsuario(getContext());
        final String[] eventDate = date.split("-");

        MaterialDialog dialogNew = new MaterialDialog.Builder(getContext())
                .title("Nuevo evento:")
                .customView(R.layout.modal_insertar_evento, true) //true indica con ScrollView
                .icon(getResources().getDrawable(R.drawable.write))
                .positiveText("Guardar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        //GUARDAR EVENTO

                        eventosBD.nuevoEvento(nombreEvento.getText().toString(), horaIni.getText().toString(), horaFin.getText().toString(), eventDate, 0);


                        setCustomResourceForDates(); //Actualizar calendario
                        caldroidFragment.refreshView();

                    }
                })
                .negativeText("Cancelar")
                .build();

        positiveAction = dialogNew.getActionButton(DialogAction.POSITIVE);
        nombreEvento = (EditText) dialogNew.getCustomView().findViewById(R.id.nombre);
        horaIni = (EditText) dialogNew.getCustomView().findViewById(R.id.editHoraIni);
        horaFin = (EditText) dialogNew.getCustomView().findViewById(R.id.editHoraFin);

        nombreEvento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (eventosBD.existeEvento(eventDate,horaIni.getText().toString(),horaFin.getText().toString()) && validacionHora(horaIni.getText().toString()) && validacionHora(horaFin.getText().toString())) {
                    Toast.makeText(getContext(), "Ya existe un evento en ese intervalo", Toast.LENGTH_SHORT).show();
                }else if (parseDate(horaIni.getText().toString()).after(parseDate(horaFin.getText().toString())) && validacionHora(horaIni.getText().toString()) && validacionHora(horaFin.getText().toString())) {
                    Toast.makeText(getContext(), "La hora de inicio es posterior a la hora final", Toast.LENGTH_SHORT).show();
                }else {
                    positiveAction.setEnabled(s.toString().trim().length() > 0 && validacionHora(horaIni.getText().toString()) && validacionHora(horaFin.getText().toString()));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        horaIni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (eventosBD.existeEvento(eventDate,horaIni.getText().toString(),horaFin.getText().toString()) && validacionHora(horaIni.getText().toString()) && validacionHora(horaFin.getText().toString())) {
                    Toast.makeText(getContext(), "Ya existe un evento en ese intervalo", Toast.LENGTH_SHORT).show();
                }else if (parseDate(horaIni.getText().toString()).after(parseDate(horaFin.getText().toString())) && validacionHora(horaIni.getText().toString()) && validacionHora(horaFin.getText().toString())) {
                    Toast.makeText(getContext(), "La hora de inicio es posterior a la hora final", Toast.LENGTH_SHORT).show();
                }else {
                        positiveAction.setEnabled(validacionHora(s.toString()) && nombreEvento.getText().length() != 0 && validacionHora(horaFin.getText().toString()));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        horaFin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (eventosBD.existeEvento(eventDate,horaIni.getText().toString(),horaFin.getText().toString()) && validacionHora(horaIni.getText().toString()) && validacionHora(horaFin.getText().toString())) {
                    Toast.makeText(getContext(), "Ya existe un evento en ese intervalo", Toast.LENGTH_SHORT).show();
                }else if (parseDate(horaIni.getText().toString()).after(parseDate(horaFin.getText().toString())) && validacionHora(horaIni.getText().toString()) && validacionHora(horaFin.getText().toString())) {
                    Toast.makeText(getContext(), "La hora de inicio es posterior a la hora final", Toast.LENGTH_SHORT).show();
                }else {
                    positiveAction.setEnabled(validacionHora(s.toString()) && validacionHora(horaIni.getText().toString()) && nombreEvento.getText().length() != 0);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        dialogNew.show();
        positiveAction.setEnabled(false);
    }

    private boolean validacionHora(String hora){
        final String timePattern = "^([0-1]?[0-9]|2[0-4]):([0-5][0-9])(:[0-5][0-9])?$";
        Pattern patt = Pattern.compile(timePattern);
        Matcher matcher = patt.matcher(hora);
        return matcher.matches();
    }

    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

}