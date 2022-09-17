import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CourierCreationPositiveTest {

    Courier courier;
    CourierClient courierClient = new CourierClient();
    private int courierId;
    private int expectedStatusCode;
    private boolean expectedOkFlag;

    public CourierCreationPositiveTest(Courier courier, int expectedStatusCode, boolean expectedOkFlag) {
        this.courier = courier;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedOkFlag = expectedOkFlag;
    }

    @Parameterized.Parameters(name = "Курьер = {index}, статус код = {1}, наличие параметра ok = {2}")
    public static Object[][] getCourierData() {
        return new Object[][]{
                {Courier.getRandomCourier(), 201, true},
                {Courier.getRandomCourierWithOnlyRequiredFields(), 201, true}
        };
    }

    @After
    public void tearDown() {
        if (!(courier.getLogin().equals("")) && !(courier.getPassword().equals(""))) {
            CourierCredentials courierCredentials = CourierCredentials.from(courier);
            courierId = courierClient.loginCourier(courierCredentials)
                    .getBody()
                    .path("id");
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Courier can be created with all fields and with only required fields. Response status 201 and body is ok: true")
    public void courierCanBeCreated() {
        Response response = courierClient.createCourier(courier);

        assertEquals(expectedStatusCode, response.getStatusCode());
        assertEquals(expectedOkFlag, response.getBody().path("ok"));
    }
}
