package com.order.chenna.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderProcessed extends RuntimeException{

    public OrderProcessed(String msg)
    {
        super(msg);
    }
}
