package com.ncs.iconnect.sample.lab.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ncs.iconnect.sample.lab.order.domain.Order;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order t  WHERE " + "LOWER(t.remarks) LIKE LOWER(CONCAT('%',:remarks, '%'))")
    public Page<Order> findByOrderNoLike(@Param("remarks") String remarks, Pageable page);

    @Query("FROM Order t  WHERE "
        + "t.quantity = CONCAT('',:query) OR "
        + "t.orderDate LIKE CONCAT('%',:query, '%') OR "
        + "t.payment = CONCAT('',:query) OR "
        + "LOWER(t.product) LIKE LOWER(CONCAT('%',:query, '%')) OR "
        + "LOWER(t.remarks) LIKE LOWER(CONCAT('%',:query, '%'))")
    public Page<Order> search(@Param("query") String query, Pageable page);
}
