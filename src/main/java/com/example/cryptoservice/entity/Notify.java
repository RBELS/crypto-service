package com.example.cryptoservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CryptoCurrency currency;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Double capturedPrice;
}
