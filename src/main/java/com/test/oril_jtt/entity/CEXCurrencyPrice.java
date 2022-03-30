package com.test.oril_jtt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CEXCurrencyPrice {
    @JsonProperty("curr1")
    private Currency from;
    @JsonProperty("curr2")
    private Currency to;
    @JsonProperty("lprice")
    private BigDecimal price;
}
