package com.example.ArtistBackend.controller;

import com.example.ArtistBackend.dto.CheckoutItemDTO;
import com.example.ArtistBackend.dto.CheckoutRequest;
import com.example.ArtistBackend.model.ArtWork;
import com.example.ArtistBackend.model.Order;
import com.example.ArtistBackend.model.OrderItem;
import com.example.ArtistBackend.model.User;
import com.example.ArtistBackend.repository.ArtworkRepo;
import com.example.ArtistBackend.repository.OrderRepository;
import com.example.ArtistBackend.repository.UserRepository;
import com.example.ArtistBackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ArtworkRepo artworkRepo;
    @Autowired
    private OrderService orderService;
    
    
    @GetMapping
    public String checkoutPage(Model model, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        model.addAttribute("user", user);


        return "checkout";
    }

    @PostMapping("/place-order")
    @ResponseBody
    public ResponseEntity<Map<String, String>> placeOrder(
            @RequestBody CheckoutRequest request,
            Authentication auth
    ) {
        System.out.println("we are in checkout controller place order......");
        Order order = orderService.createOrder(request, auth);

        // Return JSON so fetch() in the browser can read the redirect URL
        // and navigate with window.location.href (fetch cannot trigger browser navigation via 302)
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", "/checkout/order-success/" + order.getOrderNumber());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/order-success/{orderNumber}")
    public String orderSuccess( @PathVariable String orderNumber,
                               Model model,
                               Authentication auth) {


        Order order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow();

        if (!order.getUser().getEmail().equals(auth.getName())) {
            throw new RuntimeException("Unauthorized access");
        }

        model.addAttribute("order", order);
        model.addAttribute("user", order.getUser());

        return "order-success";
    }

}
