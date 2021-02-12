package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.mindrot.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class LogowanieActivity extends AppCompatActivity {
    TextView tv_login, tv_haslo, tv_progress;
    Button btn_zaloguj;
    Intent i_uzytkownik;
    ArrayList<String> parametry;
    SessionManager sessionManager;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);
        btn_zaloguj = findViewById(R.id.button3);
        tv_login = findViewById(R.id.editTextTextPersonName);
        tv_haslo = findViewById(R.id.editTextTextPassword2);
        i_uzytkownik = new Intent(this, ZalogowanoActivity.class);
        pb = findViewById(R.id.progressBar);
        tv_progress = findViewById(R.id.textView3);
        parametry = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onClick(View view) {
        new Zaloguj().execute(parametry);
    }

    boolean sprawdzLogin(String log) {

        boolean zwroc = true;
        if(log.length()>2) {
            for (int i = 0; i < log.length(); i++) {
                if (!(Character.isLetterOrDigit(log.charAt(i)))) {
                    zwroc = false;
                    break;
                }
            }
        } else {
            zwroc = false;
        }
        return zwroc;
    }

    class Zaloguj extends AsyncTask<ArrayList<String>, Integer, Boolean> {
        String error = "", login, imie, nazwisko, avatar;
        int id;
        Connection connection;
        Statement statement;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            closeKeyboard();
            tv_progress.setTextColor(getResources().getColor(R.color.colorBlack));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            pb.setVisibility(View.VISIBLE);
            tv_progress.setVisibility(View.VISIBLE);
            onProgressUpdate(0);
        }

        @SuppressLint("WrongThread")
        @Override
        protected final Boolean doInBackground(ArrayList<String>... param) {
            try {
                ArrayList<String> dane = param[0];
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://messengeruwb.xaa.pl/p562436_messenger?useSSL=false", "p562436", "h2riOY8Kf");
                statement = connection.createStatement();
                onProgressUpdate(30);
                if (sprawdzLogin(tv_login.getText().toString())) {
                    dane.add(tv_login.getText().toString());
                    ResultSet resultSet = statement.executeQuery("SELECT password FROM usertest WHERE login='" + dane.get(0) + "';");
                    if (resultSet.next()) {
                        if (BCrypt.checkpw(tv_haslo.getText().toString(), resultSet.getString(1))) {
                            ResultSet resultSete  = statement.executeQuery("SELECT id, login, name, surname, avatar FROM usertest where login='" + dane.get(0) + "';");
                            onProgressUpdate(60);
                            if (resultSete.next()) {
                                onProgressUpdate(100);
                                id = resultSete.getInt(1);
                                login = resultSete.getString(2);
                                imie = resultSete.getString(3);
                                nazwisko = resultSete.getString(4);
                                avatar = resultSete.getString(5);
                                sessionManager.createLoginSession(id, login, imie, nazwisko, avatar);
                                return true;
                            } else {
                                error = "Niepoprawny login lub hasło.";
                            }
                        } else {
                            error = "Niepoprawny login lub hasło!";
                        }
                    }
                } else {
                    error = "Niepoprawny login lub hasło!";
                }
            } catch (Exception e) {
                error = e.getMessage();
            }
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == 0) {
                tv_progress.setText("Przygotowanie do podłączenia z bazą danych.");
                pb.setProgress(values[0]);
            } else if (values[0] == 30) {
                tv_progress.setText("Przygotowanie zapytania.");
                pb.setProgress(values[0]);
            } else if (values[0] == 60) {
                tv_progress.setText("Sprawdzanie czy istnieje uzytkownik.");
                pb.setProgress(values[0]);
            } else {
                tv_progress.setText("Finalizowanie");
                pb.setProgress(100);
            }
        }

        @Override
        protected void onPostExecute(Boolean pass) {
            super.onPostExecute(pass);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toasty.Config.getInstance().setTextSize(20).apply();
            try {
                connection.close();
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (!pass) {
                Toasty.error(getApplicationContext(), "Błąd logowania: " + error, Toast.LENGTH_LONG, true).show();
                pb.setVisibility(View.INVISIBLE);
                tv_progress.setText("");
            } else {
                Toasty.success(getApplicationContext(), "Logowanie pomyślne!", Toast.LENGTH_LONG, true).show();
                pb.setVisibility(View.INVISIBLE);
                tv_progress.setVisibility(View.INVISIBLE);
                startActivity(i_uzytkownik);
            }
        }
    }
}
