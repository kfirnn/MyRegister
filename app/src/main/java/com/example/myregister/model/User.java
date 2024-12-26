package com.example.myregister.model;

public class User {
    String id;
    String fName,lName, phone, email, password,city;

    public User() {
    }

    public User(String id, String fName, String lName, String phone, String email, String password, String city) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public User(String id, String fName, String lName, String phone, String email) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.email = email;

    }

    protected String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected String getfName() {
        return fName;
    }

    protected void setfName(String fName) {
        this.fName = fName;
    }

    protected String getlName() {
        return lName;
    }

    protected void setlName(String lName) {
        this.lName = lName;
    }

    protected String getPhone() {
        return phone;
    }

    protected void setPhone(String phone) {
        this.phone = phone;
    }

    protected String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
