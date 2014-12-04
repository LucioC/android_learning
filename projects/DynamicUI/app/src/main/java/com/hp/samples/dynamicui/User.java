package com.hp.samples.dynamicui;

public class User {
    private String user;
    private String number;
    private String company;
    private String address;

    public User(String name, String number, String company, String address) {
        this.user = name;
        this.number = number;
        this.company = company;
        this.address = address;
    }

    public String getUser() {
        return user;
    }

    public String getNumber() {
        return number;
    }

    public String getCompany() {
        return company;
    }

    public String getAddress() {
        return address;
    }
}
