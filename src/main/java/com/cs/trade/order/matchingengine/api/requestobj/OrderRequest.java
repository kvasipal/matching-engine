package com.cs.trade.order.matchingengine.api.requestobj;

import com.cs.trade.order.matchingengine.model.OrderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {
    private double quantity;
    private long instumentId;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date entryDate;
    private BigDecimal orderPrice;
    private String orderType;
}
