package com.proyecto.quedemos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Usuario on 16/06/2016.
 */
public class LoginFragment extends Fragment {

    private CallbackManager callbackManager = null;
    private AccessTokenTracker mtracker = null;
    private ProfileTracker mprofileTracker = null;

    public static final String PARCEL_KEY = "parcel_key";

    private LoginButton loginButton;

    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Profile profile = Profile.getCurrentProfile();
            homeFragment(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        mtracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.v("AccessTokenTracker","oldAccessToken=" + oldAccessToken + "||" + "CurrentAccessToken="+currentAccessToken);
            }
        };

        mprofileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.v("Session Tracker", "oldProfile="+oldProfile+"||"+"currentProfile"+currentProfile);
                homeFragment(currentProfile);
            }
        };

        mtracker.startTracking();
        mprofileTracker.startTracking();
    }

    private void homeFragment (Profile profile){

        if (profile != null){

            Bundle mBundle = new Bundle ();
            mBundle.putParcelable(PARCEL_KEY, profile);
            HomeFragment hf = new HomeFragment();
            hf.setArguments(mBundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainContainer, new HomeFragment());
            fragmentTransaction.commit();
/*
            String user = profile.getName().toString();
            SharedPreferences prefs = getActivity().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("user", user);
            Log.d("Usuario",user);
            edit.commit(); */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        mtracker.stopTracking();
        mprofileTracker.stopTracking();
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isLoggedIn()){
            loginButton.setVisibility(View.INVISIBLE);
            Profile profile = Profile.getCurrentProfile();
            homeFragment(profile);
        }
    }
}
