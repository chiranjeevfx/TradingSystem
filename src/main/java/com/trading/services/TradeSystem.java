package com.trading.services;

import java.util.Map;
import com.trading.models.Order;
import com.trading.models.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TradeSystem {
    private final ConcurrentHashMap<String, User> users;
    private final ConcurrentHashMap<String, Order> orders;
    private final ConcurrentHashMap<String, OrderBook> orderBooks;
    private final ReentrantLock lock = new ReentrantLock();

    public TradeSystem() {
        this.users = new ConcurrentHashMap<>();
        this.orders = new ConcurrentHashMap<>();
        this.orderBooks = new ConcurrentHashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void placeOrder(Order order) {
        orderBooks.putIfAbsent(order.getSymbol(), new OrderBook(order.getSymbol()));
        lock.lock();
        try {
            orders.put(order.getOrderId(), order);
            orderBooks.get(order.getSymbol()).addOrder(order);
        } finally {
            lock.unlock();
        }
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Order> getOrders() {
        return orders;
    }

    public Map<String, OrderBook> getOrderBooks() {
        return orderBooks;
    }


    // Additional methods can be added for order cancellation, fetching user orders, etc.
}
