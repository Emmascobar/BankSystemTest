package com.ironhack.service.interfaces;

import com.ironhack.controller.DTOs.SavingMinimumBalanceDTO;
import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Users.User;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {
    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    List<User> getAllUsers();
    Account addNewBankAccount (String typeOfAccount, Account account);
    Admin addNewAdminUser (Admin admin);
    AccountHolder addNewAccountHolder (AccountHolder accountHolder);
    ThirdParty addNewThirdPartyUser (ThirdParty thirdParty);
    void updateBalance(Long id, BigDecimal newAmount);
    void updateAccount(Long id, Account account);
    void updateCreditLimit(Long id, BigDecimal creditLimit);
    void updateInterestRate(Long id, BigDecimal interestRate);
    void updateMinimumBalance(Long id, SavingMinimumBalanceDTO savingMinimumBalanceDTO);
    void deleteAccount(Long id);
}
