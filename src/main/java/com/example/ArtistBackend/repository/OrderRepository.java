package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.Order;
import com.example.ArtistBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findAllByOrderByIdDesc();
    List<Order> findByUserOrderByIdDesc(User user);
}