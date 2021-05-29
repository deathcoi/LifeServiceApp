package com.example.lifeservice;

public class User {

    public String fullname,email,username, pass,phone;

    public User(){

    }
    public User(String fullname,String email, String phone, String pass, String username){
        this.fullname = fullname;
        this.email = email;
        this.phone =phone;
        this.pass = pass;
        this.username = username;
    }
}
