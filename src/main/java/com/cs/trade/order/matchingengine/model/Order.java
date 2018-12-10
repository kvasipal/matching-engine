package com.cs.trade.order.matchingengine.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@Data
//@Entity
public class Order {
    //@Id
    private long orderId;
    private OfferOrder offerOrder;
    private OrderStatus orderStatus;
    private double remainingQty;
    private double executedQty;
    private BigDecimal executedPrice;
}
