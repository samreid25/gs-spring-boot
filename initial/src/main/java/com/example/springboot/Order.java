package com.example.springboot;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;

/**
 * Order type that can be attached to an order variable declaration.
 * Orders are used to contain information on an order.
 *
 * @author Samantha Reid
 */
public class Order {
    @NotNull @Min(1)
    private int id, price, quantity;
    @NotNull
    private String account, action;
    @NotNull @FutureOrPresent
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
