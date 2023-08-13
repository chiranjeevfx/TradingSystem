package com.trading.models;

public class Order {
    private final String orderId;
    private final String userId;
    private final OrderType orderType;  // Enum: BUY, SELL
    private final String symbol;
    private int quantity;
    private final double price;
    private final long timestamp;
    private OrderStatus status;  // Enum: ACCEPTED, REJECTED, CANCELED

    public Order(String orderId, String userId, OrderType orderType,
                 String symbol, int quantity, double price) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderType = orderType;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = System.currentTimeMillis();
        this.status = OrderStatus.ACCEPTED;
    }

    // getters and setters
    public String getOrderId() {
        return orderId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


}
