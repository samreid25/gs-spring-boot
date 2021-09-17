package com.example.springboot;

//import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.*;

public class Matcher {
    protected ArrayList<Order> sell;
    protected ArrayList<Order> buy;
    protected ArrayList<Trade> trades;
    //tree map, keys are sorted in ascending order
    //protected TreeMap<Integer, AggregatedOrder> aggregatedBuyList;
    //protected TreeMap<Integer, AggregatedOrder> aggregatedSellList;
    protected final String BUY = "buy";
    protected final String SELL = "sell";

    //constructor
    public Matcher() {
        sell = new ArrayList<>();
        buy = new ArrayList<>();
        trades = new ArrayList<>();
        //aggregatedBuyList = new TreeMap<>();
        //aggregatedSellList = new TreeMap<>();
    }

    public boolean newOrder(String account, int price, int quantity, String action) {
        // validate input, add more
        if (action.equals(BUY) || action.equals(SELL)) {
            final Date now = new Date();
            //boolean newOrder = false;
            if (action.equals(BUY)) {
                    final int id = nextId(buy);
                    final Order order = new Order(id, account, price, quantity, action, now);
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
                //does this work?
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
            //does this work?
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
//        if (order.getAction().equals(BUY)) {
//            aggregatedSellList = updateAggregatedOrderList(
//                    aggregatedSellList,
//                    order.getQuantity(),
//                    element.getPrice()
//            );
//        } else if (order.getAction().equals(SELL)) {
//            aggregatedBuyList = updateAggregatedOrderList(
//                    aggregatedBuyList,
//                    order.getQuantity(),
//                    element.getPrice()
//            );
//        }
        // ------------- does this work? ---------
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
//        if (order.getAction().equals(BUY)) {
//            aggregatedSellList = updateAggregatedOrderList(
//                    aggregatedSellList,
//                    order.getQuantity(),
//                    element.getPrice()
//            );
//        } else if (order.getAction().equals(SELL)) {
//            aggregatedBuyList = updateAggregatedOrderList(
//                    aggregatedBuyList,
//                    order.getQuantity(),
//                    element.getPrice()
//            );
   //     }
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
//        if (order.getAction().equals(BUY)) {
//            aggregatedSellList = updateAggregatedOrderList(
//                    aggregatedSellList,
//                    order.getQuantity(),
//                    element.getPrice()
//            );
//        } else if (order.getAction().equals(SELL)) {
//            aggregatedBuyList = updateAggregatedOrderList(
//                    aggregatedBuyList,
//                    order.getQuantity(),
//                    element.getPrice()
//            );
//        }
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
        if (order.getAction().equals(SELL)) {
            List<Order> tempList = new ArrayList<>(buyOrSellList);
            // sort the list, sell list is sorted by ascending price then date
            Comparator<Order> byPrice = Comparator.comparingInt(Order::getPrice);
            Comparator<Order> byDate = Comparator.comparingLong(t -> t.getDateTime().getTime());
//             (t1,t2) -> Integer.compare(t1.getPrice(), t2.getPrice());
//             (t1,t2) -> Long.compare(t1.getDateTime().getTime(), t2.getDateTime().getTime());
            buyOrSellList.sort(byPrice.thenComparing(byDate));
            // add to aggregatedSellList
//            addToAggregatedOrderList(aggregatedSellList, order);
            // map is sorted ascending by price, however need to change to descending --------------------------
        } else if (order.getAction().equals(BUY)) {
            List<Order> tempList = new ArrayList<>(buyOrSellList);
            // sort the list,  buy list is sorted by descending price then date ------ CHANGE -------- this is currently ascending by price but needs to be desc
            Comparator<Order> byPrice = Comparator.comparingInt(Order::getPrice);
            Comparator<Order> byDate = Comparator.comparingLong(t -> t.getDateTime().getTime());
            // (t1,t2) -> Integer.compare(t1.getPrice(), t2.getPrice());
            // (t1,t2) -> Long.compare(t1.getDateTime().getTime(), t2.getDateTime().getTime());
            buyOrSellList.sort(byPrice.thenComparing(byDate));
            // add to aggregatedBuyList
//            addToAggregatedOrderList(aggregatedBuyList, order);
        }
    }

//    protected void addToAggregatedOrderList (TreeMap<Integer, AggregatedOrder> aggregatedBuyOrSellList, Order order) {
//        AggregatedOrder temp;
//        // if it already contains the price, add it to the quantity
//        if (aggregatedBuyOrSellList.containsKey(order.getPrice())) {
//            temp = aggregatedBuyOrSellList.get(order.getPrice());
//            temp.quantity += order.getQuantity();
//        }
//        //if not in the list, add it to the list
//        else {
//            temp = new AggregatedOrder(order.getPrice(), order.getQuantity(), order.getQuantity());
//        }
//        aggregatedBuyOrSellList.put(order.getPrice(), temp);
//    }

//    protected TreeMap<Integer, AggregatedOrder> updateAggregatedOrderList (TreeMap<Integer, AggregatedOrder> aggregatedBuyOrSellList, int quantity, int price) {
//        // if it already contains the price, minus it from the quantity
//        if (aggregatedBuyOrSellList.containsKey(price)) {
//            AggregatedOrder temp = aggregatedBuyOrSellList.get(price);
//            temp.quantity -= quantity;
//            if (temp.quantity <= 0) {
//                // remove from the list if there's no quantity left
//                aggregatedBuyOrSellList.remove(price);
//            } else {
//                aggregatedBuyOrSellList.put(price, temp);
//            }
//        }
//        return aggregatedBuyOrSellList;
//    }

//    protected Object getAggregatedOrderBook() {
//        ArrayList<AggregatedOrder> aggregatedBuy = new ArrayList<>();
//        ArrayList<AggregatedOrder> aggregatedSell = new ArrayList<>();
//        for (Map.Entry<Integer, AggregatedOrder> element: aggregatedBuyList.entrySet()){
//            AggregatedOrder temp = new AggregatedOrder(element.getValue().price, element.getValue().quantity, element.getValue().quantity);
//            aggregatedBuy.add(temp);
//            if (aggregatedBuy.size() > 1) {
//                // update the volume
//                aggregatedBuy.get(aggregatedBuy.size() - 1).volume +=
//                        aggregatedBuy.get(aggregatedBuy.size() - 2).volume;
//            }
//        }
//        for (Map.Entry<Integer, AggregatedOrder> element: aggregatedSellList.entrySet()) {
//            AggregatedOrder temp = new AggregatedOrder(element.getValue().price, element.getValue().quantity, element.getValue().quantity);
//            aggregatedSell.add(temp);
//            if (aggregatedSell.size() > 1) {
//                // update the volume
//                aggregatedSell.get(aggregatedSell.size() - 1).volume +=
//                        aggregatedSell.get(aggregatedSell.size() - 2).volume;
//            }
//        }
//        return new Object[]{aggregatedBuy, aggregatedSell};
//    }

    protected void createTrade(int id, int price, int quantity) {
        final Date now = new Date();
        final Trade trade = new Trade(id, price, quantity, now);
        trades.add(trade);
    }
}