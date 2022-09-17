package order;

import config.BaseClient;
import io.restassured.response.Response;

public class OrderClient extends BaseClient {

    private final String ORDER = "/orders";
    private final String DELETE_ORDER = ORDER + "/cancel";

    public Response createOrder(Order order) {
        return getSpec()
                .body(order)
                .when()
                .post(ORDER);
    }

    public void deleteOrder(OrderTrack orderTrack) {
        getSpec()
                .body(orderTrack)
                .when()
                .put(DELETE_ORDER);
    }

    public Response getOrderList() {
        return getSpec()
                .when().
                get(ORDER);
    }
}
