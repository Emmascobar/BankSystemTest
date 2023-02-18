package com.ironhack.service.impl;

import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transfer;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Users.ThirdPartyRepository;
import com.ironhack.repository.Utils.TransferenceRepository;
import com.ironhack.service.interfaces.ThirdPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private TransferenceRepository transferenceRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public Transfer transfer(String hashKey, Transfer transfer) {
        Account destination = accountRepository.findById(transfer.getDestinationId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination ID not found"));;
        ThirdParty thirdPartyAccount = thirdPartyRepository.findById(transfer.getOwnerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin ID not found"));;
        // Checking Hashkey & secretKey //
        if (thirdPartyAccount.getHashKey() == hashKey && destination.getSecretKey() == transfer.getSecretKey()) {
            destination.setBalance(new Money(destination.getBalance().increaseAmount(transfer.getAmount())));
            accountRepository.save(destination);
            thirdPartyAccount.setBalance(new Money(thirdPartyAccount.getBalance().decreaseAmount(transfer.getAmount())));
            thirdPartyRepository.save(thirdPartyAccount);
            /* Save & Registry transfer on system and database */
        } else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Wrong Secret Key");
        }
        log.info("Transference registry on DateBase, from " + transfer.getId() + " to " + transfer.getDestinationId() + " with a amount of " + transfer.getAmount() + " at " + LocalDate.now());
        return transferenceRepository.save(transfer);
    }
}
