package com.proyecto.quedemos.Activities;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.proyecto.quedemos.Activities.MainActivity;
import com.proyecto.quedemos.R;

import org.w3c.dom.Text;

/**
 * Created by Usuario on 16/06/2016.
 */
public class LoginFragment extends Fragment {

    private CallbackManager callbackManager = null;
    private AccessTokenTracker mtracker = null;
    private ProfileTracker mprofileTracker = null;

    public static final String PARCEL_KEY = "parcel_key";

    public LoginFragment()
    {
        setRetainInstance(true);
    }

    private LoginButton loginButtonFacebook;

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


            /*HomeFragment hf = new HomeFragment();
            hf.setArguments(mBundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainContainer, new HomeFragment());
            fragmentTransaction.commit();*/

            //Guardo datos de usuario en las shared preferences
            String user = profile.getName().toString();
            String pictureUri = profile.getProfilePictureUri(20,20).toString();
            SharedPreferences prefs = getActivity().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("user", user);
            edit.putString("picture", pictureUri);
            edit.commit();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtras(mBundle);
            getActivity().startActivity(intent);


        } else {
            Intent i = new Intent (getActivity(), MainActivity.class);
            getActivity().startActivity(i);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButtonFacebook = (LoginButton) view.findViewById(R.id.login_button);
        loginButtonFacebook.setReadPermissions("user_friends");

        loginButtonFacebook.setFragment(this);
        loginButtonFacebook.registerCallback(callbackManager, callback);
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
            loginButtonFacebook.setVisibility(View.INVISIBLE);
            Profile profile = Profile.getCurrentProfile();
            homeFragment(profile);
        }
    }
}
