package com.cs.trade.order.matchingengine.service;

import com.cs.trade.order.matchingengine.model.*;
import com.cs.trade.order.matchingengine.repository.OrderBookRepositoryImpl;
import com.cs.trade.order.matchingengine.repository.OrderRepositoryImpl;
import com.cs.trade.order.matchingengine.service.impl.OrderBookStatusChanger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrderBookStatusChangerTest.class)
public class OrderBookStatusChangerTest {

    OrderBookRepositoryImpl orderBookRepository;

    OrderRepositoryImpl orderRepository;
    OrderBookStatusChanger orderBookStatusChanger;

    @Before
    public void setup() {
        orderBookRepository = new OrderBookRepositoryImpl();
        orderRepository = new OrderRepositoryImpl();
        orderBookStatusChanger = new OrderBookStatusChanger();
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
        orderRepository.save(order1);
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
    public void shouldReturnBookStatusOpenWhenBookID() {

        long bookId = 123;
        OrderBook orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .build();
        //when(orderBookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(orderBook));
        assertThat(orderBookStatusChanger.getBookStatus(bookId).get(), equalTo(OrderBookStatus.OPEN));
    }

    @Test
    public void shouldReturnBookStatusOpenWhenBookIDNotInDB() {
        assertThat(orderBookStatusChanger.getBookStatus(1l).isPresent(), equalTo(false));
    }

    //@Test
    public void shouldChargeStatusToExecutedWhenAccumulatedOrderQtyEqualToAccumulatedExecutedQty() {
        long bookId = 123;

        OfferOrder offerOrder = OfferOrder.builder()
                .orderPrice(new BigDecimal(10.55))
                .instumentId(10000l)
                .quantity(10000)
                .build();

        Order order = Order.builder()
                .orderId(1)
                .offerOrder(offerOrder)
                .executedQty(0)
                .remainingQty(offerOrder.getQuantity())
                .orderStatus(OrderStatus.VALID)
                .build();

        OrderExecution execution = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(0).build();

        OrderBook orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .orderList(Optional.of(Arrays.asList(order)))
                .executionList(Optional.of(Arrays.asList(execution)))
                .build();
        when(orderBookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(orderBook));
        assertThat(orderBookStatusChanger.changeOrderBookState(bookId).get(), equalTo(OrderBookStatus.EXECUTED.toString()));
    }


//addOrder
// checkStatus
// addExecution

    /*openOrderBook();
    closeorderBook();
    numberOfOrderInBook();
    biggestOrderInBook();
    smallestOrderInBook();
    earliestOrderInBook();
    lastOrderInBook();
    limitBreakdown(); //a table with limit prices and demand per limit price
    demandOfBook();//accumulated order quantity
    numberOfValidOrders();
    numberOfInValidOrders();
    demandOfValidOrdersOfBook();//accumulated valid order quantity
    demandOfInValidOrdersOfBook();//accumulated valid order quantity
    accumulatedExecutionQuantity();
    accumulatedExecutionPrice();
    getOrderStatus(order_id);//validity status, execution quantity and order’s price, and execution price.*/


}
