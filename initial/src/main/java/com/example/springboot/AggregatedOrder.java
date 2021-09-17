package com.example.springboot;

public class AggregatedOrder {
    private int price, quantity, volume;

    AggregatedOrder(int price, int quantity, int volume)
    {
        this.price = price;
        this.quantity = quantity;
        this.volume = volume;
    }

    public int getPrice(){
        return this.price;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public int getVolume(){
        return this.volume;
    }

    public void setPrice(int price){
        this.price = price;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setVolume(int volume){
        this.volume = volume;
    }

}
