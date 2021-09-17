package com.example.springboot;

import java.util.Date;

public class Order {
    private int id, price, quantity;
    private String account, action;
    private Date dateTime;

    Order(int id, String account, int price, int quantity, String action, Date dateTime)
    {
        this.id = id;
        this.account = account;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
        this.dateTime = dateTime;
    }

    public int getId(){
        return this.id;
    }
    public String getAccount(){
        return this.account;
    }
    public int getPrice(){
        return this.price;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public String getAction(){
        return this.action;
    }
    public Date getDateTime(){
        return this.dateTime;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setAccount(String account){
        this.account = account;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setAction(String action){
        this.action = action;
    }
    public void setDateTime(Date dateTime){
        this.dateTime = dateTime;
    }
}
