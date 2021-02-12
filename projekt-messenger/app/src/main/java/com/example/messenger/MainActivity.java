package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    Button btn_rejestracja,btn_logowanie;
    Intent i_rejestracja, i_logowanie, i_zalogowano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager=new SessionManager(this);
        btn_rejestracja=findViewById(R.id.button2);
        btn_logowanie=findViewById(R.id.button);
        i_rejestracja = new Intent(this, RejestracjaActivity.class);
        i_logowanie = new Intent(this, LogowanieActivity.class);
        i_zalogowano = new Intent(this, ZalogowanoActivity.class);
        if(sessionManager.preferences.getBoolean("KEY_ISLOGIN",false)){
            startActivity(i_zalogowano);
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button2:
                startActivity(i_rejestracja);
                break;
            case R.id.button:
                startActivity(i_logowanie);
                break;
        }
    }
}