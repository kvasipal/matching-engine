package com.cs.trade.order.matchingengine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class OrderExecution {
    private BigDecimal price;
    private double quantity;
}