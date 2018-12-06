package com.cs.trade.order.matchingengine.validator;

import com.cs.trade.order.matchingengine.model.OfferOrder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OrderValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return OfferOrder.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        OfferOrder offerOrder = (OfferOrder) o;
    }
}
