package com.ironhack.controller.interfaces;

import com.ironhack.controller.DTOs.AccountBalanceDTO;
import com.ironhack.controller.DTOs.CreditLimitDTO;
import com.ironhack.controller.DTOs.InterestRateDTO;
import com.ironhack.controller.DTOs.SavingMinimumBalanceDTO;
import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Accounts.Checking;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Accounts.Saving;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Users.User;

import java.util.List;

public interface AdminController {

    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    List<User> getAllUsers();
    Admin addNewAdminUser (Admin admin);
    AccountHolder addNewAccountHolder (AccountHolder accountHolder);
    ThirdParty addNewThirdPartyAccount (ThirdParty thirdParty);
    Account addNewSavingAccount (Saving saving);
    Account addNewCheckingAccount (Checking checking);
    Account addNewCreditCard (CreditCard creditCard);
    void updateBalance(AccountBalanceDTO accountBalanceDTO);
    void updateSavingAccount(Long id, Saving saving);
    void updateCreditLimit(Long id, CreditLimitDTO creditLimitDTO);
    void updateSavingInterestRate(Long id, InterestRateDTO interestRateDTO);
    void updateCreditInterestRate(Long id, InterestRateDTO interestRateDTO);
    void updateMinimumBalance(Long id, SavingMinimumBalanceDTO savingMinimumBalanceDTO);
    void deleteAccount(Long id);
}
