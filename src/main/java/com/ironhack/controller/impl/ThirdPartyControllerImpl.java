package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.ThridPartyController;
import com.ironhack.model.Utils.Transfer;
import com.ironhack.service.interfaces.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class ThirdPartyControllerImpl implements ThridPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("User/accounts/thirdparty/transfer/{hashkey}")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer transfer(Authentication authentication, @RequestParam String hashKey, @RequestBody Transfer transfer) {
        return thirdPartyService.transfer(hashKey, transfer);
    }

}

