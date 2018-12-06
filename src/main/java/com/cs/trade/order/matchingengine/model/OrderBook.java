package com.cs.trade.order.matchingengine.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
@Data
public class OrderBook {
    @Id
    private Long orderBookId;
    private Long instumentId;
    @Setter
    private OrderBookStatus bookStatus;
    private Optional<List<Order>> orderList;
    private Optional<List<OrderExecution>> executionList;
}
