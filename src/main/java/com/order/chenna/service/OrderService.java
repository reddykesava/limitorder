package com.order.chenna.service;

import com.order.chenna.model.Order;
import com.order.chenna.model.OrderBook;

public interface OrderService {
    Order createOrder(Order order);

    Order modifyOrder(String id, int newQuantity);

    boolean deleteOrder(String id);

    OrderBook getOrderBook();

    Order getOrder(String id);
}
