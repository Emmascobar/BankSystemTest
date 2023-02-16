package com.ironhack.controller.impl;

import com.ironhack.controller.DTOs.AccountBalanceDTO;
import com.ironhack.controller.DTOs.SavingMinimumBalanceDTO;
import com.ironhack.controller.interfaces.AdminController;
import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class AdminControllerImpl implements AdminController {

    @Autowired
    AdminService adminService;

    /* Set of ADMIN routes */
    @GetMapping("/admin/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAllAccounts() {
        return adminService.getAllAccounts();
    }
    @GetMapping("/admin/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountById(@PathVariable Long id) {
        return adminService.getAccountById(id);
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin addNewAdminUser(Admin admin) {
        return adminService.addNewAdminUser(admin);
    }

    @PostMapping("/admin/users/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder addNewAccountHolder(@RequestBody @Valid AccountHolder accountHolder) {
        return adminService.addNewAccountHolder(accountHolder);
    }
    @PostMapping("/admin/users/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty addNewThirdPartyAccount(@RequestBody @Valid ThirdParty thirdParty) {
        return adminService.addNewThirdPartyUser(thirdParty);
    }
    @PostMapping("/admin/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Account addNewBankAccount(@RequestBody @Valid String typeOfAccount, Account account) {
        return adminService.addNewBankAccount(typeOfAccount, account);
    }
    @PutMapping("/admin/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccount(@PathVariable Long id, @RequestBody @Valid Account account) {
        adminService.updateAccount(id, account);
    }
    @PatchMapping("/admin/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable Long id, @RequestBody @Valid BigDecimal newAmount, AccountBalanceDTO accountBalanceDTO) {
        adminService.updateBalance(id,newAmount);
    }
    @PatchMapping("/admin/credit-cards/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCreditLimit(@PathVariable Long id, @RequestBody @Valid BigDecimal creditLimit) {
        adminService.updateCreditLimit(id, creditLimit);
    }
    @PatchMapping("/admin/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInterestRate(@PathVariable Long id, @RequestBody @Valid BigDecimal interestRate) {
        adminService.updateCreditLimit(id, interestRate);
    }
    @PatchMapping("/admin/accounts/savings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMinimumBalance(@PathVariable Long id, @RequestBody SavingMinimumBalanceDTO savingMinimumBalanceDTO) {
        adminService.updateMinimumBalance(id, savingMinimumBalanceDTO);
    }
    @DeleteMapping("/admin/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id) {
        adminService.deleteAccount(id);
    }
}
