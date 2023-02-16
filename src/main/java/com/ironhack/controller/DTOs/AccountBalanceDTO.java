package com.ironhack.controller.DTOs;

import com.ironhack.model.Utils.Money;

public class AccountBalanceDTO {

private Money Balance;

    public Money getBalance() {
        return Balance;
    }

    public void setBalance(Money balance) {
        Balance = balance;
    }
}
