package com.example.partymanager_inzprog;

public class UserData {
    private Integer id_uzytkownika;
    private String numer_dowodu;
    private Integer numer_telefonu;
    private String imie;
    private String nazwisko;
    private String email;
    private String login;
    private String status;

    public UserData(Integer id_uzytkownika, String numer_dowodu, Integer numer_telefonu, String imie, String nazwisko, String email, String login, String status) {
        this.id_uzytkownika = id_uzytkownika;
        this.numer_dowodu = numer_dowodu;
        this.numer_telefonu = numer_telefonu;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.login = login;
        this.status = status;
    }

    public Integer getId_uzytkownika() {
        return id_uzytkownika;
    }

    public String getNumer_dowodu() {
        return numer_dowodu;
    }

    public Integer getNumer_telefonu() {
        return numer_telefonu;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getStatus() {
        return status;
    }
}