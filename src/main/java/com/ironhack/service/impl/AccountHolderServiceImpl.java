package com.ironhack.service.impl;

import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Accounts.Saving;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Role;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transference;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Accounts.CreditCardRepository;
import com.ironhack.repository.Users.AccountHoldersRepository;
import com.ironhack.repository.Users.RoleRepository;
import com.ironhack.repository.Users.UserRepository;
import com.ironhack.repository.Utils.TransferenceRepository;
import com.ironhack.security.CustomUserDetails;
import com.ironhack.service.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    @Autowired
    AccountHoldersRepository accountHoldersRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    String encodedPassword;
    Role role;
    @Autowired
    private TransferenceRepository transferenceRepository;

    /** ------------------------------------------------------------------------------------- **/

    /* Create a new user Account */
    public AccountHolder createAccount(AccountHolder accountHolder) {
        encodedPassword = passwordEncoder.encode(accountHolder.getPassword());
        accountHolder.setPassword(encodedPassword);
        accountHolder = accountHoldersRepository.save(accountHolder);
        role = roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder));
        return accountHolder;
    }

    /* Enter to user account system with ID & Credencials */

    public AccountHolder getAccountById(Long id, CustomUserDetails user) {
        return accountHoldersRepository.findAccountHolderById(id);
        // AQUI DEBO PASAR CREDENCIALES, AVERIGUAR COMO //
    }

    public Money getAccountBalance(Long id) {
        /* Every time the balance is accessed for user. The SAVINGS are checked with the Setmethod,
        And if it's been more than a month they apply or not, */
        Account account = accountRepository.findAccountById(id);
        boolean negativeBalance = (account.getBalance().getAmount().compareTo(((Saving) account).getMinimumBalance().getAmount()) == -1);
        if (account instanceof Saving && negativeBalance) {
            account.setBalance(new Money(account.getBalance().getAmount().subtract(((Saving) account).getPenaltyFee())));
            accountRepository.save(account);
        }
        /* Just as a Credit Card is accessed and checked with the setMethod. If the elapsed time is more
        than a month, the monthly interest must be applied automatically and returning the updated Balance */
        else if (account instanceof CreditCard) {
            ((CreditCard) account).setMonthlyInterestRate();
        }
        return account.getBalance();
    }
    public Money getCreditBalance(Long id) {
        Account account = accountRepository.findAccountById(id);
        /* Every time the balance of a Saving Account is accessed, it is checked with the setMethod. if
        the elapsed time is more than a year, the interest must be applied automatically and returning
        the updated Balance. */
        if (account instanceof Saving) {
            ((Saving) account).setAnnualInterestRate();
        }
        return account.getBalance();
    }


    public void transference(BigDecimal amount, Long ownerId, String ownerName, Long destinationId) {
        Account destination = accountRepository.findAccountById(destinationId);
        Account ownAccount = accountRepository.findAccountById(ownerId);

        if (destination.getPrimaryOwner().getName() == ownerName || destination.getSecondaryOwner().getName() == ownerName) {
            if (amount.compareTo(destination.getBalance().getAmount()) == -1) {
                destination.setBalance(new Money(destination.getBalance().increaseAmount(amount)));
                accountRepository.save(destination);
                ownAccount.setBalance(new Money(ownAccount.getBalance().decreaseAmount(amount)));

                /* Now check the balance. if it is negative could apply fees*/
                boolean negativeBalance = (ownAccount.getBalance().getAmount().compareTo(((Saving) ownAccount).getMinimumBalance().getAmount()) == -1);
                if (ownAccount instanceof Saving && negativeBalance) {
                    ownAccount.setBalance(new Money(ownAccount.getBalance().getAmount().subtract(((Saving) ownAccount).getPenaltyFee())));
                }
                accountRepository.save(ownAccount);
                Transference registry = new Transference(amount, destinationId, ownerId);
                transferenceRepository.save(registry);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds");
            }
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "ID/NAME not matching with a destination account. Please review");
        }
    }
}