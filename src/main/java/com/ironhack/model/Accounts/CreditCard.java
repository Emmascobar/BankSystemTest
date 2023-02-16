package com.ironhack.model.Accounts;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class CreditCard extends Account{

    @NotEmpty(message = "Credit limit cannot be empty")
    private BigDecimal creditLimit;
    @NotEmpty(message = "Interest rate cannot be empty")
    @Digits(integer = 2, fraction = 4, message = "Default format of IR is 0.0025")
    private BigDecimal interestRate;

    private LocalDate lastUpdate;

    public CreditCard() {
    }

    public CreditCard(Money balance, Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, LocalDate creationDate, Status status, BigDecimal creditLimit, BigDecimal interestRate, LocalDate lastUpdate) {
        super(balance, secretKey, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.lastUpdate = lastUpdate;
        this.creditLimit = new BigDecimal("100");
        this.interestRate = new BigDecimal("0.2");
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public void setMonthlyInterestRate() {
        LocalDate today = LocalDate.now();
        long elapsedTime = ChronoUnit.MONTHS.between(getLastUpdate(), today);
        if (elapsedTime > 1) {
            BigDecimal monthlyInteres = getInterestRate().divide(new BigDecimal("12"));
            setBalance(new Money(getBalance().getAmount().add(monthlyInteres)));
            setLastUpdate(LocalDate.now());
        }
    }
}
