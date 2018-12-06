package com.cs.trade.order.matchingengine.service.impl;

import com.cs.trade.order.matchingengine.model.*;
import com.cs.trade.order.matchingengine.repository.OrderBookRepository;
import com.cs.trade.order.matchingengine.repository.OrderBookRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
@ComponentScan( value = "com.cs.trade.order.matchingengine")
@SpringBootTest(classes = {OrderMatchingEngineImplTest.class})
public class OrderMatchingEngineImplTest {

    //@Mock
    @Autowired
     OrderBookRepositoryImpl mockOrderBookRepository;
    //@InjectMocks
    @Autowired
     OrderMatchingEngineImpl matchingEngine;

    OrderBook orderBook;

    @Before
    public void setUp() {
       // mockOrderBookRepository = new OrderBookRepositoryImpl();
       // matchingEngine = new OrderMatchingEngineImpl();
        MockitoAnnotations.initMocks(this);
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

        OrderExecution execution = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(150).build();

         orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .orderList(Optional.of(Arrays.asList(order1, order2)))
                .executionList(Optional.of(Arrays.asList(execution)))
                .build();



    }

    @Test
    public void shouldCal_exeNrem_WhenMatch2Orders100_200And_1Execution150WithSamePrice() {
        long bookId = 123;
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

        OrderExecution execution = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(150).build();

        orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .orderList(Optional.of(Arrays.asList(order1, order2)))
                .executionList(Optional.of(Arrays.asList(execution)))
                .build();
        mockOrderBookRepository.save(orderBook);
        //when(mockOrderBookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(orderBook));
        Optional<OrderBook> actual = matchingEngine.match(bookId);
        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get().getBookStatus(), equalTo(OrderBookStatus.OPEN));
        assertThat(actual.get().getOrderList().get().get(0).getRemainingQty(), equalTo(50.0d));
        assertThat(actual.get().getOrderList().get().get(1).getRemainingQty(), equalTo(100.0d));
    }

    @Test
    public void shouldCal_exeFullNrem0_WhenMatch2Orders100_200And_1Execution300WithSamePriceWithLimitOrder() {
        long bookId = 123;
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

        OrderExecution execution = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(300).build();

        orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .orderList(Optional.of(Arrays.asList(order1, order2)))
                .executionList(Optional.of(Arrays.asList(execution)))
                .build();
        mockOrderBookRepository.save(orderBook);
       //when(mockOrderBookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(orderBook));
        Optional<OrderBook> actual = matchingEngine.match(bookId);
        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get().getBookStatus(), equalTo(OrderBookStatus.EXECUTED));
        assertThat(actual.get().getOrderList().get().get(0).getRemainingQty(), equalTo(0.0d));
        assertThat(actual.get().getOrderList().get().get(1).getRemainingQty(), equalTo(0.0d));
    }

    @Test
    public void shouldCal_exeFullNrem0_WhenMatch2Orders100_200And_1Execution300WithSamePriceWith_1LimitOrder_1MarketOrder() {
        long bookId = 123;
        OfferOrder offerOrder1 = OfferOrder.builder()
                .instumentId(10000l)
                .quantity(100)
                .orderType(OrderType.MARKET)
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

        OrderExecution execution = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(300).build();

        orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .orderList(Optional.of(Arrays.asList(order1, order2)))
                .executionList(Optional.of(Arrays.asList(execution)))
                .build();
        mockOrderBookRepository.save(orderBook);
        //when(mockOrderBookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(orderBook));
        Optional<OrderBook> actual = matchingEngine.match(bookId);
        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get().getBookStatus(), equalTo(OrderBookStatus.EXECUTED));
        assertThat(actual.get().getOrderList().get().get(0).getRemainingQty(), equalTo(0.0d));
        assertThat(actual.get().getOrderList().get().get(1).getRemainingQty(), equalTo(0.0d));
    }

    @Test
    public void shouldCal_exeFullNrem0_WhenMatch2Orders100_200And_2Execution300_100WithSamePriceWith_1LimitOrder_1MarketOrder() {
        long bookId = 123;
        OfferOrder offerOrder1 = OfferOrder.builder()
                .instumentId(10000l)
                .quantity(100)
                .orderType(OrderType.MARKET)
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

        OrderExecution execution1 = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(300).build();

        OrderExecution execution2 = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(100).build();

        orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .orderList(Optional.of(Arrays.asList(order1, order2)))
                .executionList(Optional.of(Arrays.asList(execution1, execution2)))
                .build();
        mockOrderBookRepository.save(orderBook);
        //when(mockOrderBookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(orderBook));
        Optional<OrderBook> actual = matchingEngine.match(bookId);
        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get().getBookStatus(), equalTo(OrderBookStatus.EXECUTED));
        assertThat(actual.get().getOrderList().get().get(0).getRemainingQty(), equalTo(0.0d));
        assertThat(actual.get().getOrderList().get().get(1).getRemainingQty(), equalTo(0.0d));
        assertThat(actual.get().getExecutionList().get().get(0).getQuantity(), equalTo(0.0d));
        assertThat(actual.get().getExecutionList().get().get(1).getQuantity(), equalTo(0.0d));
    }

    @Test
    public void shouldCal_exeNrem_WhenMatch2Orders100_200And_2Execution100And50WithSamePrice() {
        long bookId = 123;
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

        OrderExecution execution1 = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(50).build();
        OrderExecution execution2 = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(100).build();

        orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .orderList(Optional.of(Arrays.asList(order1, order2)))
                .executionList(Optional.of(Arrays.asList(execution1,execution2)))
                .build();
        mockOrderBookRepository.save(orderBook);
//        when(mockOrderBookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(orderBook));
        Optional<OrderBook> actual = matchingEngine.match(bookId);
        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get().getBookStatus(), equalTo(OrderBookStatus.OPEN));
        assertThat(actual.get().getOrderList().get().get(0).getRemainingQty(), equalTo(50.0d));
        assertThat(actual.get().getOrderList().get().get(1).getRemainingQty(), equalTo(100.0d));
        assertThat(actual.get().getExecutionList().get().get(0).getQuantity(), equalTo(0.0d));
        assertThat(actual.get().getExecutionList().get().get(1).getQuantity(), equalTo(0.0d));
    }
    //@Test
    public void should_WhenNoOrderAnd_1Execution300() {
        long bookId = 123;

        OrderExecution execution = OrderExecution.builder()
                .price(new BigDecimal(10.55))
                .quantity(300).build();

        orderBook = OrderBook.builder()
                .orderBookId(123l)
                .bookStatus(OrderBookStatus.OPEN)
                .instumentId(10000l)
                .executionList(Optional.of(Arrays.asList(execution)))
                .build();

        when(mockOrderBookRepository.findById(bookId)).thenReturn(java.util.Optional.ofNullable(orderBook));
        Optional<OrderBook> actual = matchingEngine.match(bookId);
        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get().getBookStatus(), equalTo(OrderBookStatus.EXECUTED));
    }
}