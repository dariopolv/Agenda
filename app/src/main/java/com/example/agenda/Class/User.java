package com.example.agenda.Class;

public class User {
    String name;
    String surname;
    String email;
    String agenda;

    public User(String name, String surname, String email, String agenda) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.agenda = "";



    }

    public User() {}

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public void setName(String name) {
        this.name = name;
    }
}
