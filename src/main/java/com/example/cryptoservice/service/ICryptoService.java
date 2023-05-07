package com.example.cryptoservice.service;

import com.example.cryptoservice.dto.CurrencyDTO;
import com.example.cryptoservice.dto.NotifyDTO;

import java.util.List;

public interface ICryptoService {
    List<CurrencyDTO> getCurrencyList();
    CurrencyDTO getCurrencyById(long currencyId);
    boolean notify(NotifyDTO userCurrency);
}
