package com.example.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MotywFragment extends Fragment {
    //implements View.OnClickListener {

    private Button przelacz;
    SessionManager sessionManager;

    public MotywFragment() {
        // Required empty public constructor
    }

    public static MotywFragment newInstance() {
        MotywFragment fragment = new MotywFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager((getActivity().getApplication().getApplicationContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_motyw, container, false);

        przelacz = (Button) v.findViewById(R.id.przelacz);
        /*if (sessionManager.loadNightModeState() == true) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);

        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
        } */
        przelacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sessionManager.loadNightModeState()==true) {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                    sessionManager.setNightModeState(false);
                    // restartApp();
                }
                else {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                    sessionManager.setNightModeState(true);
                    //restartApp();
                }
            }
        });
        return v;
    }

    public void restartApp() {

        Fragment fragment = newInstance();
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
        /*Fragment currentFragment = getFragmentManager().findFragmentByTag("YourFragmentTag");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit(); */

        /*MyFragment fragment  = new MyFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,fragment);
        fragmentTransaction.commit(); */
    }

}