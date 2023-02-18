package com.ironhack.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.controller.interfaces.AdminController;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Role;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transfer;
import com.ironhack.repository.Users.RoleRepository;
import com.ironhack.repository.Utils.TransferenceRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class ThirdPartyControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    TransferenceRepository transferenceRepository;
    @Autowired
    RoleRepository roleRepository;
    private Role adminUserRole, holderUserRole;
    private CreditCard creditCard;
    private AdminController adminController;
    private AccountHolder accountHolder1, accountHolder2, accountHolder3;
    Transfer transfer;
    ThirdParty thirdParty;

    @BeforeEach
    void setUp() {
        thirdParty = new ThirdParty(new Money(new BigDecimal("500")), "AAB123456");
        transfer = new Transfer(new BigDecimal("100"), 2L, "Emma", 1L,12345);
        accountHolder2 = new AccountHolder("Pavlo Menendez", "pavlomenendez", passwordEncoder.encode("pavlomenendez88"), LocalDate.of(1988,06,14), null, null);
        accountHolder3 = new AccountHolder("Jeremias Fabbro", "jerefabbro", passwordEncoder.encode("jerefabbro98"), LocalDate.of(1998,02,21), null, null);
        /* SET USERS AND TIPES OF ROLES */
        adminUserRole = new Role("ADMIN");
        roleRepository.save(adminUserRole);
        holderUserRole = new Role("ACCOUNT_HOLDER");
        roleRepository.save(holderUserRole);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void get_transfer_ChangeBalance() throws Exception {
        String body = objectMapper.writeValueAsString(transfer);
        MvcResult mvcResult = mockMvc.perform(
                    post("/User/accounts/thirdparty/transfer/)").param("hashKey", "AAB123456")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                )
            .andExpect(status().isCreated()) // RESPONSE HTTP 204 - NO CONTENTS //
            .andReturn();

        assertEquals(1, transferenceRepository.findAll().size());

}
}