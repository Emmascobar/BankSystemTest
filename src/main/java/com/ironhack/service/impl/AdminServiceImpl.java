package com.ironhack.service.impl;

import com.ironhack.controller.DTOs.SavingMinimumBalanceDTO;
import com.ironhack.model.Accounts.*;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.Role;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Money;
import com.ironhack.repository.Accounts.*;
import com.ironhack.repository.Users.*;
import com.ironhack.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    // Autowired
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    SavingRepository savingRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AccountHoldersRepository accountHoldersRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    String encodedPassword;
    Role role;

    /** --------------------------------------------------------------------------- **/

    /* Find and get all accounts */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /* Find and get an account by Id */
    public Account getAccountById(Long id) {
        return accountRepository.findAccountById(id);
    }

    /* Create a new admin User account */
    public Admin addNewAdminUser(Admin admin) {
        encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        admin = adminRepository.save(admin);
        role = roleRepository.save(new Role("ADMIN", admin));
        return admin;
    }

    /* Create a new AccountHolder User */
    public AccountHolder addNewAccountHolder(AccountHolder accountHolder) {
        encodedPassword = passwordEncoder.encode(accountHolder.getPassword());
        accountHolder.setPassword(encodedPassword);
        accountHolder = accountHoldersRepository.save(accountHolder);
        role = roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder));
        return accountHolder;
    }

    /* Create a new TPUSer */
    public ThirdParty addNewThirdPartyUser(ThirdParty thirdParty) {
        return thirdPartyRepository.save(thirdParty);
    }

    //ADD NEW BANK ACCOUNT // Age condition (>24) to create a student or normal account // CASTING PARAMS.
    public Account addNewBankAccount(String typeOfAccount, Account account) {
        if (typeOfAccount == "Saving") {
            // SAVING ACCOUNT CREATED
            Saving saving = new Saving();
            // CASTING PARAMS FROM ACCOUNT
            saving.setCreationDate(account.getCreationDate());
            saving.setBalance(account.getBalance());
            saving.setPrimaryOwner(account.getPrimaryOwner());
            saving.setSecondaryOwner(account.getSecondaryOwner());
            saving.setSecretKey(account.getSecretKey());
            saving.setStatus(account.getStatus());
            // SAVE THE NEW SAVING ACCOUNT IN REPOSITORY
            return savingRepository.save(saving);

        } else if (typeOfAccount == "Credit Card") {
            // CREDIT CARD ACCOUNT CREATED
            CreditCard creditCard = new CreditCard();
            // CASTING PARAMS FROM ACCOUNT
            creditCard.setCreationDate(account.getCreationDate());
            creditCard.setBalance(account.getBalance());
            creditCard.setPrimaryOwner(account.getPrimaryOwner());
            creditCard.setSecondaryOwner(account.getSecondaryOwner());
            creditCard.setSecretKey(account.getSecretKey());
            creditCard.setStatus(account.getStatus());
            // SAVE THE NEW CREDIT CARD ACCOUNT IN REPOSITORY
            return creditCardRepository.save(creditCard);

        } else if (typeOfAccount == "Checking") {
            /* Get the age from the birthday with ChronoUnit */
            LocalDate today = LocalDate.now();
            LocalDate birthday = account.getPrimaryOwner().getDateOfBirth();
            Long age = ChronoUnit.YEARS.between(birthday, today);
            /* Then created conditions for new accounts by Age */
            if (age < 24) {
                // STUDENT ACCOUNT CREATED
                StudentChecking studentChecking = new StudentChecking();
                // CASTING PARAMS FROM CHECKING TO STUDENT
                studentChecking.setCreationDate(account.getCreationDate());
                studentChecking.setBalance(account.getBalance());
                studentChecking.setPrimaryOwner(account.getPrimaryOwner());
                studentChecking.setSecondaryOwner(account.getSecondaryOwner());
                studentChecking.setSecretKey(account.getSecretKey());
                studentChecking.setStatus(account.getStatus());
                // SAVE STUDENT CHECKING IN REPOSITORY
                return studentCheckingRepository.save(studentChecking);
            } else {
                // NORMAL CHECKING ACCOUNT CREATED
                Checking checking = new Checking();
                // CASTING PARAMS FROM ACCOUNT TO CHECKING
                checking.setCreationDate(account.getCreationDate());
                checking.setBalance(account.getBalance());
                checking.setPrimaryOwner(account.getPrimaryOwner());
                checking.setSecondaryOwner(account.getSecondaryOwner());
                checking.setSecretKey(account.getSecretKey());
                checking.setStatus(account.getStatus());
                // SAVE NORMAL CHECKING ACCOUNT IN REPOSITORY
                return checkingRepository.save(checking);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Type of account NO valid");
        }
    }

    /* Update Balance */
    public void updateBalance(Long id, BigDecimal newAmount){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (!optionalAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
            optionalAccount.get().setBalance(new Money(newAmount));
            accountRepository.save(optionalAccount.get());
    }


    /* Update a complete account */
    public void updateAccount(Long id, Account account) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            account.setId(id);
            accountRepository.save(account);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }

    /* Update Credits limits */
    public void updateCreditLimit(Long id, BigDecimal creditLimit) {
        CreditCard creditCard = creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit Card ID not found"));
        if (creditLimit.compareTo(new BigDecimal(100000)) == 1) {
            throw new RuntimeException("Credit Limit cannot be higher than 100.000. Define a new one");
        } else if (creditLimit.compareTo(new BigDecimal(100)) == -1 && (creditLimit.compareTo(new BigDecimal(100000)) == 1)) {
            creditCard.setCreditLimit(creditLimit);
            creditCardRepository.save(creditCard);
        } else {
            throw new RuntimeException("Credit Limit cannot be lower than 100. Define a new one");
        }
    }

    // Update accounts interest rate // SAVINGS AND CREDIT CARDS.
    public void updateInterestRate(Long id, BigDecimal interestRate) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saving count not found"));

        if (account instanceof Saving) {
            if (interestRate.compareTo(new BigDecimal("0.5")) == 1) {
                throw new RuntimeException("Interest Rate cannot be higher than 0.5. Define a new one");
            } else if (interestRate.compareTo(new BigDecimal("0.5")) == -1 && interestRate.compareTo(new BigDecimal("0.0025")) == 1) {
                ((Saving) account).setInterestRate(interestRate);
                savingRepository.save((Saving) account);
            } else {
                throw new RuntimeException("Interest Rate cannot be lower than 0.0025. Define a new one");
            }
        }
        if (account instanceof CreditCard) {
            if (interestRate.compareTo(new BigDecimal("0.1")) == -1) {
                throw new RuntimeException("Interest Rate cannot be lower than 0.1. Default value = 0.2. Define a new one");
            } else if (interestRate.compareTo(new BigDecimal("0.2")) == 1 && interestRate.compareTo(new BigDecimal("0.1")) == -1) {
                ((CreditCard) account).setInterestRate(interestRate);
                creditCardRepository.save((CreditCard) account);
            } else {
            }
        }
    }


    // Update Minimum Balance // SAVING ACCOUNTS.
    public void updateMinimumBalance(Long id, SavingMinimumBalanceDTO savingMinimumBalanceDTO) {
        Saving updateBalanceSaving = savingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saving count not found"));
        BigDecimal minimumBalance = savingMinimumBalanceDTO.getMinimumBalance().getAmount();
        if (minimumBalance.compareTo(new BigDecimal("100")) == -1) {
            throw new RuntimeException("Minimum Balance cannot be lower than 100. Define a new one");
        } else if (minimumBalance.compareTo(new BigDecimal("100")) == 1) {
            updateBalanceSaving.setMinimumBalance(new Money(minimumBalance));
            savingRepository.save(updateBalanceSaving);
        }
    }

    // Delete a account
    public void deleteAccount(Long id) {
        if (!accountRepository.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        accountRepository.deleteById(id);
    }

}