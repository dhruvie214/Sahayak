package com.example.sahayakapp;

public class Volunteer {
    private String name;
    private String address;
    private String phone;
    private String skills;

    public Volunteer(String name, String address, String phone, String skills) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getSkills() {
        return skills;
    }
}
