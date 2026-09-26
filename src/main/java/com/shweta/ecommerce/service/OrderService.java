package com.shweta.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.OrderItemRequestDTO;
import com.shweta.ecommerce.dto.OrderRequestDTO;
import com.shweta.ecommerce.dto.OrderItemResponseDTO;
import com.shweta.ecommerce.entity.Order;
import com.shweta.ecommerce.entity.OrderItem;
import com.shweta.ecommerce.entity.OrderStatus;
import com.shweta.ecommerce.entity.Product;
import com.shweta.ecommerce.entity.User;
import com.shweta.ecommerce.repository.OrderItemRepository;
import com.shweta.ecommerce.repository.OrderRepository;
import com.shweta.ecommerce.repository.ProductRepository;
import com.shweta.ecommerce.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import com.shweta.ecommerce.entity.Payment;
import com.shweta.ecommerce.entity.PaymentStatus;
import com.shweta.ecommerce.repository.PaymentRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
private PaymentRepository paymentRepository;

    // Create Order
    @Transactional
public Order createOrder(OrderRequestDTO request, String email) {
        // 1. Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Create new Order
        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());

        // 3. Calculate total amount
        double totalAmount = 0.0;

        List<OrderItem> orderItems = new ArrayList<>();

        // 4. Process each item
        for (OrderItemRequestDTO itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id: "
                                    + itemRequest.getProductId()
                    ));

            // 5. Check available stock
            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }

            // 6. Reduce product stock
            product.setQuantity(
                    product.getQuantity() - itemRequest.getQuantity()
            );

            productRepository.save(product);

            // 7. Calculate item total
            double itemTotal =
                    product.getPrice() * itemRequest.getQuantity();

            totalAmount += itemTotal;

            // 8. Create OrderItem
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());

            // Store price at the time of purchase
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
        }

        // 9. Set total amount
        order.setTotalAmount(totalAmount);

        // 10. Save Order
        Order savedOrder = orderRepository.save(order);

        // 11. Save OrderItems
        for (OrderItem orderItem : orderItems) {

            orderItem.setOrder(savedOrder);

            orderItemRepository.save(orderItem);
        }

        // 12. Return saved order
        return savedOrder;
    }

    // Get My Orders
    public List<Order> getMyOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user);
    }

    // Get Order By ID
    public Order getOrderById(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findById(id)
                .filter(order ->
                        order.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException(
                        "Order not found or you are not authorized to view this order"
                ));
    }

    // Update Order Status
    public Order updateOrderStatus(Long id, OrderStatus status) {

    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                    "Order not found with id: " + id
            ));

    OrderStatus currentStatus = order.getStatus();

    // Prevent changing a cancelled order
    if (currentStatus == OrderStatus.CANCELLED) {
        throw new RuntimeException(
                "Cancelled order cannot be updated"
        );
    }

    // Prevent changing a delivered order
    if (currentStatus == OrderStatus.DELIVERED) {
        throw new RuntimeException(
                "Delivered order cannot be updated"
        );
    }

    // Validate order status flow
    if (currentStatus == OrderStatus.PLACED
            && status != OrderStatus.CONFIRMED) {

        throw new RuntimeException(
                "PLACED order can only be changed to CONFIRMED"
        );
    }

    if (currentStatus == OrderStatus.CONFIRMED
            && status != OrderStatus.SHIPPED) {

        throw new RuntimeException(
                "CONFIRMED order can only be changed to SHIPPED"
        );
    }

    if (currentStatus == OrderStatus.SHIPPED
            && status != OrderStatus.DELIVERED) {

        throw new RuntimeException(
                "SHIPPED order can only be changed to DELIVERED"
        );
    }

    order.setStatus(status);

    return orderRepository.save(order);
}
    // Cancel Order
    @Transactional
public Order cancelOrder(Long id, String email) {

    // 1. Find logged-in user
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 2. Find order and check ownership
    Order order = orderRepository.findById(id)
            .filter(existingOrder ->
                    existingOrder.getUser().getId().equals(user.getId()))
            .orElseThrow(() -> new RuntimeException(
                    "Order not found or you are not authorized to cancel this order"
            ));

    // 3. Check if order is already cancelled
if (order.getStatus() == OrderStatus.CANCELLED) {
    throw new RuntimeException("Order is already cancelled");
}

// 4. Delivered orders cannot be cancelled
if (order.getStatus() == OrderStatus.DELIVERED) {
    throw new RuntimeException(
            "Delivered order cannot be cancelled"
    );
}

    // 4. Find all items in the order
    List<OrderItem> orderItems =
            orderItemRepository.findByOrder(order);

    // 5. Restore product stock
    for (OrderItem orderItem : orderItems) {

        Product product = orderItem.getProduct();

        product.setQuantity(
                product.getQuantity() + orderItem.getQuantity()
        );

        productRepository.save(product);
    }
    // Refund payment if payment exists
paymentRepository.findByOrder(order).ifPresent(payment -> {
    payment.setStatus(PaymentStatus.REFUNDED);
    paymentRepository.save(payment);
});

    // 6. Change order status
    order.setStatus(OrderStatus.CANCELLED);

    // 7. Save updated order
    return orderRepository.save(order);
}

    // Get Order Items
    public List<OrderItemResponseDTO> getOrderItems(
            Long orderId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .filter(existingOrder ->
                        existingOrder.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException(
                        "Order not found or you are not authorized to view this order"
                ));

        List<OrderItem> orderItems =
                orderItemRepository.findByOrder(order);

        List<OrderItemResponseDTO> responseList =
                new ArrayList<>();

        for (OrderItem orderItem : orderItems) {

            Product product = orderItem.getProduct();

            OrderItemResponseDTO response =
                    new OrderItemResponseDTO(
                            orderItem.getId(),
                            product.getId(),
                            product.getName(),
                            orderItem.getQuantity(),
                            orderItem.getPrice()
                    );

            responseList.add(response);
        }

        return responseList;
    }
}