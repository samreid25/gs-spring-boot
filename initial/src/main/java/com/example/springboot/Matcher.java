package com.example.springboot;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.*;

/**
 * Matcher is a class that handles the trading application.
 * Orders are used to contain information on an order.
 *
 * @author Samantha Reid
 */
public class Matcher {
    @NotNull
    protected ArrayList<Order> sell;
    @NotNull
    protected ArrayList<Order> buy;
    @NotNull
    protected ArrayList<Trade> trades;
    protected final String BUY = "buy";
    protected final String SELL = "sell";

    //constructor
    public Matcher() {
        sell = new ArrayList<>();
        buy = new ArrayList<>();
        trades = new ArrayList<>();
    }

    public boolean newOrder(String account, int price, int quantity, String action) {
        // validate input, add more
        if (price > 0 && quantity > 0 && (action.equals(BUY) || action.equals(SELL))) {
            final Date now = new Date();
            if (action.equals(BUY)) {
                    final int id = nextId(buy);
                    @Valid final Order order = new Order(id, account, price, quantity, action, now);
                    final boolean isMatch = lookForMatch(sell, order);
                    if (!isMatch) {
                        // no match
                        noMatch(buy, order); // add to buy list
                    }
            } else { // sell
                    final int id = nextId(sell);
                    final Order order = new Order(id, account, price, quantity, action, now);
                    final boolean isMatch = lookForMatch(buy, order);
                    if (!isMatch) {
                        // no match
                        noMatch(sell, order); // add to buy list
                    }
            }
            return true; // valid input
        } else {
            return false; // invalid input
        }
}

    protected int nextId(ArrayList<Order> buyOrSellList) {
            if (buyOrSellList.size() == 0){
                return 1;
            }
            else {
                ArrayList<Order> tempList = new ArrayList<>(buyOrSellList);
                Comparator<Order> byId = Comparator.comparingInt(Order::getId);
                tempList.sort(byId);
                //tempList.sort(Comparator.comparingInt(Order::getId));
                final int maxId = tempList.get(tempList.size() - 1).getId();
                return maxId + 1;
            }
    }

    protected int nextTradeId(ArrayList<Trade> buyOrSellList) {
        if (buyOrSellList.size() == 0){
            return 1;
        }
        else {
            ArrayList<Trade> tempList = new ArrayList<>(buyOrSellList);
//        Comparator<Order> byId = Comparator.comparingInt(Order::getId);
//        tempList.sort(byId);
            Collections.sort(tempList, Comparator.comparingInt(Trade::getId));
            final int maxId = tempList.get(tempList.size() - 1).getId();
            return maxId + 1;
        }
    }

    protected boolean lookForMatch(ArrayList<Order> buyOrSellList, Order order) {
            for (Order element: buyOrSellList) {
                // want to buy for the lowest price or sell for the highest price
                if (((order.getAction().equals(BUY) && element.getPrice() <= order.getPrice()) ||
                        (order.getAction().equals(SELL) && element.getPrice() >= order.getPrice())) &&
                        order.getQuantity() != 0) {
                    // quantity of the element in the list is the same as the quantity of the order
                    if (element.getQuantity() == order.getQuantity()) {
                        sameQuantity(buyOrSellList, element, order);
                        break;
                    }
                    // quantity of the element in the list is more than the quantity of the order
                    else if (element.getQuantity() > order.getQuantity()) {
                        moreQuantity(buyOrSellList, element, order);
                        break;
                    }
                    // quantity of the element in the list is more than the quantity of the order
                    else if (element.getQuantity() < order.getQuantity()) {
                        lessQuantity(buyOrSellList, element, order);
                        break;
                    }
                } else {
                    break; // there's not a match
                }
            }
        if (order.getQuantity() == 0) {
            return true; // there's nothing left to trade
        } else {
            return false; // must add the remainder of the order to buy or sell list
        }
    }

    protected void sameQuantity(ArrayList<Order> buyOrSellList, Order element, Order order) {
        final int id = nextTradeId(trades);
        // create a trade with existing price and new order quantity
        createTrade(id, element.getPrice(), order.getQuantity());
        buyOrSellList.remove(element); // remove it from the list
        order.setQuantity(0); // make the remaining order quantity equal to 0
        if (order.getAction().equals(BUY)) {
            sell = buyOrSellList;
        } else if (order.getAction().equals(SELL)) {
            buy = buyOrSellList;
        }
    }

    protected void moreQuantity(ArrayList<Order> buyOrSellList, Order element, Order order) {
        final int id = nextTradeId(trades);
        // create a trade with existing price and new order quantity
        createTrade(id, element.getPrice(), order.getQuantity());
        element.setQuantity(element.getQuantity() - order.getQuantity()); // remove the quantity of the new order from the existing element
        order.setQuantity(0); // make the remaining order quantity equal to 0
        if (order.getAction().equals(BUY)) {
            sell = buyOrSellList;
        } else if (order.getAction().equals(SELL)) {
            buy = buyOrSellList;
        }
    }

    protected void lessQuantity(ArrayList<Order> buyOrSellList, Order element, Order order) {
        final int id = nextTradeId(trades);
        // create a trade with existing price and existing quantity
        createTrade(id, element.getPrice(), element.getQuantity());
        order.setQuantity(order.getQuantity() - element.getQuantity()); // remove the existing quantity from the new order to get the remaining quantity
        buyOrSellList.remove(element); // remove it from the list
        if (order.getAction().equals(BUY)) {
            sell = buyOrSellList;
            lookForMatch(buyOrSellList, order); // recursively try to make another match
        } else if (order.getAction().equals(SELL)) {
            buy = buyOrSellList;
            lookForMatch(buyOrSellList, order); // recursively try to make another match
        }
    }

    protected void noMatch(List<Order> buyOrSellList, Order order) {
        buyOrSellList.add(order);
        // sort the list, buy list is sorted by descending price, sell list is sorted by ascending price
        Collections.sort(buyOrSellList, Comparator.comparingInt(Order::getPrice));
        if (order.getAction().equals(SELL)) {
            //need to reverse the sell ist
            Collections.reverse(buyOrSellList);
        }
    }

    protected void createTrade(int id, int price, int quantity) {
        final Date now = new Date();
        @Valid final Trade trade = new Trade(id, price, quantity, now);
        trades.add(trade);
    }

    public ArrayList<Order> getBuy(){
        return this.buy;
    }

    public ArrayList<Order> getSell(){
        return this.sell;
    }

    public ArrayList[] getOrders(){
        ArrayList[] orders = new ArrayList[2];
        orders[0] = this.buy;
        orders[1] = this.sell;
        return orders;
    }
}