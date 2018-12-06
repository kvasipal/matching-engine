package com.cs.trade.order.matchingengine.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
public class OfferOrder {
    private double quantity;
    private long instumentId;
    private Date entryDate;
    private BigDecimal orderPrice;
    private OrderType orderType;
}
