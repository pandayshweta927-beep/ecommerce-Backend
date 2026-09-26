package com.shweta.ecommerce.dto;

import java.time.LocalDateTime;

import com.shweta.ecommerce.entity.PaymentStatus;

public class PaymentResponseDTO {

    private Long id;
    private Long orderId;
    private Double amount;
    private PaymentStatus status;
    private String paymentMethod;
    private LocalDateTime paymentDate;

    public PaymentResponseDTO() {
    }

    public PaymentResponseDTO(
            Long id,
            Long orderId,
            Double amount,
            PaymentStatus status,
            String paymentMethod,
            LocalDateTime paymentDate) {

        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}