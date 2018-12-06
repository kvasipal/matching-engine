package com.cs.trade.order.matchingengine.service;

import com.cs.trade.order.matchingengine.model.Order;
import com.cs.trade.order.matchingengine.model.OrderExecution;

public interface OrderBookManagerService {
    public long createOrderBook(long instrumentId);
    public boolean addOrderToBook(long bookId, Order order);
    public boolean addExecutionToBook(long bookId, OrderExecution execution);

   // public ResponseEntity getOrderStatus(long orderId) ;
}
