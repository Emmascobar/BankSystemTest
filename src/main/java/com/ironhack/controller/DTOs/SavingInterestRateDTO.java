package com.ironhack.controller.DTOs;

import java.math.BigDecimal;

public class SavingInterestRateDTO {
    private BigDecimal interestRate;

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
