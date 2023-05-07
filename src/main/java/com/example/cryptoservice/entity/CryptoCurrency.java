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
public class CryptoCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long cryptoId;

    @Column(length = 16, unique = true, nullable = false)
    private String symbol;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String name;
}
