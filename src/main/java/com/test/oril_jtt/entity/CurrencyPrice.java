package com.test.oril_jtt.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@Data
public final class CurrencyPrice {
    @Id
    private String id;
    private final Currency from;
    private final Currency to;
    private final BigDecimal price;
    private final Date date;

}
