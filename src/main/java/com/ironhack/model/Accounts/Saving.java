package com.ironhack.model.Accounts;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.enums.Status;
import jakarta.persistence.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Currency;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class Saving extends Account {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    @NotEmpty(message = "Minimum balance cannot be empty")
    private Money minimumBalance;
    @NotEmpty(message = "Interest rate cannot be empty")
    @Digits(integer = 2, fraction = 4, message = "Default format of IR is 0.0025")
    private BigDecimal interestRate;

    @Nullable
    private BigDecimal penaltyFee;
    private LocalDate lastUpdate;


    public Saving() {
    }

    public Saving(Money balance, Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, LocalDate creationDate, Status status, Money minimumBalance, BigDecimal interestRate, LocalDate lastUpdate) {
        super(balance, secretKey, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.lastUpdate = lastUpdate;
        this.minimumBalance = new Money(new BigDecimal("1000"), Currency.getInstance("USD"), RoundingMode.HALF_EVEN);
        this.interestRate = new BigDecimal("0.0025");
        this.penaltyFee = new BigDecimal("40");
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    // ANNUAL INTEREST RATE METHOD
    public void setAnnualInterestRate(){
        LocalDate today = LocalDate.now();
        long elapsedTime = ChronoUnit.YEARS.between(getLastUpdate(), today);
        if (elapsedTime > 1) {
            BigDecimal annualRate = getBalance().getAmount().multiply(interestRate);
            setBalance(new Money(getBalance().getAmount().add(annualRate)));
            setLastUpdate(LocalDate.now());
        }
    }
    // PENALTY FEE METHOD
    public void applyPenaltyFee() {
        if (getBalance().getAmount().compareTo(getMinimumBalance().getAmount()) < 0) {
            setBalance(new Money(getBalance().getAmount().subtract(penaltyFee)));
        }
    }
}
