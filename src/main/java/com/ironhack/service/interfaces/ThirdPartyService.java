package com.ironhack.service.interfaces;

import java.math.BigDecimal;

public interface ThirdPartyService {
    void transference(Long id, BigDecimal amount, Long destinationId, Integer destinationSecretKey);
}
