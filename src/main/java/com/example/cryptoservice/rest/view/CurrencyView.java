package com.example.cryptoservice.rest.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CurrencyView {
    private long id;
    private String symbol;
    private double price;
}
