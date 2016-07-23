package com.proyecto.quedemos.SinUso;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

/**
 * Created by Usuario on 16/06/2016.
 */
public class HomeFragment extends Fragment {
/*
    private ImageView profile_pic = null;
    private TextView name = null;
    private Button logoutButton = null;
    private Profile profile = null;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        name = (TextView) view.findViewById(R.id.tv_name);
        logoutButton = (Button) view.findViewById(R.id.logout_button);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(getFragmentManager(), view.getContext()));

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });*/
/*
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null) {
            profile = (Profile) bundle.getParcelable(FacebookLoginFragment.PARCEL_KEY);
        } else {
            profile = Profile.getCurrentProfile();
        }

        name.setText("Welcome \n" + profile.getName());

        Picasso.with(getActivity())
                .load(profile.getProfilePictureUri(400,400).toString())
                .into(profile_pic);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new FacebookLoginFragment());
        fragmentTransaction.commit();

        SharedPreferences prefs = getActivity().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
        /*SharedPreferences.Editor edit = prefs.edit();
        edit.clear();*/
/*
        String pr_user = prefs.getString("user", "");
        Log.e("PRUEBA", pr_user);
    }

    /*********** FRAGMENT PAGER ADAPTER *********/
/*    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments[] = {"Calendario", "Grupos", "Ajustes"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position){
            //create fragment
            switch (position){
                case 0:
                    return new FragmentCalendar();
                case 1:
                    return new FragmentGroups();
                case 2:
                    return new FragmentOptions();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return fragments[position];
        }


    }*/

    //-------------- M E N U ----------------------------
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //DISTINTO MENU SEGUN USUARIO LOGUEADO? *******************************
        inflater.inflate(R.menu.menu_main, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
}
