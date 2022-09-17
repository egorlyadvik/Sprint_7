package courier;

import config.BaseClient;
import io.restassured.response.Response;

public class CourierClient extends BaseClient {

    private final String CREATE_COURIER = "/courier";
    private final String LOGIN = CREATE_COURIER + "/login";
    private final String DELETE = CREATE_COURIER + "/{courierId}";

    public Response createCourier(Courier courier) {
        return getSpec()
                .body(courier)
                .when()
                .post(CREATE_COURIER);
    }

    public Response loginCourier(CourierCredentials courierCredentials) {
        return getSpec()
                .body(courierCredentials)
                .when()
                .post(LOGIN);
    }

    public void deleteCourier(int courierId) {
        getSpec()
                .pathParam("courierId", courierId)
                .when()
                .delete(DELETE);
    }
}
