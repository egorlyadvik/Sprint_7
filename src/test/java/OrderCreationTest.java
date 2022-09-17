import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderClient;
import order.OrderTrack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    Order order;
    OrderClient orderClient;
    private int track;
    private String[] color;
    private int expectedStatusCode;

    public OrderCreationTest(String[] color, int expectedStatusCode) {
        this.color = color;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters(name = "Заказ = {index}, статус код = {1}")
    public static Object[][] getOrderData() {
        return new Object[][]{
                {new String[]{"BLACK"}, 201},
                {new String[]{"GREY"}, 201},
                {new String[]{"BLACK", "GREY"}, 201},
                {null, 201}
        };
    }

    @Before
    public void setUp() {
        order = Order.getRandomOrder(color);
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        OrderTrack orderTrack = new OrderTrack(track);
        orderClient.deleteOrder(orderTrack);
    }

    @Test
    @DisplayName("Order can be created with different colors. Response status 201 and body contains track")
    public void orderCreatesWithCorrectStatusCodeAndTrack() {
        Response response = orderClient.createOrder(order);
        track = response.getBody().path("track");

        assertEquals(expectedStatusCode, response.getStatusCode());
        assertNotNull(track);
    }
}
