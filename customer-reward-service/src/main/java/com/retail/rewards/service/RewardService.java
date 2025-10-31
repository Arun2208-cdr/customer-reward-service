package com.retail.rewards.service;

import com.retail.rewards.data.StaticData;
import com.retail.rewards.exception.CustomerNotFoundException;
import com.retail.rewards.model.Customer;
import com.retail.rewards.model.RewardSummary;
import com.retail.rewards.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private static final int THRESHOLD_50 = 50;
    private static final int THRESHOLD_100 = 100;

    /**
     * Calculates reward points for a given transaction amount based on defined thresholds.
     *
     * @param amount the transaction amount
     * @return calculated reward points
     */
    private int calculatePoints(double amount) {
        if (amount <= THRESHOLD_50)
            return 0;
        if (amount <= THRESHOLD_100)
            return (int) (amount - THRESHOLD_50);

        return (int) ((amount - THRESHOLD_100) * 2 + 50);
    }

    /**
     * Retrieves reward summaries for all customers.
     *
     * @return list of {@link RewardSummary} for all customers
     */
    public List<RewardSummary> getAllCustomerRewards() {
        return StaticData.getCustomers().stream()
                .map(this::calculateCustomerRewards)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the reward summary for a specific customer.
     *
     * @param customerId the unique identifier of the customer
     * @return {@link RewardSummary} containing total and monthly reward points
     * @throws CustomerNotFoundException if the customer ID is invalid
     */
    public RewardSummary getRewardsByCustomer(Long customerId) throws CustomerNotFoundException {
        return StaticData.getCustomers().stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .map(this::calculateCustomerRewards)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
    }

    /**
     * Retrieves the reward summary for a specific customer within a given date range.
     *
     * @param customerId the unique identifier of the customer
     * @param start start date of the period
     * @param end end date of the period
     * @return {@link RewardSummary} containing total and monthly reward points for the given period
     * @throws CustomerNotFoundException if the customer ID is invalid
     */
    public RewardSummary getRewardsForCustomerForPeriod(Long customerId, LocalDate start, LocalDate end)
            throws CustomerNotFoundException {
        return StaticData.getCustomers().stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .map(c -> calculateCustomerRewards(c, start, end))
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
    }

    /**
     * Calculates total and monthly reward points for a customer for the default year (2025).
     *
     * @param customer the customer entity
     * @return {@link RewardSummary} containing total and monthly reward points
     */
    private RewardSummary calculateCustomerRewards(Customer customer) {
        return calculateCustomerRewards(customer, LocalDate.of(2025,1,1),
                LocalDate.of(2025,12,31));
    }

    /**
     * Calculates total and monthly reward points for a customer within the specified date range.
     *
     * @param customer the customer entity
     * @param start start date of the period
     * @param end end date of the period
     * @return {@link RewardSummary} containing total and monthly reward points for the given period
     */
    private RewardSummary calculateCustomerRewards(Customer customer, LocalDate start, LocalDate end) {
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
        return new RewardSummary(customer.getId(), customer.getName(), customer.getPhone(),
                customer.getCity(), total, monthlyRewards);
    }
}
