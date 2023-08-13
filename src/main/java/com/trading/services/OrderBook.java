package com.trading.services;
import com.trading.models.Order;

import java.util.PriorityQueue;
import com.trading.models.Order;
import com.trading.models.OrderStatus;
import com.trading.models.OrderType;
import java.util.PriorityQueue;

public class OrderBook {
    private final String symbol;
    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;

    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.buyOrders = new PriorityQueue<>(this::compareOrders);
        this.sellOrders = new PriorityQueue<>(this::compareOrders);
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
                // Record the trade (omitted for brevity)
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
}
