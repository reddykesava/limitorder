package com.order.chenna;

import com.order.chenna.exception.OrderNotExists;
import com.order.chenna.model.Order;
import com.order.chenna.service.OrderService;
import com.order.chenna.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderServiceImpl();
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        order.setPrice(10);
        order.setQuantity(20);
        order.setSide("buy");
        Order createdOrder = orderService.createOrder(order);
        assertNotNull(createdOrder.getId());
        assertEquals(order.getPrice(), createdOrder.getPrice());
        assertEquals(order.getQuantity(), createdOrder.getQuantity());
        assertEquals(order.getSide(), createdOrder.getSide());
    }

    @Test
    public void testModifyOrder() {
        Order order = new Order();
        order.setPrice(10);
        order.setQuantity(20);
        order.setSide("buy");
        Order createdOrder = orderService.createOrder(order);
        Order modifiedOrder = orderService.modifyOrder(createdOrder.getId(), 30);
        assertEquals(createdOrder.getId(), modifiedOrder.getId());
        assertEquals(30, modifiedOrder.getQuantity().intValue());
    }

    @Test
    public void testModifyOrderMultipleOrder() {
        Order order = new Order();
        order.setPrice(10);
        order.setQuantity(20);
        order.setSide("buy");
        Order order2 = new Order();
        order2.setPrice(10);
        order2.setQuantity(40);
        order2.setSide("buy");
        Order order3 = new Order();
        order3.setPrice(10);
        order3.setQuantity(80);
        order3.setSide("buy");
        Order createdOrder = orderService.createOrder(order);
        Order createdOrder2 = orderService.createOrder(order2);
        Order createdOrder3 = orderService.createOrder(order3);
        Order modifiedOrder = orderService.modifyOrder(createdOrder2.getId(), 30);
        List<Order> modifiedOrderList= orderService.getOrderBook().getBuyList().get(10);
        assertEquals(createdOrder2.getId() ,modifiedOrderList.get(modifiedOrderList.size()-1).getId());
        assertEquals(30, modifiedOrder.getQuantity().intValue());
    }

    @Test()
    public void testModifyOrderWithNonExistentOrderId() {
        assertThrows(OrderNotExists.class,()->orderService.modifyOrder("non-existent-order-id", 30));
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order();
        order.setPrice(10);
        order.setQuantity(20);
        order.setSide("buy");
        Order createdOrder = orderService.createOrder(order);
        boolean deleted = orderService.deleteOrder(createdOrder.getId());
        assertTrue(deleted);
    }


    @Test
    public void testDeleteOrderMultipleOrder() {
        Order order = new Order();
        order.setPrice(10);
        order.setQuantity(20);
        order.setSide("buy");
        Order order2 = new Order();
        order2.setPrice(10);
        order2.setQuantity(40);
        order2.setSide("buy");
        Order order3 = new Order();
        order3.setPrice(10);
        order3.setQuantity(80);
        order3.setSide("buy");
        Order createdOrder = orderService.createOrder(order);
        Order createdOrder2 = orderService.createOrder(order2);
        Order createdOrder3 = orderService.createOrder(order3);
        orderService.deleteOrder(createdOrder2.getId());
        List<Order> modifiedOrderList= orderService.getOrderBook().getBuyList().get(10);
        assertEquals(2, modifiedOrderList.size());
        assertFalse(modifiedOrderList.parallelStream().anyMatch(order1 -> order1.getId().equalsIgnoreCase(order2.getId())));
    }
    @Test()
    public void testDeleteOrderWithNonExistentOrderId() {
        assertThrows(OrderNotExists.class,()->orderService.deleteOrder("non-existent-order-id"));
    }
}
