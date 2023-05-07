package com.example.cryptoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {
    private long id;
    private String symbol;
    private double price;
    private String name;
}
