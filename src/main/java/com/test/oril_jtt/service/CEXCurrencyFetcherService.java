package com.test.oril_jtt.service;

import com.test.oril_jtt.entity.CEXCurrencyPrice;
import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import com.test.oril_jtt.repository.CurrencyPriceRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Log4j2
@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CEXCurrencyFetcherService implements CurrencyFetcherService {
    static Currency MAIN_CURRENCY = Currency.getMainCurrency();
    RestTemplate restTemplate;
    CurrencyPriceRepository repository;

    @Scheduled(fixedDelay = 60000)
    public void fetchCurrencyPrice() {
        Currency.getCurrencies().stream()
                .map(this::getCurrencyPrice)
                .filter(Objects::nonNull)
                .filter(this::isPriceNotEquals)
                .map(this::transform)
                .forEach(repository::save);
    }

    private boolean isPriceNotEquals(CEXCurrencyPrice cexCurrencyPrice) {
        CurrencyPrice currencyPrice = repository.findFirstByFromOrderByDateDesc(cexCurrencyPrice.getFrom());
        return currencyPrice == null || currencyPrice.getPrice() == null ||
                currencyPrice.getPrice().compareTo(cexCurrencyPrice.getPrice()) != 0;
    }

    private CurrencyPrice transform(CEXCurrencyPrice cexCurrencyPrice) {
        return new CurrencyPrice(cexCurrencyPrice.getFrom(), cexCurrencyPrice.getTo(), cexCurrencyPrice.getPrice(), new Date());
    }

    private CEXCurrencyPrice getCurrencyPrice(Currency currency) {
        try {
            log.info("Fetching {} price...", currency);
            return restTemplate.getForObject("https://cex.io/api/last_price/" + currency + "/" + MAIN_CURRENCY, CEXCurrencyPrice.class);
        } catch (Exception e) {
            log.error("Failed fetching price fore {}", currency);
            return null;
        }
    }
}
