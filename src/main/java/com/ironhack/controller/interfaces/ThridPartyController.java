package com.ironhack.controller.interfaces;

import com.ironhack.model.Utils.Transfer;
import org.springframework.security.core.Authentication;

public interface ThridPartyController {

    Transfer transfer(Authentication authentication, String hashKey, Transfer transfer);
}
