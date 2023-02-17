package com.ironhack.service.impl;

import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transference;
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

import java.math.BigDecimal;
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


    public void transference(Long ownId, BigDecimal amount, Long destinationId, Integer destinationSecretKey) {
        Account destination = accountRepository.findById(destinationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination ID not found"));;
        ThirdParty thirdPartyAccount = thirdPartyRepository.findById(ownId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin ID not found"));;
        // Checking the secretKey //
        if (destination.getSecretKey() == destinationSecretKey) {
            destination.setBalance(new Money(destination.getBalance().increaseAmount(amount)));
            accountRepository.save(destination);
            thirdPartyAccount.setBalance(new Money(thirdPartyAccount.getBalance().decreaseAmount(amount)));
            thirdPartyRepository.save(thirdPartyAccount);

            /* Save & Registry transfer on system and database */
            Transference registry = new Transference(amount, destinationId, ownId);
            transferenceRepository.save(registry);

            log.info("Transference registry on DateBase, from " + ownId + " to " + destinationId + " with a amount of " + amount + " at " + LocalDate.now());

        } else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds");
        }
    }
}
