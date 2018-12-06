package com.cs.trade.order.matchingengine.service;

import com.cs.trade.order.matchingengine.model.OrderBook;

import java.util.Optional;

public interface OrderMatchingEngine {

    public Optional<OrderBook> match(long bookId);
}
