import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.OrderClient;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OrderListTest {

    OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Order list contains orders")
    public void orderListContainsOrdersAndCorrectStatusCode() {
        Response response = orderClient.getOrderList();
        List<Object> orders = response.jsonPath().getList("orders");

        assertEquals(200, response.getStatusCode());
        assertFalse(orders.isEmpty());
    }
}
