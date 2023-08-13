package com.trading.services;
import com.trading.models.Order;

import java.util.PriorityQueue;
import com.trading.models.OrderStatus;
import com.trading.models.OrderType;

public class OrderBook {
    private final String symbol;
    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;

//    public OrderBook(String symbol) {
//        this.symbol = symbol;
//        this.buyOrders = new PriorityQueue<>(this::compareOrders);
//        this.sellOrders = new PriorityQueue<>(this::compareOrders);
//    }
    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.buyOrders = new PriorityQueue<>((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice())); // Highest price first for BUY
        this.sellOrders = new PriorityQueue<>((o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice())); // Lowest price first for SELL
    }


    private int compareOrders(Order o1, Order o2) {
        if (o1.getPrice() != o2.getPrice()) {
            return Double.compare(o1.getPrice(), o2.getPrice());
        }
        return Long.compare(o1.getTimestamp(), o2.getTimestamp());
    }

    public synchronized void addOrder(Order order) {
        if (order.getOrderType() == OrderType.BUY) {
            matchOrder(order, sellOrders);
            if (order.getStatus() != OrderStatus.CANCELED) {
                buyOrders.add(order);
            }
        } else {
            matchOrder(order, buyOrders);
            if (order.getStatus() != OrderStatus.CANCELED) {
                sellOrders.add(order);
            }
        }
    }

    private void matchOrder(Order newOrder, PriorityQueue<Order> oppositeOrders) {
        while (!oppositeOrders.isEmpty() && newOrder.getQuantity() > 0) {
            Order topOrder = oppositeOrders.peek();
            if ((newOrder.getOrderType() == OrderType.BUY && newOrder.getPrice() >= topOrder.getPrice())
                    || (newOrder.getOrderType() == OrderType.SELL && newOrder.getPrice() <= topOrder.getPrice())) {
                int tradedQty = Math.min(newOrder.getQuantity(), topOrder.getQuantity());
                // TODO: Record the trade
                newOrder.setQuantity(newOrder.getQuantity() - tradedQty);
                topOrder.setQuantity(topOrder.getQuantity() - tradedQty);
                if (topOrder.getQuantity() == 0) {
                    oppositeOrders.poll();
                }
            } else {
                break;
            }
        }
        if (newOrder.getQuantity() == 0) {
            newOrder.setStatus(OrderStatus.CANCELED); // indicating it's fully matched
        }
    }

    public PriorityQueue<Order> getBuyOrders() {
        return buyOrders;
    }

    public void flush() {
        buyOrders.clear();
        sellOrders.clear();
    }

    public synchronized void cancelOrder(Order order) {
        if (order.getOrderType() == OrderType.BUY) {
            buyOrders.remove(order);
        } else {
            sellOrders.remove(order);
        }
        order.setStatus(OrderStatus.CANCELED);
    }

    public synchronized void modifyOrderQuantity(Order order, int newQuantity) {
        if (order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot modify a completed or canceled order.");
        }

        if (order.getOrderType() == OrderType.BUY) {
            buyOrders.remove(order);
        } else {
            sellOrders.remove(order);
        }

        order.setQuantity(newQuantity);

        if (order.getOrderType() == OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }


}
