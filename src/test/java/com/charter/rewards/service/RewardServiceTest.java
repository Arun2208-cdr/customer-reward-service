package com.charter.rewards.service;

import com.charter.rewards.exception.CustomerNotFoundException;
import com.charter.rewards.model.Customer;
import com.charter.rewards.model.RewardSummary;
import com.charter.rewards.model.Transaction;
import com.charter.rewards.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RewardServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardService rewardService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Transaction> transactions = new ArrayList<>();

        Transaction t1 = new Transaction();
        t1.setId(1L);
        t1.setAmount(120.0);
        t1.setDate(LocalDate.of(2025, Month.JUNE, 10));

        Transaction t2 = new Transaction();
        t2.setId(2L);
        t2.setAmount(75.0);
        t2.setDate(LocalDate.of(2025, Month.JULY, 15));

        Transaction t3 = new Transaction();
        t3.setId(3L);
        t3.setAmount(40.0);
        t3.setDate(LocalDate.of(2025, Month.AUGUST, 5));

        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setPhone("9999999999");
        customer.setCity("New York");
        customer.setTransactions(transactions);
    }

    @Test
    void testGetRewardsForCustomerForPeriod_Success() throws CustomerNotFoundException {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        LocalDate start = LocalDate.of(2025, 6, 1);
        LocalDate end = LocalDate.of(2025, 8, 31);

        RewardSummary summary = rewardService.getRewardsForCustomerForPeriod(1L, start, end);

        assertNotNull(summary);
        assertEquals(1L, summary.getCustomerId());
        assertEquals("John Doe", summary.getName());
        assertEquals(115, summary.getTotalRewards()); // 90 (June) + 25 (July) + 0 (August)
        assertTrue(summary.getMonthlyRewards().containsKey("2025-06"));
        assertEquals(90, summary.getMonthlyRewards().get("2025-06"));
        assertEquals(25, summary.getMonthlyRewards().get("2025-07"));
    }

    @Test
    void testGetRewardsForCustomerForPeriod_CustomerNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        LocalDate start = LocalDate.of(2025, 6, 1);
        LocalDate end = LocalDate.of(2025, 8, 31);

        assertThrows(CustomerNotFoundException.class,
                () -> rewardService.getRewardsForCustomerForPeriod(99L, start, end));
    }

    @Test
    void testGetRewardsForCustomerForPeriod_NoTransactionsInRange() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        LocalDate start = LocalDate.of(2025, Month.SEPTEMBER, 1);
        LocalDate end = LocalDate.of(2025, Month.SEPTEMBER, 30);

        RewardSummary summary = rewardService.getRewardsForCustomerForPeriod(1L, start, end);

        assertNotNull(summary);
        assertEquals(0, summary.getTotalRewards());
        assertTrue(summary.getMonthlyRewards().isEmpty());
    }

    @Test
    void testGetRewardsForCustomerForPeriod_BoundaryDatesIncluded() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        LocalDate start = LocalDate.of(2025, Month.JUNE, 10);  // same as first txn
        LocalDate end = LocalDate.of(2025, Month.JULY, 15);   // same as second txn

        RewardSummary summary = rewardService.getRewardsForCustomerForPeriod(1L, start, end);

        assertNotNull(summary);
        assertEquals(2, summary.getMonthlyRewards().size());
        assertTrue(summary.getMonthlyRewards().containsKey("2025-06"));
        assertTrue(summary.getMonthlyRewards().containsKey("2025-07"));
    }

    @Test
    void testGetRewardsForCustomerForPeriod_MultipleMonths() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        LocalDate start = LocalDate.of(2025, Month.JUNE, 1);
        LocalDate end = LocalDate.of(2025, Month.AUGUST, 31);

        RewardSummary summary = rewardService.getRewardsForCustomerForPeriod(1L, start, end);

        assertNotNull(summary);
        assertEquals(3, customer.getTransactions().size());
        assertEquals(3, summary.getMonthlyRewards().size());
        assertTrue(summary.getTotalRewards() > 0);
    }
}
