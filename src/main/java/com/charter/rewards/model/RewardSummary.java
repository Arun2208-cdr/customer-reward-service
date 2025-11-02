package com.charter.rewards.model;

import java.util.Map;

public class RewardSummary {
    private Long customerId;
    private String name;
    private String phone;
    private String city;
    private int totalRewards;
    private Map<String, Integer> monthlyRewards;

    public RewardSummary(Long customerId, String name, String phone, String city, int totalRewards, Map<String, Integer> monthlyRewards) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.totalRewards = totalRewards;
        this.monthlyRewards = monthlyRewards;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public int getTotalRewards() {
        return totalRewards;
    }

    public void setTotalRewards(int totalRewards) {
        this.totalRewards = totalRewards;
    }

    public Map<String, Integer> getMonthlyRewards() {
        return monthlyRewards;
    }

    public void setMonthlyRewards(Map<String, Integer> monthlyRewards) {
        this.monthlyRewards = monthlyRewards;
    }
}
