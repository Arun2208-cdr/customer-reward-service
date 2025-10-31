package com.retail.rewards.service;
import com.retail.rewards.data.StaticData;
import com.retail.rewards.exception.CustomerNotFoundException;
import com.retail.rewards.model.Customer;
import com.retail.rewards.model.RewardSummary;
import com.retail.rewards.model.Transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class RewardServiceTest {

    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        rewardService = new RewardService();
    }

    private Customer createCustomer(Long id, String name, List<Transaction> txns) {
        Customer c = new Customer();
        c.setId(id);
        c.setName(name);
        c.setPhone("9876543210");
        c.setCity("Chennai");
        c.setTransactions(txns);
        return c;
    }

    private Transaction txn(LocalDate date, double amount) {
        return new Transaction(date, amount);
    }

    //  Test 1: verify reward points calculation for a single customer
    @Test
    void testGetRewardsByCustomer_CalculatesPointsCorrectly() throws Exception{
        Customer customer = createCustomer(1L, "Kumar",
                Arrays.asList(
                        txn(LocalDate.of(2025, 1, 10), 40),   // 0 pts
                        txn(LocalDate.of(2025, 2, 15), 75),   // 25 pts
                        txn(LocalDate.of(2025, 3, 20), 120)   // 90 pts
                ));

        try (MockedStatic<StaticData> mockedStatic = mockStatic(StaticData.class)) {
            mockedStatic.when(StaticData::getCustomers).thenReturn(Collections.singletonList(customer));

            RewardSummary summary = rewardService.getRewardsByCustomer(1L);

            assertEquals(115, summary.getTotalRewards(), "Total points should be 115");
            assertFalse(summary.getMonthlyRewards().isEmpty());
            assertEquals("Kumar", summary.getName());
        }
    }

    //  Test 2: exception when customer not found
    @Test
    void testGetRewardsByCustomer_NotFoundThrowsException() {
        try (MockedStatic<StaticData> mockedStatic = mockStatic(StaticData.class)) {
            mockedStatic.when(StaticData::getCustomers).thenReturn(Collections.emptyList());

            assertThrows(CustomerNotFoundException.class,
                    () -> rewardService.getRewardsByCustomer(999L),
                    "Should throw exception when customer not found");
        }
    }

    //  Test 3: reward calculation for a date range (period)
    @Test
    void testGetRewardsForCustomerForPeriod_FiltersTransactionsByDate() throws Exception{
        Customer customer = createCustomer(2L, "Kumar",
                Arrays.asList(
                        txn(LocalDate.of(2025, 1, 10), 120), // inside range
                        txn(LocalDate.of(2025, 6, 10), 90),  // inside range
                        txn(LocalDate.of(2024, 12, 10), 150) // outside range
                ));

        try (MockedStatic<StaticData> mockedStatic = mockStatic(StaticData.class)) {
            mockedStatic.when(StaticData::getCustomers).thenReturn(Collections.singletonList(customer));

            RewardSummary summary = rewardService.getRewardsForCustomerForPeriod(
                    2L,
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 6, 30));

            assertEquals(90+40, summary.getTotalRewards(), "Should only include 2025 transactions");
            assertTrue(summary.getMonthlyRewards().containsKey("2025-01"));
        }
    }

    //  Test 4: verify getAllCustomerRewards aggregates all customers
    @Test
    void testGetAllCustomerRewards_ReturnsMultipleSummaries() {
        Customer c1 = createCustomer(1L, "Arun", Collections.singletonList(txn(LocalDate.of(2025, 3, 1), 120)));
        Customer c2 = createCustomer(2L, "Kumar", Collections.singletonList(txn(LocalDate.of(2025, 5, 1), 80)));

        try (MockedStatic<StaticData> mockedStatic = mockStatic(StaticData.class)) {
            mockedStatic.when(StaticData::getCustomers).thenReturn(Arrays.asList(c1, c2));

            List<RewardSummary> summaries = rewardService.getAllCustomerRewards();

            assertEquals(2, summaries.size());
            assertTrue(summaries.stream().anyMatch(s -> s.getName().equals("Arun")));
            assertTrue(summaries.stream().anyMatch(s -> s.getName().equals("Kumar")));
        }
    }
}
