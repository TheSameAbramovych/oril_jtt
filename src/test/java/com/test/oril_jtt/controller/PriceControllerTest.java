package com.test.oril_jtt.controller;

import com.test.oril_jtt.converter.csv.CSVReportCreator;
import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import com.test.oril_jtt.service.CurrencyPairService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PriceControllerTest {
    @InjectMocks
    private PriceController controller;

    @Mock
    private CurrencyPairService currencyPairService;

    MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void minPrice() throws Exception {
        CurrencyPrice currencyPrice = new CurrencyPrice(Currency.BTC, Currency.USD, new BigDecimal(10), new Date());

        when(currencyPairService.getMinPriceFor(Currency.BTC)).thenReturn(currencyPrice);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/minprice")
                        .param("name", Currency.BTC.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.from").value(currencyPrice.getFrom().name()))
                .andExpect(jsonPath("$.to").value(currencyPrice.getTo().name()))
                .andExpect(jsonPath("$.price").value(currencyPrice.getPrice()))
                .andExpect(jsonPath("$.date").isNotEmpty());
    }

    @Test
    public void maxPrice() throws Exception {
        CurrencyPrice currencyPrice = new CurrencyPrice(Currency.BTC, Currency.USD, new BigDecimal(10), new Date());

        when(currencyPairService.getMaxPriceFor(Currency.BTC)).thenReturn(currencyPrice);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/maxprice")
                        .param("name", Currency.BTC.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.from").value(currencyPrice.getFrom().name()))
                .andExpect(jsonPath("$.to").value(currencyPrice.getTo().name()))
                .andExpect(jsonPath("$.price").value(currencyPrice.getPrice()))
                .andExpect(jsonPath("$.date").isNotEmpty());
    }


    @Test
    public void history() throws Exception {
        CurrencyPrice currencyPrice = new CurrencyPrice(Currency.BTC, Currency.USD, new BigDecimal(10), new Date());

        ArgumentCaptor<Pageable> pageRequestArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        when(currencyPairService.findAll(eq(Currency.BTC), pageRequestArgumentCaptor.capture())).thenReturn(new PageImpl<>(List.of(currencyPrice)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/")
                        .param("name", Currency.BTC.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].from").value(currencyPrice.getFrom().name()))
                .andExpect(jsonPath("$[*].to").value(currencyPrice.getTo().name()))
                .andExpect(jsonPath("$[*].price").value(currencyPrice.getPrice().intValue()))
                .andExpect(jsonPath("$[*].date").isNotEmpty());

        Pageable pageable = pageRequestArgumentCaptor.getValue();
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.by("price").ascending(), pageable.getSort());
    }

}