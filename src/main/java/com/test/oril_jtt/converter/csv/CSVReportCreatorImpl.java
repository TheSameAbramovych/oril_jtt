package com.test.oril_jtt.converter.csv;

import com.test.oril_jtt.entity.Currency;
import com.test.oril_jtt.service.CurrencyPairService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;


@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CSVReportCreatorImpl implements CSVReportCreator {
    static List<String> CSV_HEADERS = List.of("Currency", "Min", "Max");
    CurrencyPairService currencyPairService;

    @Transactional
    public byte[] createCsvReport() {
        return Stream.concat(
                        Stream.of(CSV_HEADERS),
                        Currency.getCurrencies().stream()
                                .map(this::convertToRow))
                .map(this::convertToCSV)
                .collect(Collectors.joining("\n"))
                .getBytes();
    }

    private List<String> convertToRow(Currency currency) {
        String min = currencyPairService.getMinPriceFor(currency).getPrice().toString();
        String max = currencyPairService.getMaxPriceFor(currency).getPrice().toString();
        return List.of(currency.name(), min, max);
    }

    public String convertToCSV(List<String> data) {
        return data.stream()
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(", "));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
