package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.mindrot.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class UpdateDanychFragment extends Fragment {

    SessionManager sessionManager;
    Context context;
    Button zmiendane;
    EditText imie, nazwisko;
    String login, nimie, nnazwisko;
    String error="";
    ArrayList<String> parametry;
    Intent intent;
    TextView tv_podpis;
    ZalogowanoActivity activity;
    UstawieniaFragment fragment;

    public UpdateDanychFragment() {
        // Required empty public constructor
    }

    public static UpdateDanychFragment newInstance() {
        UpdateDanychFragment fragment = new UpdateDanychFragment();
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
        View v = inflater.inflate(R.layout.fragment_update_danych, container, false);
        context = v.getContext();
        parametry = new ArrayList<>();
        sessionManager = new SessionManager(context);
        imie = (EditText) v.findViewById(R.id.noweimie);
        nazwisko = (EditText) v.findViewById(R.id.nowenazwisko);
        zmiendane = (Button) v.findViewById(R.id.zmiendane);
        activity = (ZalogowanoActivity) getActivity();
        fragment = (UstawieniaFragment) getParentFragment();
        zmiendane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.zmiendane:
                        new UpdateDanychFragment.Aktualizuj().execute();
                        break;
                }
            }
        });
        return v;
    }

    private void schowajKlawiature() {
        View view = this.getActivity().getCurrentFocus(); //inside a fragment you should use getActivity()
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean sprawdzImie(String imie) {
        boolean zwroc = true;
        if (imie.length() < 3) {
            zwroc = false;
            error = "Imię jest za krótkie!";
        } else if (!Character.isUpperCase(imie.charAt(0))) {
            zwroc = false;
            error = "Pierwsza litera powinna być duża!";
        } else {
            for (int i = 0; i < imie.length(); i++) {
                if (!Character.isLetter(imie.charAt(i))) {
                    zwroc = false;
                    error = "Imię składa się tylko z liter!";
                }
            }
        }
        return zwroc;
    }

    public boolean sprawdzNazwisko(String nazwisko) {
        boolean zwroc = true;
        if (nazwisko.length() < 3) {
            zwroc = false;
            error = "Nazwisko jest za krótkie!";
        } else if (!Character.isUpperCase(nazwisko.charAt(0))) {
            zwroc = false;
            error = "Pierwsza litera powinna być duża!";
        } else {
            for (int i = 0; i < nazwisko.length(); i++) {
                if (!Character.isLetter(nazwisko.charAt(i))) {
                    zwroc = false;
                    error = "Nazwisko składa się tylko z liter!";
                }
            }
        }
        return zwroc;
    }

    class Aktualizuj extends AsyncTask<Void, Void, Boolean> {
        Connection connection;
        Statement statement;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            schowajKlawiature();
        }

        @Override
        protected final Boolean doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://messengeruwb.xaa.pl/p562436_messenger?useSSL=false","p562436", "h2riOY8Kf");
                statement = connection.createStatement();
                login = sessionManager.pobierzLogin();
                nimie = imie.getText().toString();
                nnazwisko = nazwisko.getText().toString();
                if (sprawdzImie(nimie) && sprawdzNazwisko(nnazwisko)) {
                    statement.executeUpdate("UPDATE usertest SET name ='" + nimie + "', surname ='" + nnazwisko + "' where login='" + login + "';");
                    sessionManager.editor.putString("KEY_IMIE", nimie);
                    sessionManager.editor.putString("KEY_NAZWISKO", nnazwisko);
                    sessionManager.editor.commit();
                    return true;
                }
                else {
                    //error="";
                    return false;
                }
            }
            catch (Exception e) {
                error = e.getMessage();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean pass) {
            super.onPostExecute(pass);
            try {
                connection.close();
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (!pass) {
                Toasty.error(context.getApplicationContext(), "Błąd: " + error, Toast.LENGTH_LONG, true).show();

            } else {
                tv_podpis = (TextView) activity.nvDrawer.getHeaderView(0).findViewById(R.id.tv_podpis);
                tv_podpis.setText(sessionManager.preferences.getString("KEY_IMIE", "IMIĘ") + " " + sessionManager.preferences.getString("KEY_NAZWISKO", "NAZWISKO"));
                Toasty.success(context.getApplicationContext(), "Aktualizacja pomyślna.", Toast.LENGTH_LONG, true).show();
                restartApp(); // restart fragmentu (wyczyszczenie pol)
            }
        }
    }

    public void restartApp() {
        Fragment fragment = newInstance();
        getFragmentManager().beginTransaction().replace(R.id.containerl,fragment).commit();
    }



}