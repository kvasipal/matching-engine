package com.cs.trade.order.matchingengine.repository;


import com.cs.trade.order.matchingengine.model.OrderBook;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class OrderBookRepositoryImpl {

    private static final Map<Long,OrderBook> orderBooks =  new HashMap<Long, OrderBook>();

    public Flux<OrderBook> findAll() {
        return Flux.fromIterable(orderBooks.values());
    }

    public Optional<OrderBook> findById(long bookId) {
        return Optional.of(orderBooks. get(bookId));
    }

    public void save(OrderBook orderBook) {
        orderBooks.put(orderBook.getOrderBookId(), orderBook);
    }
}
