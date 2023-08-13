package com.trading.models;

public class Trade {
    /*
    * Trades
Trade ID
Trade Type (Buy/Sell)
Buyer Order Id
Seller Order Id
Stock Symbol
Quantity
Price
Trade Timestamp
*
    * */

    private final String tradeId;
    private final String tradeType;
    private final String buyerOrderId;
    private final String sellerOrderId;
    private final String stockSymbol;

    private final int quantity;
    private final int price;
    private final String tradeTimestamp;

    public Trade(String tradeId, String tradeType, String buyerOrderId, String sellerOrderId, String stockSymbol, int quantity, int price, String tradeTimestamp) {
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.buyerOrderId = buyerOrderId;
        this.sellerOrderId = sellerOrderId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.tradeTimestamp = tradeTimestamp;
    }



}
