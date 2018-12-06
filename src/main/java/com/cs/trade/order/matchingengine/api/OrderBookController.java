package com.cs.trade.order.matchingengine.api;

import com.cs.trade.order.matchingengine.model.OfferOrder;
import com.cs.trade.order.matchingengine.model.OrderExecution;
import com.cs.trade.order.matchingengine.service.OrderBookManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping(value = "/orderbook")
@Api(tags = {"orderbook"})
public class OrderBookController {

    @Autowired
    private OrderBookManagerService bookManagerService;

    @RequestMapping(value = "/{instrumentId}",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    //@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a orderBook resource.", notes = "Returns the URL of the new resource in the Location header.")
    public ResponseEntity createOrderBook(@PathVariable("instrumentId") long instrumentId) {
        return new ResponseEntity(bookManagerService.createOrderBook(instrumentId),  HttpStatus.CREATED);
    }

//    @RequestMapping("{bookId}/order")
//    public ResponseEntity addOrderToBook(@PathVariable("bookId") long bookId, @Valid @RequestBody OfferOrder order) {
//        return new ResponseEntity(bookManagerService.addOrderToBook(bookId, order),  HttpStatus.OK);
//    }
//
//    @RequestMapping("{bookId}/execution")
//    public ResponseEntity addExecutionToBook(@PathVariable("bookId") long bookId, @Valid @RequestBody OrderExecution execution) {
//        return new ResponseEntity(bookManagerService.addExecutionToBook(bookId, execution),  HttpStatus.OK);
//    }

//    @RequestMapping("{bookId}/{orderId}/status")
//    public ResponseEntity getOrderStatus(long orderId) {
//
//    }
}
