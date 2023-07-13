package com.ncs.iconnect.sample.lab.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ncs.iconnect.sample.lab.order.domain.Order;

public interface OrderService {
    public Page<Order> findAll(Pageable pageable);
    public Order find(Long id);
    public Order add(Order entity);
    public Order update(Order entity);
    public void delete(Long id);
    public Page<Order> findOrders(String orderRemarks, Pageable page);
    public Page<Order> search(String query, Pageable page);

}
