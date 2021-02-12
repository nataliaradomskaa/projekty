package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.mysql.jdbc.Statement;

import org.mindrot.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class UpdateHaslaFragment extends Fragment {
    //implements View.OnClickListener {

    SessionManager sessionManager;
    Context context;
    Button zmienhaslo;
    EditText starehaslo, nowehaslo, powtorzhaslo;
    String login, haslo, nhaslo;
    String error="";
    ArrayList<String> parametry;
    Intent intent;

    public UpdateHaslaFragment() {
        // Required empty public constructor
    }

    public static UpdateHaslaFragment newInstance() {
        UpdateHaslaFragment fragment = new UpdateHaslaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    /*   Dla fragmentu:
         InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
         inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
         InputMethodManager.HIDE_NOT_ALWAYS);
     */

    private void schowajKlawiature() {
        View view = this.getActivity().getCurrentFocus(); //inside a fragment you should use getActivity()
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_hasla, container, false);
        context = v.getContext();
        parametry = new ArrayList<>();
        sessionManager = new SessionManager(context);
        starehaslo = (EditText) v.findViewById(R.id.starehaslo);
        nowehaslo = (EditText) v.findViewById(R.id.nowehaslo);
        powtorzhaslo = (EditText) v.findViewById(R.id.powtorzhaslo);
        zmienhaslo = (Button) v.findViewById(R.id.zmienhaslo);
        intent = new Intent(getActivity(), ZalogowanoActivity.class);
        zmienhaslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.zmienhaslo:
                        new UpdateHaslaFragment.Aktualizuj().execute();
                        break;
                }
            }
        });
        return v;
    }

    public boolean sprawdzHaslo(String h1, String h2) {
        //https://java2blog.com/validate-password-java/
        boolean zwroc = false;
        boolean duza_litera = true;
        boolean mala_litera = true;
        for (int i = 0; i < h1.length(); i++) {
            if (Character.isUpperCase(h1.charAt(i))) duza_litera = false;
            if (Character.isLowerCase(h1.charAt(i))) mala_litera = false;
        }
        if (h1.length() < 8) {
            error = "Hasło powinno posiadać minimum 8 znaków!";
        } else if (duza_litera || mala_litera) {
            error = "Użyj dużych i małych liter w haśle!";
        } else {
            if (h1.equals(h2)) {
                zwroc = true;
            } else {
                error = "Podane hasła nie są takie same!";
                zwroc = false;
            }
        }
        return zwroc;
    }


    public String hash(String haslo) {
        return BCrypt.hashpw(haslo, BCrypt.gensalt(10));
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
                //id = sessionManager.pobierzId();
                login = sessionManager.pobierzLogin();

                ResultSet resultSete = statement.executeQuery("SELECT password FROM usertest where login='" + login + "';");

                if(resultSete.next()){
                    haslo = resultSete.getString(1);
                }
                else {
                    error="Błąd sessionManager";
                }

                if (BCrypt.checkpw(starehaslo.getText().toString(), haslo)) { //jesli wprowadzone stare haslo jest dobre lecimy dalej
                    if (sprawdzHaslo(nowehaslo.getText().toString(), powtorzhaslo.getText().toString())) { // jesli nowe hasla sa takie same
                        //error="";
                        nhaslo = hash(nowehaslo.getText().toString());
                        statement.executeUpdate("UPDATE usertest SET password='" + nhaslo + "' where login='" + login + "';");
                        return true;
                    }
                    else {
                        error="Wprowadzone hasła są różne!";
                    }
                }
                else {
                    error="Wpisano niepoprawne stare hasło.";
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
                Toasty.success(context.getApplicationContext(), "Aktualizacja pomyślna.", Toast.LENGTH_LONG, true).show();
                //startActivity(intent); // do ZalogowanoActivty
                restartApp(); // restart fragmentu (wyczyszczenie pol)
            }
        }
    }

    public void restartApp() {

        Fragment fragment = newInstance();
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }


}