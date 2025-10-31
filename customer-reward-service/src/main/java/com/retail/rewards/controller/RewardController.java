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

    /**
     * Retrieves the reward summaries for all customers.
     *
     * @return a list of {@link RewardSummary} objects containing each customer's total and monthly rewards
     */
    @GetMapping
    public List<RewardSummary> getAllRewards() {
        return rewardService.getAllCustomerRewards();
    }

    /**
     * Retrieves the reward summary for a specific customer
     * @param customerId the unique ID of the customer
     * @return the {@link RewardSummary} containing the customer's total and monthly rewards
     * @throws CustomerNotFoundException if no customer is found for the given ID
     */
    @GetMapping("/{customerId}")
    public RewardSummary getRewardsByCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {
        return rewardService.getRewardsByCustomer(customerId);
    }

    /**
     * Retrieves the reward summary for a specific customer within a given date range.
     *
     * @param customerId the unique ID of the customer
     * @param start the start date of the period in ISO format (yyyy-MM-dd)
     * @param end the end date of the period in ISO format (yyyy-MM-dd)
     * @return the {@link RewardSummary} containing the customer's rewards for the specified period
     * @throws CustomerNotFoundException if no customer is found for the given ID
     */
    @GetMapping("/period")
    public RewardSummary getRewardsForCustomerForPeriod(@RequestParam Long customerId,
                                                   @RequestParam String start, @RequestParam String end)
            throws CustomerNotFoundException {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return rewardService.getRewardsForCustomerForPeriod(customerId, startDate, endDate);
    }
}
