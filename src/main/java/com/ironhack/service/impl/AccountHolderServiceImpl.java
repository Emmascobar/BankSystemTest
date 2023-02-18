package com.ironhack.service.impl;

import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Accounts.Checking;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Accounts.Saving;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Role;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transfer;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Accounts.CreditCardRepository;
import com.ironhack.repository.Users.AccountHoldersRepository;
import com.ironhack.repository.Users.RoleRepository;
import com.ironhack.repository.Utils.TransferenceRepository;
import com.ironhack.service.interfaces.AccountHolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    @Autowired
    AccountHoldersRepository accountHoldersRepository;
    @Autowired
    TransferenceRepository transferenceRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    String encodedPassword;
    Role role;
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    /**
     * -------------------------------------------------------------------------------------
     **/

    /* Create a new user Account */
    public AccountHolder createAccount(AccountHolder accountHolder) {
        encodedPassword = passwordEncoder.encode(accountHolder.getPassword());
        accountHolder.setPassword(encodedPassword);
        accountHolder = accountHoldersRepository.save(accountHolder);
        role = roleRepository.save(new Role("ACCOUNT_HOLDER"));
        return accountHolder;
    }
    @Override
    public AccountHolder getAccount(Long id) {
        AccountHolder accountHolder = accountHoldersRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return accountHoldersRepository.save(accountHolder);
    }
    public Money getCreditBalance(Long id) {
        CreditCard creditCard = creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        /* Credit Card is accessed and checked with the setMethod. If the elapsed time is more
        than a month, the monthly interest must be applied automatically and returning the updated Balance */
        creditCard.setMonthlyInterestRate();
          creditCardRepository.save(creditCard);
          return creditCard.getBalance();
    }
    public Money getSavingBalance(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        /* Every time the balance of a Saving Account is accessed, it is checked with the setMethod. if
        the elapsed time is more than a year, the interest must be applied automatically and returning
        the updated Balance. */
        if (account instanceof Saving) {
            ((Saving) account).setAnnualInterestRate();
        }
        return account.getBalance();
    }
    public Transfer transfer(Transfer transfer) {
        Account destination = accountRepository.findById(transfer.getDestinationId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination ID not found"));
        Account ownAccount = accountRepository.findById(transfer.getOwnerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin ID not found"));

        // Checking Balance & the secretKey //
        boolean enoughBalance = ownAccount.getBalance().getAmount().compareTo(transfer.getAmount()) == 1;
        String primaryOwner = destination.getPrimaryOwner().getName();
        String secondaryOwner = destination.getSecondaryOwner().getName();
        if (!enoughBalance && destination.getPrimaryOwner().getName() == transfer.getDestinationName() || destination.getSecondaryOwner().getName() == transfer.getDestinationName() ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds Or Wrong destination name, please check");
        } else {
            destination.setBalance(new Money(destination.getBalance().increaseAmount(transfer.getAmount())));
            ownAccount.setBalance(new Money(ownAccount.getBalance().decreaseAmount(transfer.getAmount())));
            accountRepository.save(destination);
            // Before save Origin balance check conditions of Saving or Checking minimum balance and PenaltyFee.
            if (ownAccount instanceof Saving || ownAccount instanceof Checking) {
                BigDecimal minimumBalance = ((Saving) ownAccount).getMinimumBalance().getAmount();
                if (ownAccount.getBalance().getAmount().compareTo(minimumBalance) == -1) {
                    /* Could apply fees*/
                    ownAccount.setBalance(new Money(ownAccount.getBalance().getAmount().subtract(((Saving) ownAccount).getPenaltyFee())));
                }
            }
            accountRepository.save(ownAccount);
        }
        /* Save & Registry transfer on system and database */
        log.info("Transference registry on DateBase, from " + transfer.getId() + " to " + transfer.getDestinationId() + " with a amount of " + transfer.getAmount() + " at " + LocalDate.now());
        return transferenceRepository.save(transfer);
    }
}