package com.example.messenger;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class UstawieniaFragment extends Fragment {
    Button zmiendane, zmienhaslo;

    public UstawieniaFragment() {
        // Required empty public constructor
    }

    public static UstawieniaFragment newInstance() {
        UstawieniaFragment fragment = new UstawieniaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ustawienia, container, false);

        zmiendane = (Button) v.findViewById(R.id.bt_zmiendane);
        zmienhaslo = (Button) v.findViewById(R.id.bt_zmienhaslo);

        zmiendane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                UpdateDanychFragment up = new UpdateDanychFragment();
                ft.replace(R.id.containerl, up);
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });

        zmienhaslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                UpdateHaslaFragment uh = new UpdateHaslaFragment();
                ft.replace(R.id.containerl, uh); // containerl to glowny framelayout
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });

        return v;
    }
}