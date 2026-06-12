package com.example.ArtistBackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {
    @GetMapping("/login")
    public String adminLogin() {
        return "adminLogin";
    }
}
