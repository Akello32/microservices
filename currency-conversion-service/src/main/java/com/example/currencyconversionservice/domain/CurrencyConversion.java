package com.example.currencyconversionservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversion {
    private Long id;
    private String from;
    private String to;
    private BigDecimal quantity;
    private BigDecimal conversion;
    private BigDecimal totalCalculatedAmount;
    private String environment;
}
