package com.trading.utils;

import com.trading.models.Order;
import com.trading.models.OrderType;
import com.trading.models.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

public class TradeMatcher {

    public static List<Trade> matchOrder(Order newOrder, PriorityQueue<Order> oppositeOrders) {
        List<Trade> trades = new ArrayList<>();

        while (!oppositeOrders.isEmpty()) {
            Order topOrder = oppositeOrders.peek();

            if (canMatch(newOrder, topOrder)) {
                String tradeId = UUID.randomUUID().toString();
                int tradedQuantity = Math.min(newOrder.getQuantity(), topOrder.getQuantity());

                trades.add(new Trade(
                        tradeId,
                        newOrder.getOrderType() == OrderType.BUY ? OrderType.SELL : OrderType.BUY,
                        newOrder.getOrderType() == OrderType.BUY ? newOrder.getOrderId() : topOrder.getOrderId(),
                        newOrder.getOrderType() == OrderType.BUY ? topOrder.getOrderId() : newOrder.getOrderId(),
                        newOrder.getSymbol(),
                        tradedQuantity,
                        topOrder.getPrice(),
                        System.currentTimeMillis()
                ));

                if (newOrder.getQuantity() > topOrder.getQuantity()) {
                    newOrder.setQuantity(newOrder.getQuantity() - topOrder.getQuantity());
                    oppositeOrders.poll();
                } else if (newOrder.getQuantity() < topOrder.getQuantity()) {
                    topOrder.setQuantity(topOrder.getQuantity() - newOrder.getQuantity());
                    newOrder.setQuantity(0);
                    break;
                } else {
                    oppositeOrders.poll();
                    newOrder.setQuantity(0);
                    break;
                }
            } else {
                break;
            }
        }

        return trades;
    }

    private static boolean canMatch(Order newOrder, Order topOrder) {
        if (newOrder.getOrderType() == OrderType.BUY) {
            return newOrder.getPrice() >= topOrder.getPrice();
        } else {
            return newOrder.getPrice() <= topOrder.getPrice();
        }
    }
}
