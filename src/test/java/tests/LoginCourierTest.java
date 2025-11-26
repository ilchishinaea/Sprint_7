package tests;

import api.CourierApi;
import base.BaseTest;
import io.restassured.response.Response;
import models.Courier;
import org.junit.jupiter.api.*;
import static utils.CourierFactory.*;

public class LoginCourierTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();

    Courier courier = randomCourier();

    @Test
    @DisplayName("Ошибка при запросе на несуществующую пару логин-пароль")
    public void nonExistentCourierReturnsStatusCode404AndMessage() {
        Response response = courierApi.loginCourier(courier);
        courierApi.checkLoginNotFound(response);
    }

    @AfterEach
    public void deleteCourierIfExists(){
        courierApi.deleteCourierIfExists(courier);
    }
}
