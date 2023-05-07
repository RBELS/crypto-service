package com.example.cryptoservice.repository;


import com.example.cryptoservice.entity.CryptoCurrency;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoCurrencyRepository extends ListCrudRepository<CryptoCurrency, Long> {
    long countAllBy();
    CryptoCurrency findByCryptoId(long cryptoId);
    CryptoCurrency findBySymbol(String symbol);
}
