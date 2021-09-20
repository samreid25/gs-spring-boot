package com.example.springboot;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;

/**
 * Trade type that can be attached to a trade variable declaration.
 * Trades are used to contain information on a trade that has happened.
 *
 * @author Samantha Reid
 */
public class Trade {
    @NotNull @Min(1)
    private int id, price, quantity;
    @NotNull @FutureOrPresent
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
