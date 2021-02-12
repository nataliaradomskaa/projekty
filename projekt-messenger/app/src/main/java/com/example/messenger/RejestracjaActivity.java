package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;


public class RejestracjaActivity extends AppCompatActivity {

    Button btn_zarejestruj;
    EditText et_login, et_haslo, et_haslor, et_mail, et_imie, et_nazwisko;
    TextView tv_regulamin;
    CheckBox chb_regulamin;
    String currentDate;
    ArrayList<String> parametry, logins, mails;
    Intent i_main;
    String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);

        et_login = findViewById(R.id.et_login);
        et_haslo = findViewById(R.id.et_haslo);
        et_haslor = findViewById(R.id.et_haslor);
        et_mail = findViewById(R.id.et_mail);
        et_imie = findViewById(R.id.et_imie);
        et_nazwisko = findViewById(R.id.et_nazwisko);
        chb_regulamin = findViewById(R.id.chb_regulamin);
        btn_zarejestruj = findViewById(R.id.btn_zarejestruj);
        parametry = new ArrayList<String>();
        logins = new ArrayList<String>();
        mails = new ArrayList<String>();
        tv_regulamin = findViewById(R.id.tv_regulamin);
        tv_regulamin.setMovementMethod(LinkMovementMethod.getInstance());
        error = "";
        // loginy();
        i_main = new Intent(this, MainActivity.class);
    }
