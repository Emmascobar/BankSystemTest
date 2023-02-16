package com.ironhack.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.controller.interfaces.AdminController;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transference;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private CreditCard creditCard;
    private AdminController adminController;
    private AccountHolder accountHolder1, accountHolder2, accountHolder3;
    Transference transference;
    ThirdParty thirdParty;

    @BeforeEach
    void setUp() {
        thirdParty = new ThirdParty(new Money(new BigDecimal("500")), "AAB123456");
        transference = new Transference(new BigDecimal("100"), 2L, 3L);
        accountHolder2 = new AccountHolder("Pavlo Menendez", "pavlomenendez", passwordEncoder.encode("pavlomenendez88"), LocalDate.of(1988,06,14), null, null);
        accountHolder3 = new AccountHolder("Jeremias Fabbro", "jerefabbro", passwordEncoder.encode("jerefabbro98"), LocalDate.of(1998,02,21), null, null);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void get_transference_ChangeBalance() throws Exception {
        Optional<Transference> optionalTransference = transferenceRepository.findById(transference.getId());
        String body = objectMapper.writeValueAsString(optionalTransference);
        MvcResult mvcResult = mockMvc.perform(
                    put("User/accounts/thirdparty/" + transference.getId() +"/transference")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent()) // RESPONSE HTTP 204 - NO CONTENTS //
            .andReturn();

    assertTrue(optionalTransference.isPresent());
    assertEquals("100", optionalTransference.get().getAmount());
    assertEquals(2L, optionalTransference.get().getDestinationId());
}
}