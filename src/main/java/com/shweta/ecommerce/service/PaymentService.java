package com.shweta.ecommerce.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.PaymentRequestDTO;
import com.shweta.ecommerce.dto.PaymentResponseDTO;
import com.shweta.ecommerce.entity.Order;
import com.shweta.ecommerce.entity.Payment;
import com.shweta.ecommerce.entity.PaymentStatus;
import com.shweta.ecommerce.entity.User;
import com.shweta.ecommerce.repository.OrderRepository;
import com.shweta.ecommerce.repository.PaymentRepository;
import com.shweta.ecommerce.repository.UserRepository;
import com.shweta.ecommerce.entity.OrderStatus;
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // Create Payment
    public PaymentResponseDTO createPayment(
            PaymentRequestDTO request,
            String email) {

        // 1. Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Find order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException(
                        "Order not found with id: " + request.getOrderId()
                ));

        // 3. Check order ownership
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not authorized to make payment for this order"
            );
        }

        // 4. Check if payment already exists
        if (paymentRepository.findByOrder(order).isPresent()) {
            throw new RuntimeException(
                    "Payment already exists for this order"
            );
        }

        // 5. Create Payment
        Payment payment = new Payment();

        payment.setOrder(order);

        // Amount comes directly from the order
        payment.setAmount(order.getTotalAmount());

        // Local testing payment
        payment.setStatus(PaymentStatus.SUCCESS);
        order.setStatus(OrderStatus.CONFIRMED);
orderRepository.save(order);

        payment.setPaymentMethod(request.getPaymentMethod());

        payment.setPaymentDate(LocalDateTime.now());

        // 6. Save Payment
        Payment savedPayment = paymentRepository.save(payment);

        // 7. Convert Entity to Response DTO
        return new PaymentResponseDTO(
                savedPayment.getId(),
                order.getId(),
                savedPayment.getAmount(),
                savedPayment.getStatus(),
                savedPayment.getPaymentMethod(),
                savedPayment.getPaymentDate()
        );
    }

    // Get Payment by ID
    public PaymentResponseDTO getPaymentById(
            Long id,
            String email) {

        // 1. Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Find payment
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Payment not found with id: " + id
                ));

        // 3. Check order ownership
        if (!payment.getOrder().getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not authorized to view this payment"
            );
        }

        // 4. Convert Entity to Response DTO
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getPaymentDate()
        );
    }
}