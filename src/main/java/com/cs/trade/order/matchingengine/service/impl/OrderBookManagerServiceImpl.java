package com.cs.trade.order.matchingengine.service.impl;

import com.cs.trade.order.matchingengine.api.requestobj.ExecutionRequest;
import com.cs.trade.order.matchingengine.api.requestobj.OrderRequest;
import com.cs.trade.order.matchingengine.model.*;
import com.cs.trade.order.matchingengine.repository.OrderBookRepository;
import com.cs.trade.order.matchingengine.repository.OrderBookRepositoryImpl;
import com.cs.trade.order.matchingengine.service.OrderBookManagerService;
import com.cs.trade.order.matchingengine.service.OrderMatchingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
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
    public boolean addOrderToBook(long bookId, OrderRequest reqOrder) {
        //TODO: order validator
        OfferOrder immutableOrder = OfferOrder.builder()
                .instumentId(reqOrder.getInstumentId())
                .quantity(reqOrder.getQuantity())
                .orderType(OrderType.valueOf(reqOrder.getOrderType()))
                .orderPrice(reqOrder.getOrderPrice())
                .entryDate(reqOrder.getEntryDate())
                .build();
        Order order = Order.builder()
                .offerOrder(immutableOrder)
                .orderStatus(OrderStatus.VALID)
                .remainingQty(immutableOrder.getQuantity())
                .executedQty(0)
                .orderId(new Random().nextLong())
                .build();
        Optional<OrderBook> bookOptional = orderBookRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            OrderBook orderBook = bookOptional.get();
            if(isBookOpen(orderBook)) {
                if (orderBook.getOrderList().isPresent()) {
                    orderBook.getOrderList().get().add(order);
                }
                orderBookRepository.save(orderBook);
                return true;
            }
            //TODO: if book not available
            return false;
        }
        return false;
    }

    @Override
    public boolean addExecutionToBook(long bookId, ExecutionRequest executionReq) {
        OrderExecution execution = OrderExecution.builder()
                .quantity(executionReq.getQuantity())
                .price(executionReq.getPrice())
                .build();
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

    @Override
    public Order getOrderStatus(long orderId) {
        return null;
    }

    @Override
    public ResponseEntity getOrderBookStatus(Long bookId) {
        Optional<OrderBook> bookOptional = orderBookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            return new ResponseEntity(bookOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("BOOK NOT FOUND", HttpStatus.OK);
        }
    }

    @Override
    public Flux<OrderBook> getAllOrderBooks() {
        return orderBookRepository.findAll();
    }

    @Override
    public boolean closeOrderBook(long bookId) {
        Optional<OrderBook> bookOptional = orderBookRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            OrderBook orderBook = bookOptional.get();
            orderBook.setBookStatus(OrderBookStatus.CLOSE);
            return true;
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
