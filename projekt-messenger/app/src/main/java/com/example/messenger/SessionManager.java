package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.HashMap;

public class SessionManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    String login;
    Boolean stan;


    public SessionManager(Context context){
        this.context=context;
        preferences = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public void createLoginSession(int id, String login,String imie, String nazwisko, String avatar){
        editor.putBoolean("KEY_ISLOGIN",true);
        editor.putInt("KEY_ID", id);
        editor.putString("KEY_LOGIN", login);
        editor.putString("KEY_IMIE",imie);
        editor.putString("KEY_NAZWISKO",nazwisko);
        editor.putString("KEY_AVATAR",avatar);
        editor.putBoolean("KEY_DARK", false);
        editor.commit();
    }

    /*public int pobierzId() {
        id = preferences.getInt("KEY_ID", 0);
        return id;
    } */

    public String pobierzLogin() { // login biezacego uzytkownika do update'u hasla
        login = preferences.getString("KEY_LOGIN", "");
        return login;
    }

    public Boolean loadNightModeState () {
        stan = preferences.getBoolean("KEY_DARK", false);
        return stan;
    }

    public void setNightModeState(Boolean stan) {
        editor.putBoolean("KEY_DARK", stan);
        editor.commit();
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i =new Intent(context,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}