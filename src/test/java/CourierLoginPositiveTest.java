import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierLoginPositiveTest {

    Courier courier;
    CourierClient courierClient;
    private Integer courierId;

    @Before
    public void setUp() {
        courier = Courier.getRandomCourier();
        courierClient = new CourierClient();
        courierClient.createCourier(courier);
    }

    @After
    public void tearDown() {
        if (!courierId.equals(null)) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Courier can login. Response status 200 and there is id in body")
    public void courierCanLogin() {
        Response response = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = response.getBody().path("id");

        assertEquals(200, response.getStatusCode());
        assertNotNull(courierId);
    }
}
