package com.retail.rewards.controller;

import com.retail.rewards.model.RewardSummary;
import com.retail.rewards.service.RewardService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping
    public List<RewardSummary> getAllRewards() {
        return rewardService.getAllCustomerRewards();
    }

    @GetMapping("/{customerId}")
    public RewardSummary getRewardsByCustomer(@PathVariable Long customerId) {
        return rewardService.getRewardsByCustomer(customerId);
    }

    @GetMapping("/period")
    public List<RewardSummary> getRewardsForPeriod(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return rewardService.getRewardsForPeriod(startDate, endDate);
    }
}
