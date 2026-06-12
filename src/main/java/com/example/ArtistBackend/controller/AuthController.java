package com.example.ArtistBackend.controller;

import com.example.ArtistBackend.dto.RegisterRequest;

import com.example.ArtistBackend.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
//    @Autowired
//    private CustomerUserDetailsService userService;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

//    public  String logout() {
//        return "logout";
//    }
@PostMapping("/register")
public String register(
        @Valid @ModelAttribute RegisterRequest registerRequest,
        BindingResult result,
        Model model) {

    if (result.hasErrors()) {
        return "register";
    }

    if (!registerRequest.getPassword()
            .equals(registerRequest.getConfirmPassword())) {
        model.addAttribute("passwordError", "Passwords do not match");
        return "register";
    }

    userDetailsService.register(registerRequest);
    return "redirect:/login?registered=true";
}
}
