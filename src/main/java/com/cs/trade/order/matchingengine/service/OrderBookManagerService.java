package com.cs.trade.order.matchingengine.service;

import com.cs.trade.order.matchingengine.api.requestobj.ExecutionRequest;
import com.cs.trade.order.matchingengine.api.requestobj.OrderRequest;
import com.cs.trade.order.matchingengine.model.Order;
import com.cs.trade.order.matchingengine.model.OrderBook;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

public interface OrderBookManagerService {
    public long createOrderBook(long instrumentId);
    public boolean addOrderToBook(long bookId, OrderRequest order);
    public boolean addExecutionToBook(long bookId, ExecutionRequest execution);

    public Order getOrderStatus(long orderId);
    public ResponseEntity getOrderBookStatus(Long bookId);

    public Flux<OrderBook> getAllOrderBooks();

    public boolean closeOrderBook(long bookId);
}
