package com.example.messenger;

import android.os.Bundle;
import android.view.View;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.Context;
import android.annotation.SuppressLint;
import android.view.Gravity;
import android.widget.Toast;
import java.util.Calendar;

public class ONasFragment extends Fragment {

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_o_nas, container, false);
        context = v.getContext();

        View aboutPage = new AboutPage(context)
                .isRTL(false)
                .setDescription("Jesteśmy młodym, prężnie rozwijającym się zespołem, zdobywającym nowe umiejętności, " +
                        "takie jak projektowanie aplikacji mobilnych na Androida, współpraca oraz obsługa Gita, " +
                        "by w przyszłości znaleźć dobrą pracę, a tymczasem zaliczyć przedmiot Zaawansowane programowanie obiektowe w Javie :-)")
                //.addItem(new Element().setTitle("Wersja 1.0"))
                .addPlayStore("com.example.messenger")
                .addGitHub("76028/Messenger")
                .addFacebook("facebook")
                .addEmail("test@test.com")
                .addItem(createCopyright())
                .create();
        return aboutPage;
    }



    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d Projekt Messenger",
                Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}