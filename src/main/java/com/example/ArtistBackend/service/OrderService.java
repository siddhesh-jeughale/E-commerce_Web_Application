package com.example.ArtistBackend.service;

import com.example.ArtistBackend.dto.CheckoutItemDTO;
import com.example.ArtistBackend.dto.CheckoutRequest;
import com.example.ArtistBackend.model.*;
import com.example.ArtistBackend.repository.ArtworkRepo;
import com.example.ArtistBackend.repository.OrderRepository;
import com.example.ArtistBackend.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArtworkRepo artworkRepo;
    @Autowired
    private EmailService emailService;

    public Order createOrder( CheckoutRequest request,
                              Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));
        Order order = new Order();
            order.setUser(user);
            order.setStatus(OrderStatus.PROCESSING);
            order.setPaymentMethod(request.getPaymentMethod());

            double subtotal = 0;

            for (CheckoutItemDTO dto : request.getItems()) {
                ArtWork artwork = artworkRepo.findById(dto.getArtworkId())
                        .orElseThrow(() -> new RuntimeException("Artwork not found with id: " + dto.getArtworkId()));

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setArtwork(artwork);
                item.setQuantity(dto.getQuantity());
                item.setPrice(artwork.getPrice());

                subtotal += artwork.getPrice() * dto.getQuantity();
                order.getItems().add(item);
            }

            order.setSubtotal(subtotal);
            order.setShipping(50.0);
            order.setTotal(subtotal + 50);

//        try {
//            emailService.sendOrderConfirmation(
//                    user.getEmail(),
//                    order.getOrderNumber(),
//                    order.getTotal()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Email failed. Order not created.");
//        }


        Order savedOrder = orderRepository.save(order);
        try {
            emailService.sendOrderConfirmation(
                    user.getEmail(),
                    savedOrder.getOrderNumber(),
                    savedOrder.getTotal()
            );
        } catch (Exception e) {
            System.err.println("Failed to send order confirmation email: " + e.getMessage());
        }


        return savedOrder;
    }


    public void updateOrderStatus(String orderNumber, OrderStatus status) {
        System.out.println("order number:------ " + orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        orderRepository.save(order);
    }
}
