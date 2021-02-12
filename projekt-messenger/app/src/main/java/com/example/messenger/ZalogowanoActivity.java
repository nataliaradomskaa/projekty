package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.w3c.dom.ls.LSOutput;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import es.dmoral.toasty.Toasty;

public class ZalogowanoActivity extends AppCompatActivity {

    public DrawerLayout mDrawer;
    public NavigationView nvDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence toolbarName;
    SessionManager sessionManager;
    ImageView iv;
    FragmentManager fragmentManager;
    TextView tv_podpis;
    MenuItem aktualneMenuBoczne;
    boolean wroc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zalogowano);
        wroc=false;
        sessionManager = new SessionManager(this);
        fragmentManager = getSupportFragmentManager();
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        try {
            fragmentManager.beginTransaction().replace(R.id.frameLayout, AllUsersFragment.class.newInstance()).commit();

            getSupportActionBar().setTitle("Użytkownicy");
        } catch (Exception e) {
            e.printStackTrace();
        }



        if (savedInstanceState != null)
            getSupportActionBar().setTitle(savedInstanceState.getCharSequence("toolbarName"));
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        nvDrawer = (NavigationView) findViewById(R.id.navigationView);
        setNavigationListener(nvDrawer);

        drawerToggle = setupDrawerToggle();
        drawerToggle.syncState();

        //animacja  hamburgerka
        mDrawer.addDrawerListener(drawerToggle);

        //ustawienie avatara
        iv = nvDrawer.getHeaderView(0).findViewById(R.id.avatar);
        Glide.with(this).load("http://messengeruwb.xaa.pl/avatary/" + sessionManager.preferences.getString("KEY_AVATAR", "default.jpg"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(iv);

        tv_podpis = (TextView) nvDrawer.getHeaderView(0).findViewById(R.id.tv_podpis);
        tv_podpis.setText(sessionManager.preferences.getString("KEY_IMIE", "IMIĘ") + " " + sessionManager.preferences.getString("KEY_NAZWISKO", "NAZWISKO"));

        if (sessionManager.loadNightModeState() == true) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);

        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_view, menu);
        return true;
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.toolbar_first_fragment:
                fragmentClass = ONasFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
                    aktualneMenuBoczne.setChecked(false);
                    getSupportActionBar().setTitle("Informacje");
                    wroc=true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                break;
        }

        return true;
    }


    public void onBackPressed() {
        if(wroc) {
            mDrawer.closeDrawers();
            Fragment fragment;
            Class fragmentClass;
            switch (aktualneMenuBoczne.getItemId()) {
                case R.id.nav_first_fragment:
                    fragmentClass = AllUsersFragment.class;
                    break;
                case R.id.nav_second_fragment:
                    fragmentClass = AllUsersFragment.class;
                    break;
                case R.id.nav_third_fragment:
                    fragmentClass = AllUsersFragment.class;
                    break;
                default:
                    fragmentClass = AllUsersFragment.class;
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
                aktualneMenuBoczne.setChecked(true);
                getSupportActionBar().setTitle(aktualneMenuBoczne.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            wroc=false;
        }
        else
            mDrawer.closeDrawers();

    }

    private void setNavigationListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        if(wroc) {wroc=!wroc;}
        Fragment fragment = null;
        Class fragmentClass=null;
        aktualneMenuBoczne=menuItem;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = AllUsersFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = ONasFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = MotywFragment.class;
                break;
            case R.id.nav_fifth_fragment:
                fragmentClass = UstawieniaFragment.class;
                break;
            case R.id.nav_fourth_fragment:
                fragmentClass = null;
                Toasty.info(this, "Wylogowano Pomyślnie !", Toasty.LENGTH_SHORT).show();
                sessionManager.logoutUser();
                break;
            default:
                fragmentClass = AllUsersFragment.class;
        }

        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //ustawienie fragmentu
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
            menuItem.setChecked(true);
            toolbarName = menuItem.getTitle();
            getSupportActionBar().setTitle(menuItem.getTitle());
            mDrawer.closeDrawers();
        }
        else
            mDrawer.closeDrawers();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("toolbarName", toolbarName);
    }

    // onClick imageView
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @SuppressLint("CheckResult")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toasty.warning(getApplicationContext(), "Problem z odczytaniem danych!", Toasty.LENGTH_LONG, true);
            } else {
                new UploadZdjecia().execute(data.getData());
            }
        }
    }

    private class UploadZdjecia extends AsyncTask<Uri, Void, Boolean> {
        InputStream zdjecie,zdjecieFTP;

        Connection connection;
        Statement statement;
        String ftp_host, ftp_login, ftp_haslo, ftp_path, ftp_rozszerzenie, login;
        FTPClient ftp;
        Boolean ftpok;
        @Override
        protected Boolean doInBackground(Uri... inputStreams) {

            try {
                zdjecie= getApplicationContext().getContentResolver().openInputStream(inputStreams[0]);
                zdjecieFTP= getApplicationContext().getContentResolver().openInputStream(inputStreams[0]);
                ftp_host = "messengeruwb.xaa.pl";
                ftp_login = "p562436";
                ftp_haslo = "h2riOY8Kf";
                ftp_path = "public_html/avatary/";
                ftp_rozszerzenie = ".jpg";
                login = sessionManager.preferences.getString("KEY_LOGIN", "default.jpg");
                ftp = new FTPClient();
                ftp.connect(ftp_host);
                ftp.login(ftp_login, ftp_haslo);
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                ftp.storeFile(ftp_path + login + ftp_rozszerzenie, zdjecieFTP);
                ftp.logout();
                ftp.disconnect();
                ftpok = true;
            } catch (Exception e) {
                e.printStackTrace();
                ftpok = false;
            }
            if (ftpok) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://messengeruwb.xaa.pl/p562436_messenger?useSSL=false", "p562436", "h2riOY8Kf");
                    statement = connection.createStatement();
                    String login = sessionManager.preferences.getString("KEY_LOGIN", "default.jpg");
                    statement.executeUpdate("UPDATE usertest SET avatar='" + login + ".jpg" + "' WHERE login='" + login + "';");
                    sessionManager.editor.putString("KEY_AVATAR",login+".jpg");
                    sessionManager.editor.commit();
                    statement.close();
                    connection.close();
                    return true;
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean wynik) {
            super.onPostExecute(wynik);
            if (wynik) {
                iv.setImageBitmap(BitmapFactory.decodeStream(zdjecie));
                Toasty.success(getApplicationContext(), "Zmiana avatara pomyślna.", Toasty.LENGTH_LONG, true).show();
            } else {
                Toasty.error(getApplicationContext(), "Wystąpił błąd przy załadowaniu obrazka!", Toasty.LENGTH_LONG, true).show();
            }
        }
    }
}