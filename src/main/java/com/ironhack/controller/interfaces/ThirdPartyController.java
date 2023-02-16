package com.ironhack.controller.interfaces;

import java.math.BigDecimal;

public interface ThirdPartyController {

    void transference(Long id, BigDecimal amount, Long destinationId, Integer destinationSecretKey);
}
