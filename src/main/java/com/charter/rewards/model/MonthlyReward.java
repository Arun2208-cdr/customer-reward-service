package com.charter.rewards.model;

public class MonthlyReward {

    private String month;
    private Integer rewardPoints;

    public MonthlyReward() {
    }

    public MonthlyReward(String month, Integer rewardPoints) {
        this.month = month;
        this.rewardPoints = rewardPoints;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
