package com.retail.rewards.model;

import java.util.List;

public class Customer {
    private Long id;
    private String name;
    private String phone;
    private String city;
    private List<Transaction> transactions;

    public Customer(){}

    public Customer(Long id, String name, String phone, String city, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.transactions = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
