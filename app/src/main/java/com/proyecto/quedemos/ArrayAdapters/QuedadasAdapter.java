package com.proyecto.quedemos.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.proyecto.quedemos.R;
import com.proyecto.quedemos.SQLite.Amigo;
import com.proyecto.quedemos.SQLite.Quedada;
import com.proyecto.quedemos.VisualResources.ImageTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MartaMillan on 25/7/16.
 */

public class QuedadasAdapter extends ArrayAdapter<Quedada> {

    public QuedadasAdapter(Context ctx, int textViewResourceId, List<Quedada> event) {
        super(ctx, textViewResourceId, event);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        RelativeLayout row = (RelativeLayout) convertView;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = (RelativeLayout) inflater.inflate(R.layout.cell_quedada, null);

        TextView nombreQuedada = (TextView) row.findViewById(R.id.nombreQuedada);
        TextView horas = (TextView) row.findViewById(R.id.horas);
        TextView fechas = (TextView) row.findViewById(R.id.fechas);
        TextView numParticipantes = (TextView) row.findViewById(R.id.numParticipantes);

        nombreQuedada.setText(getItem(pos).getNombre());

        horas.setText(getItem(pos).getHoraIni() + " - " + getItem(pos).getHoraFin());

        if (getItem(pos).isSoloFinde()) {
            fechas.setText(getItem(pos).getFechaIni() + " al " + getItem(pos).getFechaFin() + " (fin de semana");
        } else {
            fechas.setText(getItem(pos).getFechaIni() + " al " + getItem(pos).getFechaFin());
        }

        numParticipantes.setText(getItem(pos).getParticipantes().size() + " participantes");

        return row;

    }
}
