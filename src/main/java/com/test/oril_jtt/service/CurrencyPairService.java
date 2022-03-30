package com.test.oril_jtt.service;

import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurrencyPairService {
    CurrencyPrice getMinPriceFor(Currency currency);
    CurrencyPrice getMaxPriceFor(Currency currency);
    Page<CurrencyPrice> findAll(Currency currency, Pageable pageable);
}
