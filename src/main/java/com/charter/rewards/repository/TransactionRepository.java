package com.charter.rewards.repository;

import com.charter.rewards.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}
