package com.proyecto.quedemos.ActivitiesAndFragments;

/**
 * Created by MartaMillan on 16/7/16.
 */
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.proyecto.quedemos.R;

public class TabFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static TabFragment newInstance(int position) {
        TabFragment f = new TabFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (position == 0) {
            return inflater.inflate(R.layout.fragment_calendar, container, false);
        } else if (position == 1) {

              View view = inflater.inflate(R.layout.fragment_groups, container, false);

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
        } else {
            return inflater.inflate(R.layout.fragment_options, container, false);
        }


        /*
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                .getDisplayMetrics());

        TextView v = new TextView(getActivity());
        params.setMargins(margin, margin, margin, margin);
        v.setLayoutParams(params);
        v.setLayoutParams(params);
        v.setGravity(Gravity.CENTER);
        v.setBackgroundResource(R.drawable.background_card);
        v.setText("CARD " + (position + 1));

        fl.addView(v);


        return fl;*/

    }

}