package com.ironhack.controller.interfaces;

import com.ironhack.controller.DTOs.AccountBalanceDTO;
import com.ironhack.controller.DTOs.SavingMinimumBalanceDTO;
import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.ThirdParty;

import java.math.BigDecimal;
import java.util.List;

public interface AdminController {

    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    Account addNewBankAccount (String typeOfAccount, Account account);
    Admin addNewAdminUser (Admin admin);
    AccountHolder addNewAccountHolder (AccountHolder accountHolder);
    ThirdParty addNewThirdPartyAccount (ThirdParty thirdParty);
    void updateBalance(Long id, BigDecimal newAmount, AccountBalanceDTO accountBalanceDTO);
    void updateAccount(Long id, Account account);
    void updateCreditLimit(Long id, BigDecimal creditLimit);
    void updateInterestRate(Long id, BigDecimal interestRate);
    void updateMinimumBalance(Long id, SavingMinimumBalanceDTO savingMinimumBalanceDTO);
    void deleteAccount(Long id);
}
