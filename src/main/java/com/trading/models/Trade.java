package com.trading.models;

public class Trade {
    private final String tradeId;
    private final OrderType tradeType;
    private final String buyerOrderId;
    private final String sellerOrderId;
    private final String symbol;
    private final int quantity;
    private final double price;
    private final long timestamp;

    public Trade(String tradeId, OrderType tradeType, String buyerOrderId, String sellerOrderId, String symbol, int quantity, double price, long timestamp) {
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.buyerOrderId = buyerOrderId;
        this.sellerOrderId = sellerOrderId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    // ... getters and setters
}
