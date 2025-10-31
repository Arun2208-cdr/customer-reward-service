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

    private int calculatePoints(double amount) {
        if (amount <= THRESHOLD_50)
            return 0;
        if (amount <= THRESHOLD_100)
            return (int) (amount - THRESHOLD_50);

        return (int) ((amount - THRESHOLD_100) * 2 + 50);
    }

    public List<RewardSummary> getAllCustomerRewards() {
        return StaticData.getCustomers().stream()
                .map(this::calculateCustomerRewards)
                .collect(Collectors.toList());
    }

    public RewardSummary getRewardsByCustomer(Long customerId) throws CustomerNotFoundException {
        return StaticData.getCustomers().stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .map(this::calculateCustomerRewards)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
    }

    public RewardSummary getRewardsForCustomerForPeriod(Long customerId, LocalDate start, LocalDate end)
            throws CustomerNotFoundException {
        return StaticData.getCustomers().stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .map(c -> calculateCustomerRewards(c, start, end))
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
    }

    private RewardSummary calculateCustomerRewards(Customer customer) {
        return calculateCustomerRewards(customer, LocalDate.of(2025,1,1),
                LocalDate.of(2025,12,31));
    }

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
