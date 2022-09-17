import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Enclosed.class)
public class CourierLoginNegativeTest {

    public static class NotParameterizedCourierLoginNegativeTest {

        Courier courier;
        CourierClient courierClient;
        private int courierId;
        private int statusCode;

        @Before
        public void setUp() {
            courier = Courier.getRandomCourier();
            courierClient = new CourierClient();
            courierClient.createCourier(courier);
        }

        @After
        public void tearDown() {
            if (statusCode == 200) {
                CourierCredentials courierCredentials = CourierCredentials.from(courier);
                courierId = courierClient.loginCourier(courierCredentials)
                        .getBody()
                        .path("id");
                courierClient.deleteCourier(courierId);
            }
        }

        @Test
        @DisplayName("Created courier can't login with incorrect login")
        public void createdCourierCanNotLoginWithIncorrectLogin() {
            String firstLogin = courier.getLogin();
            Courier secondCourier = Courier.getRandomCourierWithExactLogin(firstLogin);
            Response response = courierClient.loginCourier(CourierCredentials.from(secondCourier));
            statusCode = response.getStatusCode();

            assertEquals(404, statusCode);
            assertNotNull("Учетная запись не найдена", response.getBody().path("message"));
        }

        @Test
        @DisplayName("Created courier can't login with incorrect password")
        public void createdCourierCanNotLoginWithIncorrectPassword() {
            String firstPassword = courier.getPassword();
            Courier secondCourier = Courier.getRandomCourierWithExactPassword(firstPassword);
            Response response = courierClient.loginCourier(CourierCredentials.from(secondCourier));
            statusCode = response.getStatusCode();

            assertEquals(404, statusCode);
            assertNotNull("Учетная запись не найдена", response.getBody().path("message"));
        }
    }

    @RunWith(Parameterized.class)
    public static class ParameterizedCourierLoginNegativeTest {

        Courier courier;
        CourierClient courierClient = new CourierClient();
        private int courierId;
        private int expectedStatusCode;
        private String expectedMessage;

        public ParameterizedCourierLoginNegativeTest(Courier courier, int expectedStatusCode, String expectedMessage) {
            this.courier = courier;
            this.expectedStatusCode = expectedStatusCode;
            this.expectedMessage = expectedMessage;
        }

        @Parameterized.Parameters(name = "Курьер = {index}, статус код = {1}, сообщение об ошибке = {2}")
        public static Object[][] getCourierData() {
            return new Object[][]{
                    {Courier.getRandomCourierWithoutLogin(), 400, "Недостаточно данных для входа"},
                    {Courier.getRandomCourierWithoutPassword(), 400, "Недостаточно данных для входа"},
                    {Courier.getRandomCourier(), 404, "Учетная запись не найдена"}
            };
        }

        @After
        public void tearDown() {
            if (courierClient.loginCourier(CourierCredentials.from(courier)).getStatusCode() == 200) {
                CourierCredentials courierCredentials = CourierCredentials.from(courier);
                courierId = courierClient.loginCourier(courierCredentials)
                        .getBody()
                        .path("id");
                courierClient.deleteCourier(courierId);
            }
        }

        @Test
        @DisplayName("Courier can't login with empty field or using non-existent data")
        public void courierWithIncorrectDataCanNotLogin() {
            Response response = courierClient.loginCourier(CourierCredentials.from(courier));

            assertEquals(expectedStatusCode, response.getStatusCode());
            assertEquals(expectedMessage, response.getBody().path("message"));
        }
    }
}
