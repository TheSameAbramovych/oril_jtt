package com.test.oril_jtt.service;

import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import com.test.oril_jtt.repository.CurrencyPriceRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CurrencyPairServiceImpl implements CurrencyPairService {
    CurrencyPriceRepository repository;

    @Override
    public CurrencyPrice getMinPriceFor(Currency currency) {
        return repository.findFirstByFromOrderByPriceAsc(currency);
    }

    @Override
    public CurrencyPrice getMaxPriceFor(Currency currency) {
        return repository.findFirstByFromOrderByPriceDesc(currency);
    }
    @Override
    public Page<CurrencyPrice> findAll(Currency currency,Pageable pageable) {
        return repository.findAllByFrom(currency,pageable);
    }

}
