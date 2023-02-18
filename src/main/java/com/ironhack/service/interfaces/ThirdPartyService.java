package com.ironhack.service.interfaces;

import com.ironhack.model.Utils.Transfer;

public interface ThirdPartyService {
    Transfer transfer(String hashKey, Transfer transfer);
}
