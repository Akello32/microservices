package com.example.currencyexchangeservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CurrencyExchange {
    @Id
    private Long id;
    @Column(name = "currency_from")
    private String from;
    @Column(name = "currency_to")
    private String to;
    private BigDecimal conversion;
    private String environment;
}
