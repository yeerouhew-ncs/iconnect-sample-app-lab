package com.ncs.iconnect.sample.lab.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ncs.iconnect.sample.lab.order.domain.Order;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Override
    public Page<Order> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order find(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order add(Order entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order update(Order entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public Page<Order> findOrders(String orderRemarks, Pageable page) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Order> search(String query, Pageable page) {
        // TODO Auto-generated method stub
        return null;
    }


}

