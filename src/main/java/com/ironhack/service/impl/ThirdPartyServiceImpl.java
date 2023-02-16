package com.ironhack.service.impl;

import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transference;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Users.ThirdPartyRepository;
import com.ironhack.repository.Utils.TransferenceRepository;
import com.ironhack.service.interfaces.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private TransferenceRepository transferenceRepository;

    public void transference(Long id, BigDecimal amount, Long destinationId, Integer destinationSecretKey) {
        Account destination = accountRepository.findAccountById(destinationId);
        ThirdParty thirdPartyAccount = thirdPartyRepository.findThirdPartyById(id);

        if (destination.getSecretKey() == destinationSecretKey) {
            destination.setBalance(new Money(destination.getBalance().increaseAmount(amount)));
            accountRepository.save(destination);
            thirdPartyAccount.setBalance(new Money(thirdPartyAccount.getBalance().decreaseAmount(amount)));
            thirdPartyRepository.save(thirdPartyAccount);
            Transference registry = new Transference(amount, destinationId, id);
            transferenceRepository.save(registry);
            } else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds");
        }
    }
}
