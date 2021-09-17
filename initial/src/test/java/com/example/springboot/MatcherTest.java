package com.example.springboot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class MatcherTest {
    Matcher matcher = new Matcher();

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("createTrade() adds trade to list")
    public void createTrade() {
        matcher.createTrade(1, 3, 2);
        //should add to trade list
        assertEquals(1, matcher.trades.size());
    }

    @Test
    @DisplayName("noMatch() adds order to buy list")
    public void noMatchBuy() {
        matcher.buy.clear();
        Date now = new Date();
        Order order = new Order(1, "SamReid", 10, 12, "buy", now);
        matcher.noMatch(matcher.buy, order);
        //should add to buy list
        assertEquals(1, matcher.buy.size());
    }

    @Test
    @DisplayName("noMatch() adds order to sell list")
    public void noMatchSell() {
        matcher.sell.clear();
        Date now = new Date();
        Order order = new Order(1, "SamReid", 10, 12, "sell", now);
        matcher.noMatch(matcher.sell, order);
        //should add to sell list
        assertEquals(1, matcher.sell.size());
    }

    @Test
    @DisplayName("noMatch() sorts sell list correctly")
    public void noMatchSortingSellList() {
        matcher.sell.clear();
        matcher.buy.clear();
        Date now = new Date();
        Order firstSellElement = new Order(1, "SamReid", 10, 2, "sell", now);
        Order secondSellElement = new Order(2, "SamReid", 1, 12, "sell", now);
        Order thirdSellElement = new Order(3, "SamReid", 13, 19, "sell", now);
        matcher.noMatch(matcher.sell, secondSellElement); //1
        matcher.noMatch(matcher.sell, thirdSellElement); //13
        matcher.noMatch(matcher.sell, firstSellElement); //10
        //should be the order with the highest price
        assertSame(thirdSellElement, matcher.sell.get(0));
        //should be the order with the middle price
        assertSame(firstSellElement, matcher.sell.get(1));
        //should be the order with the lowest price
        assertSame(secondSellElement, matcher.sell.get(2));
    }

    @Test
    @DisplayName("noMatch() sorts buy list correctly")
    public void noMatchSortingBuyList() {
        matcher.sell.clear();
        matcher.buy.clear();
        Date now = new Date();
        Order firstBuyElement = new Order(1, "SamReid", 13, 2, "buy", now);
        Order secondBuyElement = new Order(2, "SamReid", 1, 12, "buy", now);
        Order thirdBuyElement = new Order(3, "SamReid", 10, 19, "buy", now);
        matcher.noMatch(matcher.buy, firstBuyElement); //13
        matcher.noMatch(matcher.buy, secondBuyElement); //1
        matcher.noMatch(matcher.buy, thirdBuyElement); //10
        //should be the order with the highest price
        assertSame(secondBuyElement, matcher.buy.get(0));
        //should be the order with the middle price
        assertSame(thirdBuyElement, matcher.buy.get(1));
        //should be the order with the lowest price
        assertSame(firstBuyElement, matcher.buy.get(2));
    }

    @Test
    @DisplayName("sameQuantity() correctly buying")
    public void sameQuantityBuy() {
        matcher.sell.clear();
        matcher.buy.clear();
        matcher.trades.clear();
        Date now = new Date();
        Order firstSellElement = new Order(1, "SamReid", 10, 12, "sell", now);
        Order secondSellElement = new Order(2, "SamReid", 12, 2, "sell", now);
        Order buyExample = new Order(3, "SamReid", 12, 12, "buy", now);
        matcher.sell.add(firstSellElement);
        matcher.sell.add(secondSellElement);
        matcher.sameQuantity(matcher.sell, matcher.sell.get(0), buyExample);
        //should add to trade list
        assertEquals(1, matcher.trades.size());
        //trade should have correct price
        assertEquals(10, matcher.trades.get(0).getPrice());
        //trade should have correct quantity
        assertEquals(12, matcher.trades.get(0).getQuantity());
        //sell list should be reduced
        assertEquals(1, matcher.sell.size());
        //sell list second element should now be the first element
        assertEquals(secondSellElement, matcher.sell.get(0));
        //buy list should still have no elements
        assertEquals(0, matcher.buy.size());
    }

    @Test
    @DisplayName("sameQuantity() correctly selling")
    public void sameQuantitySell() {
        matcher.sell.clear();
        matcher.buy.clear();
        matcher.trades.clear();
        Date now = new Date();
        Order firstBuyElement = new Order(1, "SamReid", 19, 1, "buy", now);
        Order secondBuyElement = new Order(2, "SamReid", 13, 14, "buy", now);
        Order sellExample = new Order(3, "SamReid", 18, 1, "sell", now);
        matcher.buy.add(firstBuyElement);
        matcher.buy.add(secondBuyElement);
        matcher.sameQuantity(matcher.buy, matcher.buy.get(0), sellExample);
        //should add to trade list
        assertEquals(1, matcher.trades.size());
        //trade should have correct price
        assertEquals(19, matcher.trades.get(0).getPrice());
        //trade should have correct quantity
        assertEquals(1, matcher.trades.get(0).getQuantity());
        //buy list should be reduced
        assertEquals(1, matcher.buy.size());
        //buy list second element should now be the first element
        assertEquals(secondBuyElement, matcher.buy.get(0));
        //sell list should still have no elements
        assertEquals(0, matcher.sell.size());
    }

    @Test
    @DisplayName("moreQuantity() correctly buying")
    public void moreQuantityBuy() {
        matcher.sell.clear();
        matcher.buy.clear();
        matcher.trades.clear();
        Date now = new Date();
        Order firstSellElement = new Order(1, "SamReid", 10, 17, "sell", now);
        Order secondSellElement = new Order(2, "SamReid", 12, 12, "sell", now);
        Order buyExample = new Order(3, "SamReid", 12, 12, "buy", now);
        matcher.sell.add(firstSellElement);
        matcher.sell.add(secondSellElement);
        matcher.moreQuantity(matcher.sell, matcher.sell.get(0), buyExample);
        //should add to trade list
        assertEquals(1, matcher.trades.size());
        //trade should have correct price
        assertEquals(10, matcher.trades.get(0).getPrice());
        //trade should have correct quantity
        assertEquals(12, matcher.trades.get(0).getQuantity());
        //sell list length should be the same
        assertEquals(2, matcher.sell.size());
        //quantity of the first element in sell list should be reduced
        assertEquals(5, matcher.sell.get(0).getQuantity());
        //price of the first element in sell list should be unchanged
        assertEquals(10, matcher.sell.get(0).getPrice());
        //second element in sell list should be unchanged
        assertEquals(secondSellElement, matcher.sell.get(1));
        //buy list should still have no elements
        assertEquals(0, matcher.buy.size());
    }

    @Test
    @DisplayName("moreQuantity() correctly selling")
    public void moreQuantitySell() {
        matcher.sell.clear();
        matcher.buy.clear();
        matcher.trades.clear();
        Date now = new Date();
        Order firstBuyElement = new Order(1, "SamReid", 18, 19, "buy", now);
        Order secondBuyElement = new Order(2, "SamReid", 7, 12, "buy", now);
        Order sellExample = new Order(3, "SamReid", 16, 12, "sell", now);
        matcher.buy.add(firstBuyElement);
        matcher.buy.add(secondBuyElement);
        matcher.moreQuantity(matcher.buy, matcher.buy.get(0), sellExample);
        //should add to trade list
        assertEquals(1, matcher.trades.size());
        //trade should have correct price
        assertEquals(18, matcher.trades.get(0).getPrice());
        //trade should have correct quantity
        assertEquals(12, matcher.trades.get(0).getQuantity());
        //buy list length should be the same
        assertEquals(2, matcher.buy.size());
        //quantity of the first element in buy list should be reduced
        assertEquals(7, matcher.buy.get(0).getQuantity());
        //price of the first element in buy list should be unchanged
        assertEquals(18, matcher.buy.get(0).getPrice());
        //second element in buy list should be unchanged
        assertEquals(secondBuyElement, matcher.buy.get(1));
        //sell list should still have no elements
        assertEquals(0, matcher.sell.size());
    }

    @Test
    @DisplayName("lessQuantity() correctly buying")
    public void lessQuantityBuy() {
        matcher.sell.clear();
        matcher.buy.clear();
        matcher.trades.clear();
        Date now = new Date();
        Order firstSellElement = new Order(1, "SamReid", 10, 17, "sell", now);
        Order secondSellElement = new Order(2, "SamReid", 12, 12, "sell", now);
        Order buyExample = new Order(3, "SamReid", 12, 20, "buy", now);
        matcher.sell.add(firstSellElement);
        matcher.sell.add(secondSellElement);
        matcher.lessQuantity(matcher.sell, matcher.sell.get(0), buyExample);
        //should add two trades to the list
        assertEquals(2, matcher.trades.size());
        //first trade should have correct price
        assertEquals(10, matcher.trades.get(0).getPrice());
        //first trade should have correct quantity
        assertEquals(17, matcher.trades.get(0).getQuantity());
        //second trade should have correct price
        assertEquals(12, matcher.trades.get(1).getPrice());
        //second trade should have correct quantity
        assertEquals(3, matcher.trades.get(1).getQuantity());
        //sell list length should have reduced by 1
        assertEquals(1, matcher.sell.size());
        //quantity of the element in sell list should be reduced
        assertEquals(9, matcher.sell.get(0).getQuantity());
        //price of the element in sell list should be unchanged
        assertEquals(12, matcher.sell.get(0).getPrice());
        //buy list should still have no elements
        assertEquals(0, matcher.buy.size());
    }

    @Test
    @DisplayName("lessQuantity() correctly selling")
    public void lessQuantitySell() {
        matcher.sell.clear();
        matcher.buy.clear();
        matcher.trades.clear();
        Date now = new Date();
        Order firstBuyElement = new Order(1, "SamReid", 19, 7, "buy", now);
        Order secondBuyElement = new Order(2, "SamReid", 12, 12, "buy", now);
        Order thirdBuyElement = new Order(3, "SamReid", 2, 20, "buy", now);
        Order sellExample = new Order(4, "SamReid", 2, 28, "sell", now);
        matcher.buy.add(firstBuyElement);
        matcher.buy.add(secondBuyElement);
        matcher.buy.add(thirdBuyElement);
        matcher.lessQuantity(matcher.buy, matcher.buy.get(0), sellExample);
        //should add three trades to the list
        assertEquals(3, matcher.trades.size());
        //first trade should have correct price
        assertEquals(19, matcher.trades.get(0).getPrice());
        //first trade should have correct quantity
        assertEquals(7, matcher.trades.get(0).getQuantity());
        //second trade should have correct price
        assertEquals(12, matcher.trades.get(1).getPrice());
        //second trade should have correct quantity
        assertEquals(12, matcher.trades.get(1).getQuantity());
        //third trade should have correct price
        assertEquals(2, matcher.trades.get(2).getPrice());
        //third trade should have correct quantity
        assertEquals(9, matcher.trades.get(2).getQuantity());
        //buy list length should have reduced by 2
        assertEquals(1, matcher.buy.size());
        //quantity of the element in buy list should be reduced
        assertEquals(11, matcher.buy.get(0).getQuantity());
        //price of the element in buy list should be unchanged
        assertEquals(2, matcher.buy.get(0).getPrice());
        //sell list should still have no elements
        assertEquals(0, matcher.sell.size());
    }

    @Test
    @DisplayName("newOrder() correctly having left over when buying")
    public void newOrderBuyLeftOver() {
        matcher.sell.clear();
        matcher.buy.clear();
        matcher.trades.clear();
        Date now = new Date();
        Order firstSellElement = new Order(1, "SamReid", 10, 17, "sell", now);
        Order secondSellElement = new Order(2, "SamReid", 12, 12, "sell", now);
        matcher.sell.add(firstSellElement);
        matcher.sell.add(secondSellElement);
        matcher.newOrder("SamReid", 12, 40, "buy");
        //should add two trades to the list
        assertEquals(2, matcher.trades.size());
        //first trade should have correct price
        assertEquals(10, matcher.trades.get(0).getPrice());
        //first trade should have correct quantity
        assertEquals(17, matcher.trades.get(0).getQuantity());
        //second trade should have correct price
        assertEquals(12, matcher.trades.get(1).getPrice());
        //second trade should have correct quantity
        assertEquals(12, matcher.trades.get(1).getQuantity());
        //sell list length should be empty
        assertEquals(0, matcher.sell.size());
        //buy list should have the 1 element
        assertEquals(1, matcher.buy.size());
        //element should have correct price
        assertEquals(12, matcher.buy.get(0).getPrice());
        //element should have correct remaining quantity
        assertEquals(11, matcher.buy.get(0).getQuantity());
    }

    @Test
    @DisplayName("newOrder() correctly having left over when selling")
    public void newOrderSellLeftOver() {
        matcher.sell.clear();
        matcher.buy.clear();
        matcher.trades.clear();
        Date now = new Date();
        Order firstBuyElement = new Order(1, "SamReid", 106, 17, "buy", now);
        Order secondBuyElement = new Order(2, "SamReid", 42, 26, "buy", now);
        Order thirdBuyElement = new Order(2, "SamReid", 19, 16, "buy", now);
        matcher.buy.add(firstBuyElement);
        matcher.buy.add(secondBuyElement);
        matcher.buy.add(thirdBuyElement);
        matcher.newOrder("SamReid", 100, 100, "sell");
        //should add two trades to the list
        assertEquals(1, matcher.trades.size());
        //trade should have correct price
        assertEquals(106, matcher.trades.get(0).getPrice());
        //trade should have correct quantity
        assertEquals(17, matcher.trades.get(0).getQuantity());
        //buy list length should be reduced by 1
        assertEquals(2, matcher.buy.size());
        //sell list should have the 1 element
        assertEquals(1, matcher.sell.size());
        //element should have correct price
        assertEquals(100, matcher.sell.get(0).getPrice());
        //element should have correct remaining quantity
        assertEquals(83, matcher.sell.get(0).getQuantity());
    }
    //do for handling errors
}
