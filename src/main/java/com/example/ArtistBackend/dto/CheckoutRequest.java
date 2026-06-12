package com.example.ArtistBackend.dto;

import java.util.Arrays;
import java.util.List;

public class CheckoutRequest {
    private List<CheckoutItemDTO> items;
    private String paymentMethod;

    public List<CheckoutItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CheckoutItemDTO> items) {
        this.items = items;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
