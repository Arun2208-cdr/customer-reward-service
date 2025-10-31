package com.retail.rewards.data;

import com.retail.rewards.model.Customer;
import com.retail.rewards.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class StaticData {
    private static final List<Customer> CUSTOMERS = new ArrayList<>();

    static {
        CUSTOMERS.add(createCustomer(1L, "Arun", "9876543210", "Chennai"));
        CUSTOMERS.add(createCustomer(2L, "Dinesh", "9988776655", "Bangalore"));
        CUSTOMERS.add(createCustomer(3L, "Rahul", "9090909090", "Hyderabad"));
        CUSTOMERS.add(createCustomer(4L, "Dhivya", "9123456789", "Pune"));
        CUSTOMERS.add(createCustomer(5L, "Vikram", "9871234567", "Delhi"));
    }

    /**
     * Generates random transaction data for a customer.
     *
     * @param id    the unique ID of the customer
     * @param name  the name of the customer
     * @param phone the phone number of the customer
     * @param city  the city of the customer
     * @return a {@link Customer} object containing randomly generated transactions
     */
    private static Customer createCustomer(Long id, String name, String phone, String city) {
        List<Transaction> txns = new ArrayList<>();
        Random rand = new Random(id);
        for (int m = 1; m <= 12; m++) {
            int txnCount = 3 + rand.nextInt(3);
            for (int i = 0; i < txnCount; i++) {
                double amount = 30 + rand.nextInt(150);
                txns.add(new Transaction(LocalDate.of(2025, m, 1 + rand.nextInt(25)), amount));
            }
        }
        return new Customer(id, name, phone, city, txns);
    }

    /**
     * Returns the list of all predefined customers.
     *
     * @return a list of {@link Customer} objects
     */
    public static List<Customer> getCustomers() {
        return CUSTOMERS;
    }
}
