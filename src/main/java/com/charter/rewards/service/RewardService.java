package com.charter.rewards.service;

import com.charter.rewards.exception.CustomerNotFoundException;
import com.charter.rewards.model.Customer;
import com.charter.rewards.model.RewardSummary;
import com.charter.rewards.model.Transaction;
import com.charter.rewards.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

import java.util.HashMap;
import java.util.Map;

@Service
public class RewardService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Calculates the reward summary for a specific customer within a given date range.
     * This method retrieves the customer by ID, filters their transactions that fall within
     * the specified start and end dates, calculates reward points for each qualifying transaction,
     * and aggregates the results by month and overall total.
     *
     * @param customerId the unique identifier of the customer whose rewards need to be calculated
     * @param start the start date of the reward calculation period (inclusive)
     * @param end the end date of the reward calculation period (inclusive)
     * @return a {@link RewardSummary} containing the customer's total reward points and a breakdown per month
     * @throws CustomerNotFoundException if no customer exists for the provided ID
     */
    public RewardSummary getRewardsForCustomerForPeriod(Long customerId, LocalDate start, LocalDate end)
            throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found. Customer Id: " + customerId));

        Map<String, Integer> monthlyRewards = new HashMap<>();
        int total = 0;

        for (Transaction t : customer.getTransactions()) {
            if (!t.getDate().isBefore(start) && !t.getDate().isAfter(end)) {
                int points = calculatePoints(t.getAmount());
                total += points;
                String month = YearMonth.from(t.getDate()).toString();
                monthlyRewards.merge(month, points, Integer::sum);
            }
        }

        return new RewardSummary(customer.getId(), customer.getName(),
                customer.getPhone(), customer.getCity(), total, monthlyRewards);
    }

    /**
     * Calculates reward points for a given transaction amount based on defined thresholds.
     *
     * @param amount the transaction amount
     * @return calculated reward points
     */
    private int calculatePoints(double amount) {
        if (amount <= 50)
            return 0;
        if (amount <= 100)
            return (int) (amount - 50);
        return (int) ((amount - 100) * 2 + 50);
    }
}
