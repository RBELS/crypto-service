package com.example.cryptoservice.rest.view;

import lombok.*;

@AllArgsConstructor
@Getter @Setter
public class NotifyRequestBody {
    @NonNull
    private String username;
    @NonNull
    private String symbol;
}
