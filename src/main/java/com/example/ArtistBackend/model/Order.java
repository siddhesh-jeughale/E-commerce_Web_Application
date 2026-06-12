package com.example.ArtistBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // PUBLIC ORDER NUMBER (shown to user)
        @Column(unique = true, nullable = false)
        private String orderNumber;

        @ManyToOne
        private User user;

        private Double subtotal;
        private Double shipping;
        private Double total;

        private String paymentMethod; // COD, CARD, UPI (later)
//        private String status;        // CREATED, PAID, SHIPPED

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PROCESSING;

        private LocalDateTime createdAt = LocalDateTime.now();

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItem> items = new ArrayList<>();

        @PrePersist
        private void generateOrderNumber() {
          this.orderNumber = "ORD-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
}

