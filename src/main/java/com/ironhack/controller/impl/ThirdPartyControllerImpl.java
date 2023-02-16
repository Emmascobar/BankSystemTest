package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.ThirdPartyController;
import com.ironhack.service.interfaces.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class ThirdPartyControllerImpl implements ThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;


    /* Transferences POSTMAPPING of ThirdParty accounts */
    //AQUI TENGO QUE PONER EL SECRET KEY PARA AUTENTIFICAR.?¿¿?¿¿?¿?¿?¿?¿?¿
    @PostMapping("User/accounts/thirdparty/{id}/transference")
    @ResponseStatus(HttpStatus.CREATED)
    public void transference(Long id, BigDecimal amount, Long destinationId, Integer destinationSecretKey) {
        thirdPartyService.transference(id, amount, destinationId, destinationSecretKey);
    }
}
