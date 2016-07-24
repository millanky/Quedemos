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
import com.proyecto.quedemos.SQLite.Evento;

import java.util.List;

/**
 * Created by MartaMillan on 24/7/16.
 */
public class AmigosAdapter extends ArrayAdapter<Amigo> {

    public AmigosAdapter(Context ctx, int textViewResourceId, List<Amigo> event) {
        super(ctx, textViewResourceId, event);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        RelativeLayout row = (RelativeLayout) convertView;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = (RelativeLayout) inflater.inflate(R.layout.cell_amigos, null);

        TextView nombre = (TextView) row.findViewById(R.id.nombre);
       // ImageView imagen  = (ImageView) row.findViewById(R.id.imgAmigo);


        nombre.setText(getItem(pos).getNombre());

        return row;

    }
}
