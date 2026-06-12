package com.example.ArtistBackend.controller;

import com.example.ArtistBackend.model.Order;
import com.example.ArtistBackend.model.User;
import com.example.ArtistBackend.repository.OrderRepository;
import com.example.ArtistBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class UserOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String myOrders(Authentication auth, Model model) {


//        handle this exception when user directly visit to the order page
//                without login

        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            return "orders";
        }
        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        List<Order> orders =
                orderRepository.findByUserOrderByIdDesc(user);

        model.addAttribute("orders", orders);

        return "orders";
    }
}

