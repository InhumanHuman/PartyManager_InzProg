package com.example.partymanager_inzprog;

public class MessagesData {
    private Integer id_wiadomosci;
    private String temat;
    private String tresc_wiadomosci;
    private Integer user_id;
    private String firstname;
    private String lastname;
    private String e_mail;
    private Integer id_number;

    public MessagesData(Integer id_wiadomosci, String temat, String tresc_wiadomosci, Integer user_id, String firstname, String lastname, String e_mail, Integer id_number) {
        this.id_wiadomosci = id_wiadomosci;
        this.temat = temat;
        this.tresc_wiadomosci = tresc_wiadomosci;
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.e_mail = e_mail;
        this.id_number = id_number;
    }

    public Integer getId_wiadomosci() {
        return id_wiadomosci;
    }
    public String getTemat() {
        return temat;
    }
    public String getTresc_wiadomosci() {
        return tresc_wiadomosci;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getE_mail() {
        return e_mail;
    }
    public Integer getId_number() {
        return id_number;
    }
}
