package com.cs.trade.order.matchingengine.api;

import com.cs.trade.order.matchingengine.api.requestobj.ExecutionRequest;
import com.cs.trade.order.matchingengine.api.requestobj.OrderRequest;
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
    @ApiOperation(value = "Create a orderBook resource.", notes = "Returns the URL of the new resource in the Location header.")
    public ResponseEntity createOrderBook(@PathVariable("instrumentId") long instrumentId) {
        return new ResponseEntity(bookManagerService.createOrderBook(instrumentId),  HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{bookId}/order", method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    public ResponseEntity addOrderToBook(@PathVariable("bookId") long bookId, @Valid @RequestBody OrderRequest order) {
        return new ResponseEntity(bookManagerService.addOrderToBook(bookId, order),  HttpStatus.OK);
    }

    @RequestMapping(value = "/{bookId}/execution", method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    public ResponseEntity addExecutionToBook(@PathVariable("bookId") long bookId, @Valid @RequestBody ExecutionRequest execution) {
        return new ResponseEntity(bookManagerService.addExecutionToBook(bookId, execution),  HttpStatus.OK);
    }

    @RequestMapping(value = "/{bookId}/{orderId}/status", method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    public ResponseEntity getOrderStatus(long orderId) {
        return new ResponseEntity(bookManagerService.getOrderStatus(orderId),  HttpStatus.OK);
    }

    @RequestMapping(value = "/{bookId}/status", method = RequestMethod.GET,
    produces = {"application/json", "application/xml"})
    public ResponseEntity getOrderBookStatus(@PathVariable Long bookId) {
        return new ResponseEntity(bookManagerService.getOrderBookStatus(bookId),  HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity getOrderStatus() {
        return new ResponseEntity(bookManagerService.getAllOrderBooks(),  HttpStatus.OK);
    }

    @RequestMapping(value = "/{bookId}/status", method = RequestMethod.PUT,
            produces = {"application/json", "application/xml"})
    public ResponseEntity addExecutionToBook(@PathVariable("bookId") long bookId) {
        return new ResponseEntity(bookManagerService.closeOrderBook(bookId),  HttpStatus.OK);
    }


}
