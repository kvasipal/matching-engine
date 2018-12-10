package com.cs.trade.order.matchingengine.api.requestobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExecutionRequest {
    private BigDecimal price;
    private double quantity;
}
