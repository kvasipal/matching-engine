package com.cs.trade.order.matchingengine.service.impl;

import com.cs.trade.order.matchingengine.model.Order;
import com.cs.trade.order.matchingengine.model.OrderBook;
import com.cs.trade.order.matchingengine.model.OrderBookStatus;
import com.cs.trade.order.matchingengine.model.OrderExecution;
import com.cs.trade.order.matchingengine.repository.OrderBookRepository;
import com.cs.trade.order.matchingengine.repository.OrderBookRepositoryImpl;
import com.cs.trade.order.matchingengine.service.OrderBookManagerService;
import com.cs.trade.order.matchingengine.service.OrderMatchingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderBookManagerServiceImpl  implements OrderBookManagerService {
    @Autowired
    private OrderBookRepositoryImpl orderBookRepository;
    @Autowired
    private OrderMatchingEngine matchingEngine;

    @Override
    public long createOrderBook(long instrumentId) {
        OrderBook book = OrderBook.builder()
                .instumentId(instrumentId)
                .bookStatus(OrderBookStatus.OPEN)
                .orderBookId(new Random().nextLong())
                .orderList(Optional.of(new ArrayList<Order>()))
                .executionList(Optional.of(new ArrayList<OrderExecution>()))
                .build();
        //save to db
        orderBookRepository.save(book);
        return book.getOrderBookId();
    }

    @Override
    public boolean addOrderToBook(long bookId, Order order) {
        //TODO: order validator
        Optional<OrderBook> bookOptional = orderBookRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            OrderBook orderBook = bookOptional.get();
            if(isBookOpen(orderBook)) {
                if (orderBook.getOrderList().isPresent()) {
                    orderBook.getOrderList().get().add(order);
                }
                //TODO: first time list
                orderBookRepository.save(orderBook);
                return true;
            }
            //TODO: if book not aavilable
            return false;
        }
        return false;
    }

    @Override
    public boolean addExecutionToBook(long bookId, OrderExecution execution) {
        //TODO: execution validator
        Optional<OrderBook> bookOptional = orderBookRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            OrderBook orderBook = bookOptional.get();
            if(isBookClosed(orderBook) && !isBookExecuted(orderBook)) {
                if(orderBook.getExecutionList().isPresent()) {
                    orderBook.getExecutionList().get().add(execution);
                }
                //TODO: ExecutionAddedToOrderEvent executionAddedToOrderEvent =
                //TODO: Call matcher engine
                orderBookRepository.save(orderBook);
                matchingEngine.match(bookId);
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean isBookClosed(OrderBook orderBook) {
        return orderBook.getBookStatus().equals(OrderBookStatus.CLOSE);
    }

    private boolean isBookOpen(OrderBook orderBook) {
        return orderBook.getBookStatus().equals(OrderBookStatus.OPEN);
    }

    private boolean isBookExecuted(OrderBook orderBook) {
        return orderBook.getBookStatus().equals(OrderBookStatus.EXECUTED);
    }
}
