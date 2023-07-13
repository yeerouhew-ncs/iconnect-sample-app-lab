package com.ncs.iconnect.sample.lab.order.service;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.ncs.iconnect.sample.lab.order.domain.Order;
import com.ncs.iconnect.sample.lab.order.repository.OrderRepository;

public class OrderServiceTest {
    OrderService orderService;
    OrderRepository orderRepository = mock(OrderRepository.class);

    @BeforeEach
    public void init() {
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    public void testAddOrderExpectSuccess() {
        Order order = prepareOrder();
        when(orderRepository.save(order)).thenReturn(order);
        Order savedOrder = orderService.add(order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testFindOrdersExpectNonEmptyResult() {
        Pageable page =PageRequest.of(0, 10);
        String orderRemarks = "Deliver before 5pm";
        when(orderRepository.findByOrderNoLike(orderRemarks, page)).thenReturn(prepareSearchResults(page));
        Page<Order> searchResults = orderService.findOrders(orderRemarks, page);
        verify(orderRepository, times(1)).findByOrderNoLike(orderRemarks, page);
    }

    private Order prepareOrder() {
        Order order = new Order();
        order.setProduct("Chicken Satay");
        order.setQuantity(50);
        order.setPrice(1.5);
        order.setPayment("Cash");
        order.setOrderDate(LocalDate.now());
        order.setRemarks("Deliver before 5pm");
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private Page<Order> prepareSearchResults(Pageable page) {
        return new PageImpl<Order>(Arrays.asList(prepareOrder()), page, 1);
    }

}
