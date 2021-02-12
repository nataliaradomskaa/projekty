package com.example.messenger;

public class Osoba {

    int id;
    String login,imie, nazwisko,avatar;

    Osoba( int id, String login, String imie, String nazwisko, String avatar){
        this.id = id;
        this.login = login;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.avatar = avatar;
    }

    public String toString(){
        return "Id: "+id+" login: "+login+" imię: "+imie+" nazwisko: "+nazwisko;
    }
}
