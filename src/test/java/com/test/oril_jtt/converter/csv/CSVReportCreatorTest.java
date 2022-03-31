package com.test.oril_jtt.converter.csv;

import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import com.test.oril_jtt.service.CurrencyPairService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;

import static com.test.oril_jtt.entity.Currency.USD;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CSVReportCreatorTest {
    private static final String EXPECTED_REPORT = "Currency, Min, Max\nBTC, 1, 2\nETH, 2, 3\nXRP, 3, 4";

    @InjectMocks
    CSVReportCreatorImpl csvReportCreator;

    @Mock
    CurrencyPairService currencyPairService;

    @Test
    public void createCsvReport() {
        Currency.getCurrencies().forEach(currency -> {
            when(currencyPairService.getMaxPriceFor(currency))
                    .thenReturn(new CurrencyPrice(currency, USD, new BigDecimal(currency.ordinal() + 1), new Date()));
            when(currencyPairService.getMinPriceFor(currency))
                    .thenReturn(new CurrencyPrice(currency, USD, new BigDecimal(currency.ordinal()), new Date()));
        });

        byte[] csvReport = csvReportCreator.createCsvReport();

        assertEquals(EXPECTED_REPORT, new String(csvReport));
    }
}