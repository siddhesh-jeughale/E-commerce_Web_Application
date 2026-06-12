package com.example.ArtistBackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(String to,
                                      String orderNumber,
                                      Double total) {

        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println("Sending from: " + fromEmail);
        System.out.println("Sending to: " + to + " with order number: " + orderNumber);
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Order Confirmation - " + orderNumber);
        message.setText(
                "Thank you for your purchase!\n\n" +
                        "Order Number: " + orderNumber + "\n" +
                        "Total Amount: ₹" + total + "\n\n" +
                        "We will notify you once shipped."
        );

        mailSender.send(message);
    }
}


