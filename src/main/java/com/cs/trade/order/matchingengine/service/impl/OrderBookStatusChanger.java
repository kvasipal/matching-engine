package com.cs.trade.order.matchingengine.service.impl;

import com.cs.trade.order.matchingengine.model.Order;
import com.cs.trade.order.matchingengine.model.OrderBook;
import com.cs.trade.order.matchingengine.model.OrderBookStatus;
import com.cs.trade.order.matchingengine.model.OrderExecution;
import com.cs.trade.order.matchingengine.repository.OrderBookRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderBookStatusChanger {
    @Autowired
    private OrderBookRepositoryImpl orderBookRepository;


    public Optional<OrderBookStatus> getBookStatus(Long bookId) {
        Optional<OrderBook> orderBook = orderBookRepository.findById(bookId);
        if (orderBook.isPresent()) {
            return Optional.ofNullable(orderBook.get().getBookStatus());
        }
        return Optional.empty();
    }

    public Optional<String> changeOrderBookState(long bookId) {
        Optional<OrderBook> orderBookOpt = orderBookRepository.findById(bookId);
        if (orderBookOpt.isPresent()) {
            OrderBook book = orderBookOpt.get();
            Optional<Double> accumilatedOrderQty = getAccumilatedOrderQty(book);
            Optional<Double> accumilatedExecutedQty = getAccumilatedExecutedQty(book);
            if(accumilatedOrderQty.isPresent() && accumilatedExecutedQty.isPresent()) {
                if(accumilatedOrderQty.get() <= accumilatedExecutedQty.get()) {
                    book.setBookStatus(OrderBookStatus.EXECUTED);
                    return Optional.of(OrderBookStatus.EXECUTED.toString());
                }
            }
            return Optional.of(book.getBookStatus().toString());
        }
        return Optional.empty();
    }

    public Optional<Double> getAccumilatedExecutedQty(OrderBook orderBook) {
        double accumilatedExecutedQty = 0;
        for (OrderExecution execution: orderBook.getExecutionList().get()) {
            accumilatedExecutedQty = accumilatedExecutedQty + execution.getQuantity();
        }
        return Optional.of(accumilatedExecutedQty);
    }

    public Optional<Double> getAccumilatedOrderQty(OrderBook orderBook) {
        double accumilatedOrderQty = 0;
            for (Order order: orderBook.getOrderList().get()) {
                accumilatedOrderQty = accumilatedOrderQty + order.getOfferOrder().getQuantity();
            }
        return Optional.of(accumilatedOrderQty);
    }
}
