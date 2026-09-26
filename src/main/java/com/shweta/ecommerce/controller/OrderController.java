package com.shweta.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.shweta.ecommerce.dto.OrderRequestDTO;
import com.shweta.ecommerce.dto.OrderResponseDTO;
import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.entity.Order;
import com.shweta.ecommerce.service.OrderService;

import jakarta.validation.Valid;
import java.util.List;
import com.shweta.ecommerce.entity.OrderStatus;
import com.shweta.ecommerce.entity.OrderItem;
import com.shweta.ecommerce.dto.OrderItemResponseDTO;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseDTO<OrderResponseDTO>> createOrder(
            @Valid @RequestBody OrderRequestDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        Order order = orderService.createOrder(request, email);

        OrderResponseDTO responseData = new OrderResponseDTO(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate()
        );

        ResponseDTO<OrderResponseDTO> response = new ResponseDTO<>(
                true,
                "Order created successfully",
                responseData
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
public ResponseEntity<ResponseDTO<List<OrderResponseDTO>>> getMyOrders(
        Authentication authentication) {

    String email = authentication.getName();

    List<Order> orders = orderService.getMyOrders(email);

    List<OrderResponseDTO> responseData = orders.stream()
            .map(order -> new OrderResponseDTO(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    order.getOrderDate()
            ))
            .toList();

    ResponseDTO<List<OrderResponseDTO>> response = new ResponseDTO<>(
            true,
            "Orders fetched successfully",
            responseData
    );

    return new ResponseEntity<>(response, HttpStatus.OK);
}
@GetMapping("/{id}")
public ResponseEntity<ResponseDTO<OrderResponseDTO>> getOrderById(
        @PathVariable Long id,
        Authentication authentication) {

    String email = authentication.getName();

    Order order = orderService.getOrderById(id, email);

    OrderResponseDTO responseData = new OrderResponseDTO(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getOrderDate()
    );

    ResponseDTO<OrderResponseDTO> response = new ResponseDTO<>(
            true,
            "Order fetched successfully",
            responseData
    );

    return new ResponseEntity<>(response, HttpStatus.OK);
}
@PutMapping("/{id}/status")
public ResponseEntity<ResponseDTO<OrderResponseDTO>> updateOrderStatus(
        @PathVariable Long id,
        @RequestParam OrderStatus status) {

    Order order = orderService.updateOrderStatus(id, status);

    OrderResponseDTO responseData = new OrderResponseDTO(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getOrderDate()
    );

    ResponseDTO<OrderResponseDTO> response = new ResponseDTO<>(
            true,
            "Order status updated successfully",
            responseData
    );

    return new ResponseEntity<>(response, HttpStatus.OK);
}
@PutMapping("/{id}/cancel")
public ResponseEntity<ResponseDTO<OrderResponseDTO>> cancelOrder(
        @PathVariable Long id,
        Authentication authentication) {

    String email = authentication.getName();

    Order order = orderService.cancelOrder(id, email);

    OrderResponseDTO responseData = new OrderResponseDTO(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getOrderDate()
    );

    ResponseDTO<OrderResponseDTO> response = new ResponseDTO<>(
            true,
            "Order cancelled successfully",
            responseData
    );

    return new ResponseEntity<>(response, HttpStatus.OK);
}
@GetMapping("/{id}/items")
public ResponseEntity<ResponseDTO<List<OrderItemResponseDTO>>> getOrderItems(
        @PathVariable Long id,
        Authentication authentication) {

    String email = authentication.getName();

    List<OrderItemResponseDTO> orderItems =
            orderService.getOrderItems(id, email);

    ResponseDTO<List<OrderItemResponseDTO>> response = new ResponseDTO<>(
            true,
            "Order items fetched successfully",
            orderItems
    );

    return new ResponseEntity<>(response, HttpStatus.OK);
}
}