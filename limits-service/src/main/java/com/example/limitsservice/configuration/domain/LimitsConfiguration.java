package com.example.limitsservice.configuration.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LimitsConfiguration {

    private int max;
    private int min;

}
