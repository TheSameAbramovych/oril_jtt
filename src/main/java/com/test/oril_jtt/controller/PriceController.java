package com.test.oril_jtt.controller;

import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.entity.CurrencyPrice;
import com.test.oril_jtt.converter.csv.CSVReportCreator;
import com.test.oril_jtt.service.CurrencyPairService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PriceController {
    CurrencyPairService currencyPairService;
    CSVReportCreator csvReportCreator;

    @GetMapping("/minprice")
    public CurrencyPrice minPrice(@RequestParam(name = "name") Currency currency) {
        return currencyPairService.getMinPriceFor(currency);
    }

    @GetMapping("/maxprice")
    public CurrencyPrice maxPrice(@RequestParam(name = "name") Currency currency) {
        return currencyPairService.getMaxPriceFor(currency);
    }

    @GetMapping()
    public List<CurrencyPrice> history(@RequestParam(name = "name")Currency currency,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("price").ascending());
        Page<CurrencyPrice> currencyPrices = currencyPairService.findAll(currency, pageRequest);
        return currencyPrices.getContent();
    }

    @GetMapping(value = "csv", produces = "text/csv")
    public byte[] generateCsv() {
        return csvReportCreator.createCsvReport();
    }

}
