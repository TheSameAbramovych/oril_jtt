package com.test.oril_jtt.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum Currency {
    USD(true),
    BTC,
    ETH,
    XRP;

    @Getter
    private final boolean main;

    Currency() {
        this(false);
    }

    Currency(boolean main) {
        this.main = main;
    }

    public static Currency getMainCurrency() {
        return Arrays.stream(values()).filter(Currency::isMain).findFirst().orElse(null);
    }

    public static List<Currency> getCurrencies() {
        return Arrays.stream(values()).filter(Predicate.not(Currency::isMain)).collect(Collectors.toList());
    }
}
