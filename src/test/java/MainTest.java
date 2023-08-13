import com.trading.Main;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    public void testHelloWorld() {
        Main main = new Main();
        assert main.helloWorld().equals("Hello world!");
    }
}
