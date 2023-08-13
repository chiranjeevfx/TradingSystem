package com.trading.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.trading.models.Order;
import com.trading.models.OrderStatus;
import com.trading.models.Trade;
import com.trading.models.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class TradeSystem {
    private final ConcurrentHashMap<String, User> users;
    private final ConcurrentHashMap<String, Order> orders;
    private final ConcurrentHashMap<String, OrderBook> orderBooks;

    private final List<Trade> trades;
    private final ReentrantLock lock = new ReentrantLock();


    public TradeSystem() {
        this.trades = new CopyOnWriteArrayList<>();
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

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null && order.getStatus() == OrderStatus.ACCEPTED) {
            orderBooks.get(order.getSymbol()).cancelOrder(order);
        }
    }

    public List<Order> getUserOrders(String userId) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getUserId().equals(userId)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }

    public void modifyOrderQuantity(String orderId, int newQuantity) {
        Order order = orders.get(orderId);
        if (order != null) {
            orderBooks.get(order.getSymbol()).modifyOrderQuantity(order, newQuantity);
        }
    }
}
