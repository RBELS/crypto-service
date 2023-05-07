package com.example.cryptoservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponseEntity {
    private long id;
    private String symbol;
    @JsonProperty("price_usd")
    private double priceUsd;
    private String name;
}
