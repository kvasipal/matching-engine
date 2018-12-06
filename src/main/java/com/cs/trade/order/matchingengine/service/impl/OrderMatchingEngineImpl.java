package com.cs.trade.order.matchingengine.service.impl;

import com.cs.trade.order.matchingengine.model.*;
import com.cs.trade.order.matchingengine.repository.OrderBookRepository;
import com.cs.trade.order.matchingengine.repository.OrderBookRepositoryImpl;
import com.cs.trade.order.matchingengine.service.OrderMatchingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
@Component
public class OrderMatchingEngineImpl implements OrderMatchingEngine {

    @Autowired
    private OrderBookRepositoryImpl orderBookRepository;

    @Override
    public Optional<OrderBook> match(long bookId) {
        Predicate<Order> cr1 = o1 -> o1.getOfferOrder().getOrderType().equals(OrderType.LIMIT)
                && o1.getOrderStatus().equals(OrderStatus.VALID) ;

        Optional<OrderBook> bookOptional =orderBookRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            OrderBook orderBook = bookOptional.get();
            if(!isBookExecuted(orderBook)) {
                BigDecimal executionPrice = orderBook.getExecutionList().get().get(0).getPrice();
                double aggregatedExecutionQty = orderBook.getExecutionList().get().stream()
                        .mapToDouble(OrderExecution::getQuantity)
                        .sum();
                orderBook.getOrderList().get()
                        .stream()
                        .filter(cr1)
                        .filter(o -> o.getOfferOrder().getOrderPrice().compareTo(executionPrice) < 0)
                        .forEach(o3 -> {
                            o3.setOrderStatus(OrderStatus.INVALID);
                            o3.setExecutedQty(0);
                            o3.setRemainingQty(0);
                        });

                Predicate<Order> cr2 = o2 ->
                        (o2.getOrderStatus().equals(OrderStatus.VALID)
                            && (o2.getOfferOrder().getOrderType().equals(OrderType.MARKET)
                                || (o2.getOfferOrder().getOrderType().equals(OrderType.LIMIT)
                                    && o2.getOfferOrder().getOrderPrice().compareTo(executionPrice) == 0)));
                double orderAggQty = orderBook.getOrderList().get()
                        .stream()
                        .filter(cr2)
                        .mapToDouble(Order::getRemainingQty)
                        .sum();


                orderBook.getOrderList().get()
                        .stream()
                        .filter(cr2)
                        .forEach(o -> {
                            if(aggregatedExecutionQty >= orderAggQty){
                                o.setExecutedQty(o.getRemainingQty());
                                o.setRemainingQty(0); //aggregatedExecutionQty - orderAggQty// set book as exexuted
                            } else {
                                double qty = o.getRemainingQty();
                                double remaining = (qty - (qty/orderAggQty)* aggregatedExecutionQty);
                                o.setExecutedQty(qty-remaining);
                                o.setRemainingQty(remaining);
                            }
                            o.setExecutedPrice(executionPrice);
                            }
                        );
                orderBook.getExecutionList().get()
                        .stream()
                        .forEach(execution -> execution.setQuantity(0));
                if (orderBook.getOrderList().get()
                        .stream()
                        .mapToDouble(Order::getRemainingQty)
                        .sum() == 0) {
                    orderBook.setBookStatus(OrderBookStatus.EXECUTED);
                }
                return Optional.of(orderBook);
            }
        }
        return Optional.empty();
    }
        private boolean isBookExecuted(OrderBook orderBook) {
            return orderBook.getBookStatus().equals(OrderBookStatus.EXECUTED);
        }

}