// wyjebać do async task
//    public void loginy() {
//        String error = "";
//        try
//        {
////          https://4programmers.net/Forum/Java/267737-could_not_create_connection_to_database_server_attempted_reconnect_3_times_giving_up  poniższe 2 linie
////          https://developer.android.com/reference/android/os/StrictMode.html
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://messengeruwb.xaa.pl/p562436_messenger?useSSL=false","p562436", "h2riOY8Kf");
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT login,mail FROM usertest;");
//            while (resultSet.next()) {
//                logins.add(resultSet.getString(1));
//                mails.add(resultSet.getString(2));
//            }
//            connection.close();
//            statement.close();
//        }
//        catch(Exception e)
//        {
//            error = e.getMessage();
//        }
//        if(error!="") Toasty.error(getApplicationContext(), error, Toast.LENGTH_LONG, true).show();
//    }

    public void onClick(View view) {

//        Toasty.Config.getInstance().setTextSize(20).apply();
//        if (sprawdzLogin(et_login.getText().toString()) && sprawdzHaslo(et_haslo.getText().toString(), et_haslor.getText().toString()) && sprwadzMail(et_mail.getText().toString()) && sprawdzImie(et_imie.getText().toString()) && sprawdzNazwisko(et_nazwisko.getText().toString())) {
//            if (chb_regulamin.isChecked()) {
//                parametry.add(et_login.getText().toString());
//                parametry.add(hash(et_haslo.getText().toString()));
//                parametry.add(et_mail.getText().toString());
//                parametry.add(et_imie.getText().toString());
//                parametry.add(et_nazwisko.getText().toString());
//                currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//                parametry.add(currentDate);
//                new Zarejestruj().execute(parametry);
//            } else {
//                Toasty.warning(getApplicationContext(), "Zaakceptuj regulamin!", Toast.LENGTH_LONG, true).show();
//            }
//        }
        new Zarejestruj().execute(parametry);
    }

    public boolean sprawdzLogin(String log) {
        boolean zwroc = true;
        if (log.length() > 2) {
            for (String s : logins) {
                if (log.equals(s)) {
                    zwroc = false;
                    //Toasty.warning(getApplicationContext(), "Login jest już użyty!", Toast.LENGTH_LONG, true).show();
                    error = "Login jest już użyty!";
                    break;
                }
            }
        } else {
            //Toasty.warning(getApplicationContext(), "Login powinien mieć długość przynajmniej 3 znaków!", Toast.LENGTH_LONG, true).show();
            error = "Login powinien mieć długość przynajmniej 3 znaków!";
            zwroc = false;
        }

        for (int i = 0; i < log.length(); i++) {
            if (!(Character.isLetterOrDigit(log.charAt(i)))) {
                zwroc = false;
                //Toasty.warning(getApplicationContext(), "Login może składać się tylko z liter i cyfr!", Toast.LENGTH_LONG, true).show();
                error = "Login może składać się tylko z liter i cyfr!";
                break;
            }
        }
        return zwroc;
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
            //Toasty.warning(getApplicationContext(), "Hasło powinno posiadać minimum 8 znaków!", Toast.LENGTH_LONG, true).show();
            error = "Hasło powinno posiadać minimum 8 znaków!";
        } else if (duza_litera || mala_litera) {
            //Toasty.warning(getApplicationContext(), "Użyj dużych i małych liter w haśle!", Toast.LENGTH_LONG, true).show();
            error = "Użyj dużych i małych liter w haśle!";
        } else {
            if (h1.equals(h2)) {
                zwroc = true;
            } else {
                //Toasty.warning(getApplicationContext(), "Podane hasła nie są takie same!", Toast.LENGTH_LONG, true).show();
                error = "Podane hasła nie są takie same!";
                zwroc = false;
            }
        }
        return zwroc;
    }

    public boolean sprwadzMail(String mail) {
        boolean zwroc = true;
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(mail);
        zwroc = matcher.matches();
        if (!zwroc) {
            //Toasty.warning(getApplicationContext(), "Błędny adres e-mail!", Toast.LENGTH_LONG, true).show();
            error = "Błędny adres e-mail!";
        } else {
            for (String s : mails) {
                if (mail.equals(s)) {
                    zwroc = false;
                    // Toasty.warning(getApplicationContext(), "Adres e-mail jest już użyty!", Toast.LENGTH_LONG, true).show();
                    error = "Adres e-mail jest już użyty!";
                    break;
                }
            }
        }
        return zwroc;
    }

    public boolean sprawdzImie(String imie) {

        boolean zwroc = true;
        if (imie.length() < 3) {
            zwroc = false;
            //Toasty.warning(getApplicationContext(), "Imię jest za krótkie!", Toast.LENGTH_LONG, true).show();
            error = "Imię jest za krótkie!";
        } else if (!Character.isUpperCase(imie.charAt(0))) {
            zwroc = false;
            //Toasty.warning(getApplicationContext(), "Pierwsza litera powinna być duża!", Toast.LENGTH_LONG, true).show();
            error = "Pierwsza litera powinna być duża!";
        } else {
            for (int i = 0; i < imie.length(); i++) {
                if (!Character.isLetter(imie.charAt(i))) {
                    zwroc = false;
                    Toasty.warning(getApplicationContext(), "Imię składa się tylko z liter!", Toast.LENGTH_LONG, true).show();
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
            //Toasty.warning(getApplicationContext(), "Nazwisko jest za krótkie!", Toast.LENGTH_LONG, true).show();
            error = "Nazwisko jest za krótkie!";
        } else if (!Character.isUpperCase(nazwisko.charAt(0))) {
            zwroc = false;
            //Toasty.warning(getApplicationContext(), "Pierwsza litera powinna być duża!", Toast.LENGTH_LONG, true).show();
            error = "Pierwsza litera powinna być duża!";
        } else {
            for (int i = 0; i < nazwisko.length(); i++) {
                if (!Character.isLetter(nazwisko.charAt(i))) {
                    zwroc = false;
                    //Toasty.warning(getApplicationContext(), "Nazwisko składa się tylko z liter!", Toast.LENGTH_LONG, true).show();
                    error = "Nazwisko składa się tylko z liter!";
                }
            }
        }
        return zwroc;
    }

    public String hash(String haslo) {
        return BCrypt.hashpw(haslo, BCrypt.gensalt(10));
    }

    class Zarejestruj extends AsyncTask<ArrayList<String>, Void, Void> {
        Connection connection;
        Statement statement;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected final Void doInBackground(ArrayList<String>... param) {
            try {
                // połączenie z bazą
                ArrayList<String> dane = param[0];
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://messengeruwb.xaa.pl/p562436_messenger?useSSL=false", "p562436", "h2riOY8Kf");
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT login,mail FROM usertest;");
                while (resultSet.next()) {
                    logins.clear();
                    mails.clear();
                    logins.add(resultSet.getString(1));
                    mails.add(resultSet.getString(2));
                }
                if (sprawdzLogin(et_login.getText().toString()) && sprawdzHaslo(et_haslo.getText().toString(), et_haslor.getText().toString()) && sprwadzMail(et_mail.getText().toString()) && sprawdzImie(et_imie.getText().toString()) && sprawdzNazwisko(et_nazwisko.getText().toString())) {
                    if (chb_regulamin.isChecked()) {
                        error = "";
                        dane.add(et_login.getText().toString());
                        dane.add(hash(et_haslo.getText().toString()));
                        dane.add(et_mail.getText().toString());
                        dane.add(et_imie.getText().toString());
                        dane.add(et_nazwisko.getText().toString());
                        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        dane.add(currentDate);
                        statement.executeUpdate("INSERT INTO usertest (login, password,mail,name,surname,date) VALUES ('" + dane.get(0) + "', '" + dane.get(1) + "','" + dane.get(2) + "','" + dane.get(3) + "','" + dane.get(4) + "','" + dane.get(5) + "');");
                    } else {
                        error = "Zaakceptuj regulamin!";
                    }
                }
            } catch (Exception e) {
                error = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                connection.close();
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (error != "")
                Toasty.warning(getApplicationContext(), error, Toast.LENGTH_LONG, true).show();
            else {
                Toasty.success(getApplicationContext(), "Gratulacje !!! Rejestracja pomyślna.", Toast.LENGTH_LONG, true).show();
                startActivity(i_main);
            }
        }
    }
}