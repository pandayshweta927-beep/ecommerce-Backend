package com.shweta.ecommerce.dto;

import java.time.LocalDateTime;

import com.shweta.ecommerce.entity.OrderStatus;

public class OrderResponseDTO {

    private Long id;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(
            Long id,
            Double totalAmount,
            OrderStatus status,
            LocalDateTime orderDate) {

        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}