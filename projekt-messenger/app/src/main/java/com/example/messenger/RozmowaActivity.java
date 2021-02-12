package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;

public class RozmowaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> parametry;
    private Intent i_back;
    private SessionManager sessionManager;
    private EditText et_wiadomosc;
    private String do_kogo;
    private RecyclerView wiadomosci_recycler;
    private int maxDisplayWidth;
    Boolean scrollingToBottom;
    Thread odswiezanie;
    boolean pierwszyRaz;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rozmowa);
        utworzRecycleView();
        ustawListenerKlawiatury();
        ustawToolbar();
        sessionManager = new SessionManager(getApplicationContext());
        et_wiadomosc = findViewById(R.id.editTextTextPersonName3);
        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(bundle.getCharSequence("imienazwisko"));
        do_kogo = bundle.getString("id");
        pierwszyRaz=true;
        uruchomOdswiezanie();
    }

    public void ustawToolbar(){
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_bar_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odswiezanie.interrupt();
                onBackPressed();
            }
        });
    };

    public void ustawListenerKlawiatury(){
        //po otwarciu klawiatury zescroluj na dol recycle view, troche janusz ale dziala
        wiadomosci_recycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                wiadomosci_recycler.getWindowVisibleDisplayFrame(r);
                int screenHeight = wiadomosci_recycler.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    // keyboard is opened
                    if (!scrollingToBottom) {
                        scrollingToBottom = true;
                        wiadomosci_recycler.scrollToPosition(wiadomosci_recycler.getAdapter().getItemCount() - 1);
                    }
                } else {
                    // keyboard is closed
                    scrollingToBottom = false;
                }
            }
        });
    }

    public void utworzRecycleView(){
        //display size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        maxDisplayWidth = (int) (displayMetrics.widthPixels * 0.8);
        //stworzenie pustego recyclerview przed aktualizacja
        wiadomosci_recycler = findViewById(R.id.recyclerView);
        wiadomosci_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        wiadomosci_recycler.setAdapter(new Adapter_wiadomosc(new ArrayList<String>(), new ArrayList<String>(), "", maxDisplayWidth));
    }


    public void onClick(View v) {
        if(et_wiadomosc.getText().toString().matches("")) {
            Toasty.info(this, "Puste pole !", Toast.LENGTH_SHORT).show();
        }
        else {
            parametry.clear();
            parametry.add(et_wiadomosc.getText().toString());
            parametry.add(do_kogo);
            parametry.add(String.valueOf(sessionManager.preferences.getInt("KEY_ID", -1)));
            et_wiadomosc.setText("");
            new Wyslijwiadomosc().execute(parametry);
        }

    }



    public void onBackPressed() {
        super.onBackPressed();
        odswiezanie.interrupt();
    }


    class Wyslijwiadomosc extends AsyncTask<ArrayList<String>, Integer, Boolean> {
        String error = "", wiadomosc, od_kogo, do_kogo;
        ArrayList<String> wiadomosci = new ArrayList<>();
        ArrayList<String> id_od_kogo = new ArrayList<>();
        Connection connection;
        Statement statement;

        protected final Boolean doInBackground(ArrayList<String>... param) {
            try {
                ArrayList<String> dane = param[0];
                wiadomosc = dane.get(0);
                do_kogo = dane.get(1);
                od_kogo = dane.get(2);
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://messengeruwb.xaa.pl/p562436_messenger?useSSL=false", "p562436", "h2riOY8Kf");
                statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO wiadomosci (od, do, wiadomosc) VALUES ('" + od_kogo + "','" + do_kogo + "','" + wiadomosc + "');");
                ResultSet resultSet = statement.executeQuery("SELECT wiadomosc,od FROM wiadomosci where od=" + od_kogo + " AND do=" + do_kogo + " OR od=" + do_kogo + " AND do=" + od_kogo + ";");

                while (resultSet.next()) {

                    wiadomosci.add(resultSet.getString(1));
                    id_od_kogo.add(String.valueOf(resultSet.getInt(2)));
                }
                return true;
            } catch (Exception e) {
                error = e.getMessage();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean pass) {
            super.onPostExecute(pass);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toasty.Config.getInstance().setTextSize(15).apply();
            try {
                connection.close();
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (!pass) {
                Toasty.error(getApplicationContext(), "Błąd wysyłania wiadomosci: " + error, Toast.LENGTH_LONG, true).show();
            } else {
                wiadomosci_recycler.setAdapter(new Adapter_wiadomosc(wiadomosci, id_od_kogo, od_kogo, maxDisplayWidth));
                wiadomosci_recycler.scrollToPosition(wiadomosci.size() - 1);
            }
        }
    }

    public void uruchomOdswiezanie(){
        odswiezanie = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        parametry = new ArrayList<>();
                        parametry.clear();
                        parametry.add(et_wiadomosc.getText().toString());
                        parametry.add(do_kogo);
                        parametry.add(String.valueOf(sessionManager.preferences.getInt("KEY_ID", -1)));
                        new RefreshWiadomosc().execute(parametry);
                        Thread.sleep(1000);

                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        odswiezanie.start();
    }

    class RefreshWiadomosc extends AsyncTask<ArrayList<String>, Integer, Boolean> {
        String error = "", od_kogo, do_kogo;
        ArrayList<String> wiadomosci = new ArrayList<>();
        ArrayList<String> id_od_kogo = new ArrayList<>();
        Connection connection;
        Statement statement;
        private Parcelable recyclerViewState;

        @Override
        protected final Boolean doInBackground(ArrayList<String>... param) {
            try {
                ArrayList<String> dane = param[0];
                do_kogo = dane.get(1);
                od_kogo = dane.get(2);

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://messengeruwb.xaa.pl/p562436_messenger?useSSL=false", "p562436", "h2riOY8Kf");
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT wiadomosc,od FROM wiadomosci where od=" + od_kogo + " AND do=" + do_kogo + " OR od=" + do_kogo + " AND do=" + od_kogo + ";");


                while (resultSet.next()) {
                    wiadomosci.add(resultSet.getString(1));
                    id_od_kogo.add(String.valueOf(resultSet.getInt(2)));
                }
                return true;
            } catch (Exception e) {
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
            if (pass == false)
                Toasty.error(getApplicationContext(), "Błąd odświeżania: " + error, Toast.LENGTH_LONG, true).show();
            else {
                LinearLayoutManager myLayoutManager =  (LinearLayoutManager) wiadomosci_recycler.getLayoutManager();
                System.out.println();
                recyclerViewState = wiadomosci_recycler.getLayoutManager().onSaveInstanceState();
                if(pierwszyRaz){
                    wiadomosci_recycler.setAdapter(new Adapter_wiadomosc(wiadomosci, id_od_kogo, od_kogo, maxDisplayWidth));
                    wiadomosci_recycler.scrollToPosition(wiadomosci.size() - 1);
                    pierwszyRaz=false;
                }
                else if(myLayoutManager.findLastVisibleItemPosition()==wiadomosci.size()-1 || myLayoutManager.findLastVisibleItemPosition()==wiadomosci.size()-2) {
                    wiadomosci_recycler.setAdapter(new Adapter_wiadomosc(wiadomosci, id_od_kogo, od_kogo, maxDisplayWidth));
                    wiadomosci_recycler.scrollToPosition(wiadomosci.size() - 1);
                }
                else{
                    wiadomosci_recycler.setAdapter(new Adapter_wiadomosc(wiadomosci, id_od_kogo, od_kogo, maxDisplayWidth));
                    wiadomosci_recycler.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                }
            }
        }
    }
}