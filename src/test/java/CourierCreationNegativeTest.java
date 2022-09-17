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

@RunWith(Enclosed.class)
public class CourierCreationNegativeTest {

    public static class NotParameterizedCourierCreationNegativeTest {

        Courier courier;
        CourierClient courierClient;
        private int courierId;

        @Before
        public void setUp() {
            courier = Courier.getRandomCourier();
            courierClient = new CourierClient();
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
        @DisplayName("Two identical couriers can't be created")
        public void twoIdenticalCouriersCanNotBeCreated() {
            courierClient.createCourier(courier);
            Response response = courierClient.createCourier(courier);

            assertEquals(409, response.getStatusCode());
            assertEquals("Этот логин уже используется", response.getBody().path("message"));
        }

        @Test
        @DisplayName("It is impossible to create a courier with login which is already in use")
        public void canNotCreateCourierWithUsedLogin() {
            courierClient.createCourier(courier);
            String firstLogin = courier.getLogin();
            Courier secondCourier = Courier.getRandomCourierWithExactLogin(firstLogin);
            Response response = courierClient.createCourier(secondCourier);

            assertEquals(409, response.getStatusCode());
            assertEquals("Этот логин уже используется", response.getBody().path("message"));
        }
    }

    @RunWith(Parameterized.class)
    public static class ParameterizedCourierCreationNegativeTest {

        Courier courier;
        CourierClient courierClient = new CourierClient();
        private int courierId;
        private int expectedStatusCode;
        private String expectedMessage;

        public ParameterizedCourierCreationNegativeTest(Courier courier, int expectedStatusCode, String expectedMessage) {
            this.courier = courier;
            this.expectedStatusCode = expectedStatusCode;
            this.expectedMessage = expectedMessage;
        }

        @Parameterized.Parameters(name = "Курьер = {index}, статус код = {1}, сообщение об ошибке = {2}")
        public static Object[][] getCourierData() {
            return new Object[][]{
                    {Courier.getRandomCourierWithoutLogin(), 400, "Недостаточно данных для создания учетной записи"},
                    {Courier.getRandomCourierWithoutPassword(), 400, "Недостаточно данных для создания учетной записи"}
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
        @DisplayName("Courier without required fields can't be created")
        public void courierWithoutRequiredDataCanNotBeCreated() {
            Response response = courierClient.createCourier(courier);

            assertEquals(expectedStatusCode, response.getStatusCode());
            assertEquals(expectedMessage, response.getBody().path("message"));
        }
    }
}
