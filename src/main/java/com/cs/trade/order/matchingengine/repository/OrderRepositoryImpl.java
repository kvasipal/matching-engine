package com.cs.trade.order.matchingengine.repository;

import com.cs.trade.order.matchingengine.model.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Component
public class OrderRepositoryImpl {
//
//    private static final Map<Long,OfferOrder> orders =  new HashMap<Long, OfferOrder>();
//
//    public Flux<OfferOrder> findAll() {
//        return Flux.fromIterable(orders.values());
//    }
//
//    public Optional<OfferOrder> findById(long bookId) {
//        return Optional.of(orders. get(bookId));
//    }
//    public void save(OfferOrder offerOrder1) {
//        orders.put(1l, offerOrder1);
//        System.out.println(offerOrder1);
//
//    }

    private static final Map<Long, Order> orders = new HashMap<>();

    public Flux<Order> findAll() {
        return Flux.fromIterable(orders.values());
    }

    public Optional<Order> findById(long bookId) {
        return Optional.of(orders. get(bookId));
    }
    public void save(Order order) {
        orders.put(order.getOrderId(), order);
        System.out.println(orders);

    }
}
