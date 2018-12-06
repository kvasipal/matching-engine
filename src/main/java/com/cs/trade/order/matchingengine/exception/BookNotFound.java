package com.cs.trade.order.matchingengine.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BookNotFound extends Exception {
    private String message = "Order book not found";
}
