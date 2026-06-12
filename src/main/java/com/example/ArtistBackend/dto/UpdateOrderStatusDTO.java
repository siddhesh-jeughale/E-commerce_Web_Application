package com.example.ArtistBackend.dto;

import com.example.ArtistBackend.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusDTO {

    private String orderNumber;
    private OrderStatus status;
}
