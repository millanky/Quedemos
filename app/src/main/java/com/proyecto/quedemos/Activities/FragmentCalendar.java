package com.proyecto.quedemos.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.Caldroid.CaldroidCustomFragment;
import com.proyecto.quedemos.R;
import com.proyecto.quedemos.SQLite.BaseDatosUsuario;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")

/**
 * Created by Usuario on 17/06/2016.
 */
public class FragmentCalendar extends Fragment {

    private boolean undo = false;
    private View positiveAction;
    private EditText nombreEvento;
    private EditText horaIni;
    private EditText horaFin;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -8);
        Date blueDate = cal.getTime();

        //Fija un día con un evento
        cal = Calendar.getInstance();
        cal.set(2016, Calendar.AUGUST, 30);
        Date blueDate2 = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Log.e("DiaInt  ", String.valueOf(Calendar.DATE));
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate2);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate2);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

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
            args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefaultDark); //MI PROPIO THEME
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
                //Toast.makeText(view.getContext(),formatter.format(date),
                        //Toast.LENGTH_SHORT).show();

                final EditText txtUrl = new EditText(view.getContext());

                txtUrl.setHint("Evento");

                showCustomView();

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
               // Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
              /*  Toast.makeText(view.getContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                   /* Toast.makeText(view.getContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();*/
                }
            }

        };

        // Setup Caldroid
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

    /*****RECOGER EVENTOS****/

    public void showCustomView() {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("NUEVO EVENTO")
                .customView(R.layout.insertar_evento, true) //true indica con ScrollView
                //.icon(R.drawable.write)
                .positiveText("Guardar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //TODO:GUARDAR EVENTO
                        Log.e("EVENTO: ", nombreEvento.getText().toString());
                        Log.e("EVENTO: ", horaIni.getText().toString());
                        Log.e("EVENTO: ", horaFin.getText().toString());

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, 0);
                        String formatDate = format.format(cal.getTime());

                        Log.e("DIA", formatDate);
                        BaseDatosUsuario eventosBD = new BaseDatosUsuario(getContext());
                        eventosBD.nuevoEvento(nombreEvento.getText().toString(), horaIni.getText().toString(), horaFin.getText().toString(),formatDate);
                        eventosBD.getEventoPorFecha(formatDate);

                    }
                })
                .negativeText("Cancelar")
                .build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        nombreEvento = (EditText) dialog.getCustomView().findViewById(R.id.nombre);
        horaIni = (EditText) dialog.getCustomView().findViewById(R.id.editHoraIni);
        horaFin = (EditText) dialog.getCustomView().findViewById(R.id.editHoraFin);

        nombreEvento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        horaIni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        horaFin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }
}