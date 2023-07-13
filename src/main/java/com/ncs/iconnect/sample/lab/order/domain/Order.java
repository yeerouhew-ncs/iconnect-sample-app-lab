package com.ncs.iconnect.sample.lab.order.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Order.
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String product;

    private Integer quantity;

    private Double price;

    private String payment;

    private LocalDate orderDate;

    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public Order product(String product) {
        this.product = product;
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Order quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Order price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPayment() {
        return payment;
    }

    public Order payment(String payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Order orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public Order remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        if (order.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + getId() + ", product='" + getProduct() + "'" + ", quantity=" + getQuantity()
            + ", price=" + getPrice() + ", payment='" + getPayment() + "'" + ", orderDate='" + getOrderDate() + "'"
            + ", remarks='" + getRemarks() + "'" + "}";
    }
}

