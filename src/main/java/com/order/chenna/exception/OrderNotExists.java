package com.order.chenna.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderNotExists extends RuntimeException{

    public OrderNotExists(String msg)
    {
        super(msg);
    }
}
