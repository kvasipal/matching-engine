package com.cs.trade.order.matchingengine.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BookHasNoOrdersToExecute extends Exception {
    private String message = "No Orders are added to Book, Nothing to execute";
}
