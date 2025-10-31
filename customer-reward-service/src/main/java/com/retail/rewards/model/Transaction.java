package com.retail.rewards.model;

import java.time.LocalDate;

public class Transaction {
    private LocalDate date;
    private double amount;

    public Transaction(LocalDate date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public LocalDate getDate() { return date; }
    public double getAmount() { return amount; }
}
