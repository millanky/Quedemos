package com.proyecto.quedemos.ArrayAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
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
import com.proyecto.quedemos.VisualResources.ImageTransformation;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by MartaMillan on 24/7/16.
 */
public class AmigosAdapter extends ArrayAdapter<Amigo> {

    ImageView imagen;
    TextView nombre;

    public AmigosAdapter(Context ctx, int textViewResourceId, List<Amigo> event) {
        super(ctx, textViewResourceId, event);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        RelativeLayout row = (RelativeLayout) convertView;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = (RelativeLayout) inflater.inflate(R.layout.cell_amigos, null);

        nombre = (TextView) row.findViewById(R.id.nombre);
        imagen  = (ImageView) row.findViewById(R.id.imgAmigo);


        nombre.setText(getItem(pos).getNombre());


        Picasso.with(getContext())
                .load(getItem(pos).getUrlimg())
                .placeholder(R.drawable.q_logo)
                .error(R.drawable.q_logo)
                .resize(100, 100)
                .transform(new ImageTransformation())
                .into(imagen);

        return row;

    }
}
