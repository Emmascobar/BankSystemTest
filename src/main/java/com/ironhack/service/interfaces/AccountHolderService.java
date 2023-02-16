package com.ironhack.service.interfaces;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.security.CustomUserDetails;

import java.math.BigDecimal;

public interface AccountHolderService {

    AccountHolder createAccount (AccountHolder accountHolder);
    AccountHolder getAccountById(Long id, CustomUserDetails user);
    Money getAccountBalance (Long id);
    Money getCreditBalance (Long id);
    void transference(BigDecimal amount, Long ownerId, String ownerName, Long destinationId);


}
