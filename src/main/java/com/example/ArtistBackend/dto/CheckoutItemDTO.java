package com.example.ArtistBackend.dto;

public class CheckoutItemDTO {
    private Long artworkId;
    private int quantity;


    public Long getArtworkId(){
        return artworkId;
    }
    public void setArtworkId(Long artworkId){
        this.artworkId = artworkId;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

}
