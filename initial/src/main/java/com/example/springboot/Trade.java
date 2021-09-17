package com.example.springboot;

import java.util.Date;

public class Trade {
    private int id, price, quantity;
    private Date dateTime;

    Trade(int id, int price, int quantity, Date dateTime)
    {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.dateTime = dateTime;
    }

    public int getId(){
        return this.id;
    }
    public int getPrice(){
        return this.price;
    }
    public int getQuantity(){
        return this.quantity;
    }

    public Date getDateTime(){
        return this.dateTime;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setDateTime(Date dateTime){
        this.dateTime = dateTime;
    }
}
