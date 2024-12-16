package com.voggella.android.doan.Database_Adapter;

public class UserModel {
    private String phone;
    private String name;
    private String password;
    private boolean isVip;

    public UserModel(String phone, String name, String password) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.isVip = false;
    }

    // Getters
    public String getPhone() { return phone; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public boolean isVip() { return isVip; }

    // Setters
    public void setPhone(String phone) { this.phone = phone; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setVip(boolean vip) { isVip = vip; }
}