package com.test.oril_jtt.service;

import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import com.test.oril_jtt.exception.ApiException;
import com.test.oril_jtt.repository.CurrencyPriceRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CurrencyPairServiceImpl implements CurrencyPairService {
    CurrencyPriceRepository repository;

    @Override
    public CurrencyPrice getMinPriceFor(Currency currency) {
        validateCurrency(currency);
        return repository.findFirstByFromOrderByPriceAsc(currency);
    }

    @Override
    public CurrencyPrice getMaxPriceFor(Currency currency) {
        validateCurrency(currency);
        return repository.findFirstByFromOrderByPriceDesc(currency);
    }

    @Override
    public Page<CurrencyPrice> findAll(Currency currency, Pageable pageable) {
        validateCurrency(currency);
        return repository.findAllByFrom(currency, pageable);
    }

    private void validateCurrency(Currency currency) {
        if (!Currency.getCurrencies().contains(currency)) {
            throw new ApiException("'" + currency + "' is not allowed currency", HttpStatus.BAD_REQUEST);
        }
    }

}
