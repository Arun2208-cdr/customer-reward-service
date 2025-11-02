package com.charter.rewards.controller;

import com.charter.rewards.exception.CustomerNotFoundException;
import com.charter.rewards.model.RewardSummary;
import com.charter.rewards.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    /**
     * Retrieves reward summary for a specific customer within an optional date range.
     * Customer ID is mandatory — returns error if missing.
     * If start and end dates are not provided → calculates for the last 3 months.
     *
     * @param customerId the unique ID of the customer (required)
     * @param start optional start date (yyyy-MM-dd)
     * @param end optional end date (yyyy-MM-dd)
     * @return the {@link RewardSummary} containing total and monthly rewards
     * @throws CustomerNotFoundException if no customer is found for the given ID
     */
    @GetMapping
    public RewardSummary getRewardsForCustomer(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end
    ) throws CustomerNotFoundException {

        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must be provided to fetch rewards");
        }

        LocalDate endDate = (end != null) ? LocalDate.parse(end) : LocalDate.now();
        LocalDate startDate = (start != null) ? LocalDate.parse(start) : endDate.minusMonths(3);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return rewardService.getRewardsForCustomerForPeriod(customerId, startDate, endDate);
    }
}
