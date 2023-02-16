package com.ironhack.model.Accounts;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class StudentChecking extends Account{

    @Nullable
    private BigDecimal penaltyFee;

    public StudentChecking() {
    }

    public StudentChecking(Money balance, Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, LocalDate creationDate, Status status) {
        super(balance, secretKey, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.penaltyFee = new BigDecimal("40");
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }
}
