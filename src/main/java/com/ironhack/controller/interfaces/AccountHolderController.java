package com.ironhack.controller.interfaces;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;

import java.math.BigDecimal;

public interface AccountHolderController {

    AccountHolder getAccountById(Long id, String userName);

    Money getAccountBalance (Long id);

    void transference(Long id, BigDecimal amount, String ownerName, Long destinationId);

}
