package com.example.cryptoservice.rest;

import com.example.cryptoservice.dto.CurrencyDTO;
import com.example.cryptoservice.dto.NotifyDTO;
import com.example.cryptoservice.rest.view.NotifyRequestBody;
import com.example.cryptoservice.service.CryptoService;
import com.example.cryptoservice.rest.view.CurrencyView;
import com.example.cryptoservice.service.ICryptoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    private final ICryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Iterable<CurrencyView> getAvailableCryptoList() {
        List<CurrencyDTO> dtoList = cryptoService.getCurrencyList();
        return dtoList.stream()
                .map(currencyDTO -> new CurrencyView(
                        currencyDTO.getId(),
                        currencyDTO.getSymbol(),
                        currencyDTO.getPrice())
                ).toList();
    }

    @GetMapping(
            value = "/{cryptoId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CurrencyView> getCryptoById(@PathVariable(name = "cryptoId") Long cryptoId) {
        CurrencyDTO dto = cryptoService.getCurrencyById(cryptoId);
        if (Objects.isNull(dto)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CurrencyView result = new CurrencyView(dto.getId(), dto.getSymbol(), dto.getPrice());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(
            value = "/track"
    )
    public ResponseEntity<Void> notify(
            @RequestBody NotifyRequestBody body
    ) {
        boolean resultOK = cryptoService.notify(new NotifyDTO(body.getUsername(), body.getSymbol()));
        if (resultOK)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
