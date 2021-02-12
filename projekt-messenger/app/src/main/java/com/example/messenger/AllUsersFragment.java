package com.example.messenger;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AllUsersFragment extends Fragment {
    SessionManager sessionManager;
    ArrayList<Osoba> osoby;
    RecyclerView person_recycler;
    Context context;
    Connection connection;
    Statement statement;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_users, container, false);
        context=v.getContext();
        sessionManager = new SessionManager(context);
        person_recycler = (RecyclerView) v.findViewById(R.id.recyclerView);
        person_recycler.setLayoutManager(new LinearLayoutManager(context));
        osoby=new ArrayList<>();
        person_recycler.setAdapter(new Adapter_person(osoby));
        new AllUsersFragment.RefreshUsers().execute();
        return v;
    }

    class RefreshUsers extends AsyncTask<Void, Void, Void> {
        String error="", login, imie, nazwisko, avatar;
        int id;
        @Override
        protected final Void doInBackground(Void ...voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://messengeruwb.xaa.pl/p562436_messenger?useSSL=false","p562436", "h2riOY8Kf");
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT id, login, name, surname, avatar FROM usertest;");
                if(!resultSet.next()){
                    error="Brak osob w bazie";
                }
                while(resultSet.next()) {
                    id= resultSet.getInt(1);
                    if(sessionManager.preferences.getInt("KEY_ID",-1)==id)
                        continue;
                    login=resultSet.getString(2);
                    imie = resultSet.getString(3);
                    nazwisko = resultSet.getString(4);
                    avatar = resultSet.getString(5);
                    osoby.add(new Osoba(id, login, imie, nazwisko, avatar));
                }
            }
            catch(Exception e)
            {
                error = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                connection.close();
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            person_recycler.setAdapter(new Adapter_person(osoby));
        }
    }
}