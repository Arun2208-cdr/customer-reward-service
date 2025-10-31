package com.retail.rewards.controller;

import com.retail.rewards.exception.CustomerNotFoundException;
import com.retail.rewards.model.RewardSummary;
import com.retail.rewards.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping
    public List<RewardSummary> getAllRewards() {
        return rewardService.getAllCustomerRewards();
    }

    @GetMapping("/{customerId}")
    public RewardSummary getRewardsByCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {
        return rewardService.getRewardsByCustomer(customerId);
    }

    @GetMapping("/period")
    public RewardSummary getRewardsForCustomerForPeriod(@RequestParam Long customerId,
                                                   @RequestParam String start, @RequestParam String end)
            throws CustomerNotFoundException {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return rewardService.getRewardsForCustomerForPeriod(customerId, startDate, endDate);
    }
}
