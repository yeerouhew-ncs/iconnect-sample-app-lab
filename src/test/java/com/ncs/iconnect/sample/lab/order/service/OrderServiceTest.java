package com.ncs.iconnect.sample.lab.order.service;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ncs.iconnect.sample.lab.order.domain.Order;
public class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    public void init() {
        orderService = new OrderServiceImpl();
    }

    @Test
    public void testAddOrderExpectSuccess(){
        Order order = prepareOrder();
        Order savedOrder= orderService.add(order);
        Assertions.assertNotNull(savedOrder.getId());
        Assertions.assertNotNull(savedOrder.getQuantity());
    }
    @Test
    public void testFindOrdersExpectNonEmptyResult(){
        Pageable page = PageRequest.of(0, 10);
        Page<Order>searchResults = orderService.findAll(page);
        Assertions.assertTrue(searchResults.getSize()>0);
    }

    private Order prepareOrder() {
        Order order = new Order();
        order.setProduct("Chicken Satay");
        order.setQuantity(50);
        order.setPrice(1.5);
        order.setPayment("Cash");
        order.setOrderDate(LocalDate.now());
        order.setRemarks("Junit Test Order Remarks");
        order.setOrderDate(LocalDate.now());
        return order;
    }
}
