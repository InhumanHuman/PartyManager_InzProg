package com.example.partymanager_inzprog;

public class UserData {
    private Integer user_id;
    private String firstname;
    private String lastname;
    private String e_mail;
    private String id_number;
    private String login;
    private String password;
    private String status;

    public UserData(Integer user_id, String firstname, String lastname, String e_mail, String id_number, String login, String password, String status) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.e_mail = e_mail;
        this.id_number = id_number;
        this.login = login;
        this.password = password;
        this.status = status;
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
    public String getId_number() {
        return id_number;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public String getStatus() {
        return status;
    }
}