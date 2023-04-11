package com.order.chenna.service.impl;

import com.order.chenna.exception.OrderNotExists;
import com.order.chenna.exception.OrderProcessed;
import com.order.chenna.model.Order;
import com.order.chenna.model.OrderBook;
import com.order.chenna.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    public static final String COMPLETED = "COMPLETED";

    public static final String PENDING = "PENDING";
    public static final String ORDER_ID_NOT_EXISTS = "Order id not exists";
    public static final String BUY = "buy";
    private OrderBook orderBook = new OrderBook(new HashMap<>(), new HashMap<>());

    private Map<String, Order> idMap = new ConcurrentHashMap<>();

    /**
     * This Method is to create the order and add it to the order book
     *
     * @param order input order
     * @return Created Order object
     */
    @Override
    public Order createOrder(Order order) {

        // create an id and update the status of the order as pending
        order.setId(UUID.randomUUID().toString());
        order.setStatus(PENDING);

        // insert in the order in the id map for fetching
        idMap.put(order.getId(), order);

        List<Order> orders;

        // check if the order is of side buy
        if (order.getSide().equalsIgnoreCase(BUY)) {

            // fetch the buy order list from the order book by the order price
            orders = orderBook.getBuyList().get(order.getPrice());

            // if the input order is first order for the price then create a new arraylist and add the order to it
            if (isEmpty(orders)) {
                List<Order> newOrder = new ArrayList<>();
                newOrder.add(order);
                orderBook.getBuyList().put(order.getPrice(), newOrder);
            } else {
                // if input order is not the first order add the order to the order list
                orders.add(order);
            }

            // check if the order is of side sell
        } else {
            // fetch the sell order list from the order book by the order price
            orders = orderBook.getSellList().get(order.getPrice());

            // if the input order is first order for the price then create a new arraylist and add the order to it
            if (isEmpty(orders)) {
                List<Order> newOrder = new ArrayList<>();
                newOrder.add(order);
                orderBook.getSellList().put(order.getPrice(), newOrder);
            } else {
                // if input order is not the first order add the order to the order list
                orders.add(order);
            }
        }

        // return the created order
        return order;
    }


    /**
     * This method is to update the order if it is not completed.
     * It throws {@link OrderProcessed } if order is completed and {@link OrderNotExists }
     * if no order exists for the input id.
     *
     * @param id          the id of the order.
     * @param newQuantity the updated quantity.
     * @return updated Order object
     */
    @Override
    public Order modifyOrder(String id, int newQuantity) {

        // fetch the order by order id
        Order oldOrder = idMap.get(id);

        // Check weather order is in pending
        if (oldOrder != null && oldOrder.getStatus().equalsIgnoreCase(PENDING)) {
            List<Order> orders;

            // Update the order quantity for buy side
            if (oldOrder.getSide().equalsIgnoreCase(BUY)) {
                orders = orderBook.getBuyList().get(oldOrder.getPrice());
                orderBook.getBuyList().put(oldOrder.getPrice(), orders.stream().filter(order -> !oldOrder.getId().equalsIgnoreCase(order.getId())).collect(Collectors.toList()));
                oldOrder.setQuantity(newQuantity);
                orderBook.getBuyList().get(oldOrder.getPrice()).add(oldOrder);

                // Update the order quantity for sell side
            } else {
                orders = orderBook.getSellList().get(oldOrder.getPrice());
                orderBook.getSellList().put(oldOrder.getPrice(), orders.stream().filter(order -> !oldOrder.getId().equalsIgnoreCase(order.getId())).collect(Collectors.toList()));
                oldOrder.setQuantity(newQuantity);
                orderBook.getSellList().get(oldOrder.getPrice()).add(oldOrder);
            }
            return oldOrder;

            // If order is completed update cannot be applied
        } else if (oldOrder != null && !oldOrder.getStatus().equalsIgnoreCase(PENDING)) {
            throw new OrderProcessed("Completed Order Cannot be updated!!!");
        } else {
            throw new OrderNotExists(ORDER_ID_NOT_EXISTS);
        }
    }


    /**
     * This method is to delete the order if it is not completed.
     * It throws {@link OrderProcessed } if order is completed and {@link OrderNotExists }
     * if no order exists for the input id.
     * @param id the input id
     * @return true if order is deleted successfully from the orderbook
     */
    @Override
    public boolean deleteOrder(String id) {

        // fetch the order by order id
        Order oldOrder = idMap.get(id);

        // Check weather order is in pending
        if (oldOrder != null && oldOrder.getStatus().equalsIgnoreCase(PENDING)) {
            List<Order> orders;

            // delete the order for buy side in order book
            if (oldOrder.getSide().equalsIgnoreCase(BUY)) {
                orders = orderBook.getBuyList().get(oldOrder.getPrice());
                orderBook.getBuyList().put(oldOrder.getPrice(), orders.stream().filter(order -> !oldOrder.getId().equalsIgnoreCase(order.getId())).collect(Collectors.toList()));


                // delete the order for sell side in order book
            } else {
                orders = orderBook.getSellList().get(oldOrder.getPrice());
                orderBook.getSellList().put(oldOrder.getPrice(), orders.stream().filter(order -> !oldOrder.getId().equalsIgnoreCase(order.getId())).collect(Collectors.toList()));
            }

            // delete the order from the id map
            idMap.remove(id);

            // If order is completed delete cannot be applied
        } else if (oldOrder != null && oldOrder.getStatus().equalsIgnoreCase(COMPLETED)) {
            throw new OrderProcessed("Completed Order Cannot be deleted!!!");
        } else {
            throw new OrderNotExists(ORDER_ID_NOT_EXISTS);
        }
        return true;
    }

    private static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    @Override
    public OrderBook getOrderBook() {
        return orderBook;
    }


    /**
     * This method is to get the order by order id.
     * It throws {@link OrderNotExists} if no order exists for the input id.
     * @param id the input id
     * @return true if order is deleted successfully from the orderbook
     */
    @Override
    public Order getOrder(String id) {

        Order order = idMap.get(id);
        if (order != null) {
            return order;
        } else {
            throw new OrderNotExists(ORDER_ID_NOT_EXISTS);
        }
    }

}
