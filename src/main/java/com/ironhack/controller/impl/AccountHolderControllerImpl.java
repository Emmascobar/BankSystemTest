package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.AccountHolderController;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.service.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AccountHolderControllerImpl implements AccountHolderController {

    @Autowired
    AccountHolderService accountHolderService;

    /* Account holder GETMAPPING by ID */

    //METER LA AUTENTIFICACION. REVISAR SERVICES.
//    @GetMapping("user/accounts/account-holder/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public AccountHolder getAccountById(@PathVariable Long id @RequestBody String userName) {
//        return accountHolderService.getAccountById(id, username);
//    }

    @Override
    public AccountHolder getAccountById(Long id, String userName) {
        return null;
    }

    /* Balances GETMAPPING of Account Holders */
    //METER LA AUTENTIFICACION misma arriba. REVISAR SERVICES.
    @GetMapping("user/accounts/account-holder/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getAccountBalance(@PathVariable Long id) {
        return accountHolderService.getAccountBalance(id);
    }

    /* Transferences POSTMAPPING of Account Holders */
    @PostMapping("User/accounts/{id}/transference")
    @ResponseStatus(HttpStatus.CREATED)
    public void transference(@PathVariable Long id, @RequestBody BigDecimal amount, String ownerName, Long destinationId) {
        accountHolderService.transference(amount, id, ownerName, destinationId);
    }
}
