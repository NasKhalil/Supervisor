package com.k2thend.supervisor.model;

public class User {
  private String uid;
  private String name;
  private String mail;
  private String pwd;

    public User() {
    }

    public User(String uid, String name, String mail, String pwd) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.pwd = pwd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}