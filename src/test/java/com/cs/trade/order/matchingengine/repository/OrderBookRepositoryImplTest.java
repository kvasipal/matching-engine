package com.cs.trade.order.matchingengine.repository;

import com.cs.trade.order.matchingengine.model.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OrderBookRepositoryImplTest {

    OrderBookRepositoryImpl orderBookRepository;
    OrderRepositoryImpl orderRepository;

    @Before
    public void setup() {
        orderRepository = new OrderRepositoryImpl();
        orderBookRepository = new OrderBookRepositoryImpl();
        OfferOrder offerOrder1 = OfferOrder.builder()
                .orderPrice(new BigDecimal(10.55))
                .instumentId(10000l)
                .quantity(100)
                .orderType(OrderType.LIMIT)
                .build();

        Order order1 = Order.builder()
                .orderId(1)
                .offerOrder(offerOrder1)
                .executedQty(0)
                .remainingQty(offerOrder1.getQuantity())
                .orderStatus(OrderStatus.VALID)
                .executedPrice(new BigDecimal(0.0))
                .build();
        orderRepository.save(order1);
        OfferOrder offerOrder2 = OfferOrder.builder()
                .orderPrice(new BigDecimal(10.55))
                .instumentId(10000l)
                .quantity(200)
                .orderType(OrderType.LIMIT)
                .build();

        Order order2 = Order.builder()
                .orderId(2)
                .offerOrder(offerOrder2)
                .executedQty(0)
                .remainingQty(offerOrder2.getQuantity())
                .orderStatus(OrderStatus.VALID)
                .executedPrice(new BigDecimal(0.0))
                .build();

        orderRepository.save(order2);
        OrderExecution execution = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(150).build();

        OrderBook orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .orderList(Optional.of(Arrays.asList(order1, order2)))
                .executionList(Optional.of(Arrays.asList(execution)))
                .build();
        orderBookRepository.save(orderBook);

    }
    @Test
    public void shouldgetRecordsFromOrderBookForGivenBook() {
        long bookid = 123l;
        Optional<OrderBook> bookOptional = orderBookRepository.findById(bookid);
        assertThat(bookOptional.isPresent(), equalTo(true));
        assertThat(bookOptional.get().getBookStatus(), equalTo(OrderBookStatus.OPEN));
        assertThat(bookOptional.get().getOrderList().get().size(), equalTo(2));
        assertThat(bookOptional.get().getExecutionList().get().size(), equalTo(1));
    }

    @Test
    public void shouldReturnCorrectOrderFromOrderForGivenOrderId() {

        Optional<Order> ordersOptional = orderRepository.findById(1l);
        assertThat(ordersOptional.isPresent(), equalTo(true));
        assertThat(ordersOptional.get().getOfferOrder().getQuantity(), equalTo(100.0));
    }
}
