package com.test.oril_jtt.service;

import com.test.oril_jtt.entity.CEXCurrencyPrice;
import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import com.test.oril_jtt.repository.CurrencyPriceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.test.oril_jtt.service.CEXCurrencyFetcherService.MAIN_CURRENCY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CEXCurrencyFetcherServiceTest {

    @InjectMocks
    private CEXCurrencyFetcherService fetcherService;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private CurrencyPriceRepository repository;

    @Test
    public void fetchCurrencyPrice_ok(){
        List<CEXCurrencyPrice> responses = new ArrayList<>();

        Currency.getCurrencies().forEach(currency -> {
            CEXCurrencyPrice currencyPrice = new CEXCurrencyPrice();
            currencyPrice.setPrice(new BigDecimal(currency.ordinal()));
            currencyPrice.setFrom(currency);
            responses.add(currencyPrice);
            currencyPrice.setTo(MAIN_CURRENCY);
            when(restTemplate.getForObject("https://cex.io/api/last_price/" + currency + "/" + MAIN_CURRENCY, CEXCurrencyPrice.class))
                    .thenReturn(currencyPrice);
                }
        );

        fetcherService.fetchCurrencyPrice();

        ArgumentCaptor<CurrencyPrice> currencyPriceArgumentCaptor = ArgumentCaptor.forClass(CurrencyPrice.class);
        verify(repository, times(responses.size())).save(currencyPriceArgumentCaptor.capture());

        List<CurrencyPrice> currencyPriceList = currencyPriceArgumentCaptor.getAllValues();
        assertEquals(responses.size(), currencyPriceList.size());

        IntStream.range(0, responses.size()).forEach(i->{
            CEXCurrencyPrice cexCurrencyPrice = responses.get(i);
            CurrencyPrice currencyPrice = currencyPriceList.get(i);
            assertEquals(cexCurrencyPrice.getPrice(), currencyPrice.getPrice());
            assertEquals(cexCurrencyPrice.getFrom(), currencyPrice.getFrom());
            assertEquals(cexCurrencyPrice.getTo(), currencyPrice.getTo());
        });
    }

    @Test
    public void fetchCurrencyPrice_duplicate(){
        Currency.getCurrencies().forEach(currency -> {
            CEXCurrencyPrice currencyPrice = new CEXCurrencyPrice();
            currencyPrice.setPrice(new BigDecimal(currency.ordinal()));
            currencyPrice.setFrom(currency);
            currencyPrice.setTo(MAIN_CURRENCY);
            when(restTemplate.getForObject("https://cex.io/api/last_price/" + currency + "/" + MAIN_CURRENCY, CEXCurrencyPrice.class))
                    .thenReturn(currencyPrice);
                }
        );

        Currency from = Currency.getCurrencies().get(0);
        when(repository.findFirstByFromOrderByDateDesc(from)).thenReturn(new CurrencyPrice(from, MAIN_CURRENCY, new BigDecimal(from.ordinal()), null));

        fetcherService.fetchCurrencyPrice();

        ArgumentCaptor<CurrencyPrice> currencyPriceArgumentCaptor = ArgumentCaptor.forClass(CurrencyPrice.class);
        verify(repository, times(2)).save(currencyPriceArgumentCaptor.capture());

        List<CurrencyPrice> currencyPriceList = currencyPriceArgumentCaptor.getAllValues();
        assertEquals(2, currencyPriceList.size());
    }

    @Test
    public void fetchCurrencyPrice_fail(){
        Currency.getCurrencies().forEach(currency -> {
            when(restTemplate.getForObject("https://cex.io/api/last_price/" + currency + "/" + MAIN_CURRENCY, CEXCurrencyPrice.class))
                    .thenThrow(new RuntimeException());
                }
        );

        fetcherService.fetchCurrencyPrice();

        ArgumentCaptor<CurrencyPrice> currencyPriceArgumentCaptor = ArgumentCaptor.forClass(CurrencyPrice.class);
        verify(repository, never()).save(currencyPriceArgumentCaptor.capture());
    }
}