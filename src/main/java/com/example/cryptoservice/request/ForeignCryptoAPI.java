package com.example.cryptoservice.request;

import com.example.cryptoservice.config.ConfigCurrency;
import com.example.cryptoservice.config.CryptoConfig;
import com.example.cryptoservice.dto.CurrencyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class ForeignCryptoAPI {

    private final String getCurrencyAPIURL;

    public ForeignCryptoAPI(CryptoConfig cryptoConfig) {
        List<ConfigCurrency> configCurrencyList = cryptoConfig.getCurrencies();

        //build url
        StringBuilder requestUrl = new StringBuilder(cryptoConfig.getGetCurrencyUrl());
        requestUrl.append("?id=");
        for (ConfigCurrency configCurrency : configCurrencyList) {
            requestUrl.append(configCurrency.getId());
            requestUrl.append(",");
        }
        requestUrl.deleteCharAt(requestUrl.length()-1);
        //build url

        this.getCurrencyAPIURL = requestUrl.toString();
    }

    public Map<Long, CurrencyDTO> requestCurrenciesFromConfig() {
        RestTemplate template = new RestTemplate();
        CurrencyResponseEntity[] currencyArr = template.getForObject(getCurrencyAPIURL, CurrencyResponseEntity[].class);
        if (Objects.isNull(currencyArr)) {
            return new HashMap<>();
        }

        //map response array to DTO list
        Map<Long, CurrencyDTO> currencyDTOResult = new HashMap<>();
        Arrays.stream(currencyArr).forEach(responseEntity -> currencyDTOResult.put(
                responseEntity.getId(),
                new CurrencyDTO(
                    responseEntity.getId(),
                    responseEntity.getSymbol(),
                    responseEntity.getPriceUsd(),
                    responseEntity.getName())
                ));

        return currencyDTOResult;
    }
}
