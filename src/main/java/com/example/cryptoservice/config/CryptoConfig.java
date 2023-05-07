package com.example.cryptoservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "crypto")
public class CryptoConfig {
    @Getter @Setter
    private List<ConfigCurrency> currencies;

    @Value("https://api.coinlore.net/api/ticker/")
    @Getter @Setter
    private String getCurrencyUrl;
}
