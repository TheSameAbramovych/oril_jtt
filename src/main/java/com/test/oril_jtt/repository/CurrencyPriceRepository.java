package com.test.oril_jtt.repository;

import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyPriceRepository extends PagingAndSortingRepository<CurrencyPrice, Long> {
    CurrencyPrice findFirstByFromOrderByPriceDesc(Currency currency);
    CurrencyPrice findFirstByFromOrderByPriceAsc(Currency currency);
    Page<CurrencyPrice> findAllByFrom(Currency currency,Pageable pageable);
    CurrencyPrice findFirstByFromOrderByDateDesc(Currency from);
}
