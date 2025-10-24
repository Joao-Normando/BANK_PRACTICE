package com.example.banco.service; // <-- CORREÇÃO AQUI

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class AccountService {

    private BigDecimal balance;
    private final BigDecimal overdraftLimit;

    /**
     * Starts with R$ 200.00 balance and R$ 1000.00 overdraft limit.
     */
    public AccountService() {
        this.balance = new BigDecimal("200.00");
        this.overdraftLimit = new BigDecimal("1000.00");
    }

    public BigDecimal checkBalance() {
        return this.balance;
    }

    public String getOverdraftInfo() {
        BigDecimal usedAmount = BigDecimal.ZERO;

        // If the balance is negative, the used amount is the (absolute) balance
        if (this.balance.compareTo(BigDecimal.ZERO) < 0) {
            usedAmount = this.balance.abs();
        }

        BigDecimal available = this.overdraftLimit.subtract(usedAmount);

        return String.format(
                "Total Limit: R$ %.2f | Used: R$ %.2f | Available: R$ %.2f",
                this.overdraftLimit,
                usedAmount,
                available
        );
    }

    public void deposit(BigDecimal amount) {
        // Simple validation
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The deposit amount must be positive.");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The operation amount must be positive.");
        }

        BigDecimal totalAvailableBalance = this.balance.add(this.overdraftLimit);

        if (totalAvailableBalance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
        } else {
            throw new RuntimeException("Insufficient funds (including overdraft).");
        }
    }

    public void payBill(BigDecimal amount) {
        this.withdraw(amount);
    }

    public boolean isUsingOverdraft() {
        return this.balance.compareTo(BigDecimal.ZERO) < 0;
    }
}