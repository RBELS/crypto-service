package com.example.cryptoservice.service;

import com.example.cryptoservice.dto.CurrencyDTO;
import com.example.cryptoservice.dto.NotifyDTO;
import com.example.cryptoservice.entity.CryptoCurrency;
import com.example.cryptoservice.entity.Notify;
import com.example.cryptoservice.repository.CryptoCurrencyRepository;
import com.example.cryptoservice.repository.NotifyRepository;
import com.example.cryptoservice.request.ForeignCryptoAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CryptoService implements ICryptoService {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final NotifyRepository notifyRepository;
    private final ForeignCryptoAPI foreignCryptoAPI;

    public CryptoService(CryptoCurrencyRepository cryptoCurrencyRepository, NotifyRepository notifyRepository, ForeignCryptoAPI foreignCryptoAPI) {
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
        this.notifyRepository = notifyRepository;
        this.foreignCryptoAPI = foreignCryptoAPI;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initCryptoCurrencyRepo() {

        if (cryptoCurrencyRepository.countAllBy() == 0) {
            log.info("INIT START");
            Collection<CurrencyDTO> currencyDTOList = foreignCryptoAPI.requestCurrenciesFromConfig().values();

            // Map CurrencyDTO to CryptoCurrency
            List<CryptoCurrency> cryptoCurrencyList = currencyDTOList.stream()
                    .map(currencyDTO -> new CryptoCurrency(
                            null, currencyDTO.getId(),
                            currencyDTO.getSymbol(), currencyDTO.getPrice(),
                            currencyDTO.getName())
                    ).toList();

            cryptoCurrencyRepository.saveAll(cryptoCurrencyList);
            log.info("INIT END");
        }
    }

    public List<CurrencyDTO> getCurrencyList() {
        List<CryptoCurrency> entityList = cryptoCurrencyRepository.findAll();
        return entityList.stream()
                .map(cryptoCurrency -> new CurrencyDTO(
                        cryptoCurrency.getCryptoId(),
                        cryptoCurrency.getSymbol(),
                        cryptoCurrency.getPrice(),
                        cryptoCurrency.getName())
                ).toList();
    }

    public CurrencyDTO getCurrencyById(long currencyId) {
        CryptoCurrency entity = cryptoCurrencyRepository.findByCryptoId(currencyId);
        if (Objects.isNull(entity)) return null;
        return new CurrencyDTO(
                entity.getCryptoId(),
                entity.getSymbol(),
                entity.getPrice(),
                entity.getName()
        );
    }

    public boolean notify(NotifyDTO userCurrency) {
        CryptoCurrency currencyEntity = cryptoCurrencyRepository.findBySymbol(userCurrency.getSymbol());
        if (Objects.isNull(currencyEntity) || userCurrency.getUsername().isBlank()) {
            return false;
        }
        Notify notifyEntity = new Notify(null, currencyEntity, userCurrency.getUsername(), currencyEntity.getPrice());
        notifyRepository.save(notifyEntity);

        return true;
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1)
    protected void updateCryptoPrices() {
        Map<Long, CurrencyDTO> currencyDTOMap = foreignCryptoAPI.requestCurrenciesFromConfig();
        List<CryptoCurrency> entityList = cryptoCurrencyRepository.findAll();
        for (CryptoCurrency entity : entityList) {
            CurrencyDTO tempDTO = currencyDTOMap.get(entity.getCryptoId());
            if (Objects.isNull(tempDTO)) continue;

            entity.setPrice(tempDTO.getPrice());
        }
        cryptoCurrencyRepository.saveAll(entityList);
        checkUserPrices();
    }

    private void checkDiffPercentage(Notify notifyEntity) {
        final double LOG_PERCENTAGE_BORDER = 1.0;

        double diffPercentage = (notifyEntity.getCurrency().getPrice() / notifyEntity.getCapturedPrice() - 1) * 100.0;
        if (Math.abs(diffPercentage) > LOG_PERCENTAGE_BORDER) {
            log.warn(String.format("%d\t%s\t%.2f", notifyEntity.getCurrency().getCryptoId(), notifyEntity.getUsername(), diffPercentage));
        }
    }

    private void checkUserPrices() {
        List<Notify> registeredUsers = notifyRepository.findAll();
        registeredUsers.forEach(this::checkDiffPercentage);
    }
}
