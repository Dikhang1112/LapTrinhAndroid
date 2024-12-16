// File: model/Transaction.java
package com.voggella.android.doan.Database_Adapter;

public class Transaction {
    private String type;
    private double amount;
    private String date;

    // Constructor
    public Transaction(String type, double amount, String date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }
    // Getter và Setter
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
