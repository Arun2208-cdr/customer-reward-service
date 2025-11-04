package com.charter.rewards.controller;

import com.charter.rewards.exception.CustomerNotFoundException;
import com.charter.rewards.model.MonthlyReward;
import com.charter.rewards.model.RewardSummary;
import com.charter.rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(RewardControllerTest.MockConfig.class)
public class RewardControllerTest {

    private RewardSummary mockRewardSummary;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        RewardController rewardController(RewardService rewardService) {
            return new RewardController(rewardService);
        }
    }

    @BeforeEach
    void setUp() {
        mockRewardSummary = new RewardSummary(
                1L,
                "ABC",
                "9876543210",
                "New York",
                115,
                List.of(
                        new MonthlyReward("2025-06", 90),
                        new MonthlyReward("2025-07", 25)
                )
        );
    }

    @Test
    @DisplayName("Should return rewards summary successfully for valid customer and date range")
    void testGetRewardsForCustomer_Success() throws Exception {
        Mockito.when(rewardService.getRewardsForCustomerForPeriod(eq(1L), any(), any()))
                .thenReturn(mockRewardSummary);

        mockMvc.perform(get("/api/rewards")
                        .param("customerId", "1")
                        .param("start", "2025-06-01")
                        .param("end", "2025-08-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.name").value("ABC"))
                .andExpect(jsonPath("$.totalRewards").value(115))
                .andExpect(jsonPath("$.monthlyRewards[0].month").value("2025-06"))
                .andExpect(jsonPath("$.monthlyRewards[0].rewardPoints").value(90));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when customerId is missing")
    void testGetRewardsForCustomer_MissingCustomerId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("start", "2025-06-01")
                        .param("end", "2025-08-31"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Customer ID must be provided to fetch rewards"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when start date is after end date")
    void testGetRewardsForCustomer_InvalidDateRange_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("customerId", "1")
                        .param("start", "2025-09-01")
                        .param("end", "2025-06-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Start date cannot be after end date"));
    }

    @Test
    @DisplayName("Should return 404 Not Found when customer does not exist")
    void testGetRewardsForCustomer_CustomerNotFound_ShouldReturn404() throws Exception {
        Mockito.when(rewardService.getRewardsForCustomerForPeriod(eq(99L), any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found. Customer Id: 99"));

        mockMvc.perform(get("/api/rewards")
                        .param("customerId", "99")
                        .param("start", "2025-06-01")
                        .param("end", "2025-08-31"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Customer not found. Customer Id: 99"));
    }
}
