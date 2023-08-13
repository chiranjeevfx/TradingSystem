package trading.services;

import com.trading.models.Order;
import com.trading.models.OrderType;
import com.trading.models.User;
import com.trading.services.TradeSystem;
import org.junit.jupiter.api.Test;
public class TradeSystemTest {
    @Test
    public void testOrderPlacement() {
        TradeSystem system = new TradeSystem();
        User user = new User("1", "Alice", "1234567890", "alice@email.com");
        system.addUser(user);

        Order order = new Order("1", "1", OrderType.BUY, "RELIANCE", 10, 100.0);
        system.placeOrder(order);

        assert system.getOrders().get("1").equals(order);
        assert system.getOrderBooks().get("RELIANCE").getBuyOrders().peek().equals(order);
    }

    // More tests...
}

