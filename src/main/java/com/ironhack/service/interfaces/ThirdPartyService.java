package com.ironhack.service.interfaces;

import java.math.BigDecimal;

public interface ThirdPartyService {
    void transference(Long OwnId, BigDecimal amount, Long destinationId, Integer destinationSecretKey);
}
