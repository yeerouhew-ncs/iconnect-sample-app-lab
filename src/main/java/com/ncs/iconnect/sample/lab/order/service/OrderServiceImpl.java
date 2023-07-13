package com.ncs.iconnect.sample.lab.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ncs.iconnect.sample.lab.order.domain.Order;
import com.ncs.iconnect.sample.lab.order.repository.OrderRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Order find(Long id) {
        return orderRepository.getOne(id);
    }

    @Override
    public Order add(Order entity) {
        return orderRepository.save(entity);
    }

    @Override
    public Order update(Order entity) {
        return orderRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.getOne(id);
        if (order != null) {
            orderRepository.delete(order);
        }
    }

    @Override
    public Page<Order> findOrders(String orderRemarks, Pageable page) {
        return orderRepository.findByOrderNoLike(orderRemarks, page);
    }

    @Override
    public Page<Order> search(String query, Pageable page) {
        return orderRepository.search(query, page);
    }


}

