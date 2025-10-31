package com.retail.rewards.service;

import com.retail.rewards.model.RewardSummary;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RewardServiceTest {
    private final RewardService service = new RewardService();

    @Test
    public void testGetAllCustomerRewards() {
        List<RewardSummary> list = service.getAllCustomerRewards();
        assertNotNull(list);
        assertTrue(list.size() >= 5);
    }

    @Test
    public void testSingleCustomerReward() {
        RewardSummary summary = service.getRewardsByCustomer(1L);
        assertNotNull(summary);
        assertTrue(summary.getTotalRewards() >= 0);
    }
}
