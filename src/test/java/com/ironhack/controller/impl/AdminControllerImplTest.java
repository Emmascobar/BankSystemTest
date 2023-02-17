package com.ironhack.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.controller.DTOs.AccountBalanceDTO;
import com.ironhack.controller.DTOs.CreditLimitDTO;
import com.ironhack.controller.DTOs.SavingInterestRateDTO;
import com.ironhack.controller.DTOs.SavingMinimumBalanceDTO;
import com.ironhack.controller.interfaces.AdminController;
import com.ironhack.model.Accounts.Checking;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Accounts.Saving;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.Role;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Address;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.enums.Status;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Accounts.CheckingRepository;
import com.ironhack.repository.Accounts.CreditCardRepository;
import com.ironhack.repository.Accounts.SavingRepository;
import com.ironhack.repository.Users.AccountHoldersRepository;
import com.ironhack.repository.Users.AdminRepository;
import com.ironhack.repository.Users.RoleRepository;
import com.ironhack.repository.Users.ThirdPartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired

    private SavingRepository savingRepository;
    @Autowired

    private AccountRepository accountRepository;
    @Autowired

    private CreditCardRepository creditCardRepository;
    private CreditCard creditCard;
    private AdminController adminController;
    private AccountHolder accountHolder1, accountHolder2, accountHolder3;
    private Checking checking;
    private Saving saving01, saving02, saving03;
    private ThirdParty thirdParty;
    private AccountBalanceDTO accountBalanceDTO;
    private CreditLimitDTO creditLimitDTO;
    private SavingInterestRateDTO  savingInterestRateDTO;
    private SavingMinimumBalanceDTO savingMinimumBalanceDTO;
    private Role adminUserRole, holderUserRole;
    private Address address01, address02, address03;
    private Admin admin;
    @Autowired
    private AccountHoldersRepository accountHoldersRepository;
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {



    /* ADDRESS */
    address01 = new Address("Carrer de la Argentina 3", "Barcelona", 8041);
    address02 = new Address("Calle Ecuador", "Buenos Aires", 2041);
    address03 = new Address("Lincoln Road Avenue 963", "Miami", 1963);


    /* SET USERS - ACCOUNTS-HOLDERS */
    accountHolder1 = new AccountHolder("Matias Fabbro", "mattfabbro", passwordEncoder.encode("mattfabbro88"), List.of(holderUserRole), LocalDate.of(1988,03,24), address01, address03);
    accountHoldersRepository.save(accountHolder1);
    accountHolder2 = new AccountHolder("Pavlo Menendez", "pavlomenendez", passwordEncoder.encode("pavlomenendez88"),List.of(adminUserRole) ,LocalDate.of(1988,06,14), address02, null);
    accountHoldersRepository.save(accountHolder2);
    accountHolder3 = new AccountHolder("Jeremias Fabbro", "jerefabbro", passwordEncoder.encode("jerefabbro98"),List.of(holderUserRole), LocalDate.of(1998,02,21), address03, null);
    accountHoldersRepository.save(accountHolder3);
    admin = new Admin("Pavlo Menendez", "pavlomenendez88", passwordEncoder.encode("pavlomenendez88"), List.of(adminUserRole));
    adminRepository.save(admin);

    /* SET USERS AND TIPES OF ROLES */
    adminUserRole = new Role("ADMIN", accountHolder2);
    roleRepository.save(adminUserRole);
    holderUserRole = new Role("ACCOUNT_HOLDER", accountHolder1);
    roleRepository.save(holderUserRole);

    /* BANK ACCOUNTS */
    saving01 = new Saving(new Money(new BigDecimal("250")), 123456, accountHolder1, null, new BigDecimal("40"), LocalDate.now(), Status.ACTIVE, new Money(new BigDecimal("500")), new BigDecimal("0.2"), null );
    savingRepository.save(saving01);
    saving02 = new Saving(new Money(new BigDecimal("250")), 456789, accountHolder2, null, new BigDecimal("40"), LocalDate.now(), Status.FROZEN, new Money(new BigDecimal("50")), new BigDecimal("0.1"), null );
    saving03 = new Saving(new Money(new BigDecimal("250")), 568942, accountHolder3, accountHolder1, new BigDecimal("40"), LocalDate.now(), Status.ACTIVE, new Money(new BigDecimal("100")), new BigDecimal("0.02"), null );
    checking = new Checking(new Money(new BigDecimal("250")), 568942, accountHolder3, null, new BigDecimal("40"), LocalDate.now(), Status.ACTIVE, new Money(new BigDecimal("100")), new BigDecimal("0.02"));
    checkingRepository.save(checking);
    creditCard = new CreditCard(new Money(new BigDecimal("250")), 456789, accountHolder2, null, new BigDecimal("40"), LocalDate.now(), Status.FROZEN, new BigDecimal("500"), new BigDecimal("0.2"), null );
    creditCardRepository.save(creditCard);
    thirdParty = new ThirdParty(new Money(new BigDecimal("500")), "AAB123456");
    thirdPartyRepository.save(thirdParty);

    /* SET ROLES ON ACCOUNTS-USER*/
        accountHolder1.setRoles(List.of(holderUserRole));
        accountHolder2.setRoles(List.of(holderUserRole, adminUserRole));
}

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void findAll_NoParams_Accounts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/admin/accounts"))
                .andExpect(status().isOk()) // RESPONSE HTTP 200 - OK //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pavlo Menendez"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jeremias Fabbro"));
    }

    @Test
    void getAccountById_ValidId_GetAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/admin/accounts/"+ saving01.getId()))
                .andExpect(status().isOk()) // RESPONSE HTTP 200 - OK //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Matias Fabbro"));
    }

    @Test
    void save_ValidUser_NewAdminUser() throws Exception {
        String body = objectMapper.writeValueAsString(admin);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pavlo Menendez"));
    }

    @Test
    void store_ValidAccountHolder_AddNewAccountHolder() throws Exception {
        String body = objectMapper.writeValueAsString(accountHolder3);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin/users/account-holder")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jeremias Fabbro"));
    }

    @Test
    void store_AddNewThirdParty_AddNewAccount() throws Exception {
        String body = objectMapper.writeValueAsString(thirdParty);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin/users/third-party")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("AAB123456"));
    }


    @Test
    void store_ValidBankAccount_addNew() throws Exception {
        String body = objectMapper.writeValueAsString(accountHolder1);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin/accounts")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Matias Fabbro"));
    }


    @Test
    void change_updateValidAccount_NoReturn() throws Exception {
        Saving saving = new Saving(new Money(new BigDecimal("250")), 456789, accountHolder2, null, new BigDecimal("40"), LocalDate.now(), Status.FROZEN, new Money(new BigDecimal("50")), new BigDecimal("0.1"), null );
        String body = objectMapper.writeValueAsString(saving03);
        MvcResult mvcResult = mockMvc.perform(
                        put("/admin/accounts/"+saving.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // 204
                .andReturn();

        Optional<Saving> optionalSaving = savingRepository.findById(saving.getId());
        assertTrue(optionalSaving.isPresent());
        assertEquals("Pavlo Menendez", optionalSaving.get().getPrimaryOwner().getName());
    }

    @Test
    void change_updateBalance_NoReturn() throws Exception {
        accountBalanceDTO.setBalance(new Money(new BigDecimal("1500")));
        String body = objectMapper.writeValueAsString(accountBalanceDTO);
        MvcResult mvcResult = mockMvc.perform(
                        put("/admin/balance/" + saving02.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // RESPONSE HTTP 204 - NO CONTENTS //
                .andReturn();

        Optional<Saving> optionalSaving = savingRepository.findById(saving02.getId());
        assertTrue(optionalSaving.isPresent());

        assertEquals("1500", optionalSaving.get().getInterestRate());
    }

    @Test
    void update_CreditLimit_noReturn() throws Exception {
        creditLimitDTO.setCreditLimit(new BigDecimal("300"));
        String body = objectMapper.writeValueAsString(creditLimitDTO);
        MvcResult mvcResult = mockMvc.perform(
                        put("/admin/credit-cards/" + creditCard.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // RESPONSE HTTP 204 - NO CONTENTS //
                .andReturn();

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCard.getId());
        assertTrue(optionalCreditCard.isPresent());

        /* Run with valid params (Credit Limits < 1000) */
        assertEquals("300", optionalCreditCard.get().getCreditLimit());

        /* Run with wrong valid params (interestRate< 100>) */
        creditLimitDTO.setCreditLimit(new BigDecimal("50"));
        assertEquals("Interest Rate cannot be higher than 0.5. Define a new one", status().isBadRequest());

    }

    @Test
    void change_InterestRate_NoReturn() throws Exception {
        savingInterestRateDTO.setInterestRate(new BigDecimal("0.15"));
        String body = objectMapper.writeValueAsString(savingInterestRateDTO);
        MvcResult mvcResult = mockMvc.perform(
                        put("/admin/accounts/" + saving01.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // RESPONSE HTTP 204 - NO CONTENTS //
                .andReturn();

        Optional<Saving> optionalSaving = savingRepository.findById(saving01.getId());
        assertTrue(optionalSaving.isPresent());

        /* Run with valid params (interestRate<0.5) */
        assertEquals("0.15", optionalSaving.get().getInterestRate());

        /* Run with no valid params (interestRate>0.5) */
        savingInterestRateDTO.setInterestRate(new BigDecimal("0.60"));
        assertEquals("Interest Rate cannot be higher than 0.5. Define a new one", status().isBadRequest());

    }

    @Test
    void update_AccounMinimumBalance_NoReturns() throws Exception {
        savingMinimumBalanceDTO.setMinimumBalance(new Money(new BigDecimal("250")));
        String body = objectMapper.writeValueAsString(savingMinimumBalanceDTO);
        MvcResult mvcResult = mockMvc.perform(
                        put("/admin/accounts/" + +saving01.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // RESPONSE HTTP 204 - NO CONTENTS //
                .andReturn();

        Optional<Saving> optionalSaving = savingRepository.findById(saving01.getId());
        assertTrue(optionalSaving.isPresent());

        /* Run with valid params (MinimumBalance>100) */
        assertEquals("250", optionalSaving.get().getMinimumBalance());
        /* Run with invalid params (interestRate<100) */
        savingMinimumBalanceDTO.setMinimumBalance(new Money(new BigDecimal("50")));
        assertEquals("Interest Rate cannot be higher than 0.5. Define a new one", status().isBadRequest());
    }

    @Test
    void delete_ValidAccount_CourseRemoved() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/admin/accounts/"+ saving02.getId()))
                .andExpect(status().isNoContent())
                .andReturn();
        assertFalse(savingRepository.existsById(saving02.getId()));
    }
}