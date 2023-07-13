package com.ncs.iconnect.sample.lab.order.tests.integration;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.order.domain.Order;
import com.ncs.iconnect.sample.lab.order.repository.OrderRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IconnectSampleAppLabApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class OrderRepositoryITCase {


    @Autowired
    OrderRepository orderRepository;

    @Test
    public void testCRUD() {
        // test create
        Order order = prepareOrder();
        order = this.orderRepository.save(order);
        Assertions.assertNotNull(order);
        // test update
        order.setQuantity(20);

        Order updatedOrder = orderRepository.getOne(order.getId());
        Assertions.assertNotNull(updatedOrder.getId());
        Assertions.assertNotNull(updatedOrder);
        Assertions.assertTrue(updatedOrder.getQuantity().intValue() == 20);
        // test delete
        Long id = order.getId();
        orderRepository.deleteById(order.getId());
        Optional<Order> result = orderRepository.findById(id);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void testFindByOrderNo() {
        Pageable page_temp = PageRequest.of(0, 100);
        Page<Order> orders2Clear = this.orderRepository.findByOrderNoLike("Junit Test", page_temp);
        orderRepository.deleteAll(orders2Clear.getContent());
        Order order = prepareOrder();
        order = this.orderRepository.save(order);
        Pageable page = PageRequest.of(0, 10);
        Page<Order> orders = this.orderRepository.findByOrderNoLike("Junit Test", page);
        Assertions.assertTrue(orders.getContent().size() == 1);

        orders = this.orderRepository.findByOrderNoLike("Junit", page);
        Assertions.assertTrue(orders.getContent().size() == 1);

        orders = this.orderRepository.findByOrderNoLike("abc", page);
        Assertions.assertTrue(orders.getContent().size() == 0);
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
