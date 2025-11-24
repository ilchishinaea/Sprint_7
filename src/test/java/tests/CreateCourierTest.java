package tests;

import api.CourierApi;
import base.BaseTest;
import io.restassured.response.Response;
import models.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static utils.CourierFactory.*;

public class CreateCourierTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();

    Courier courier;
    Courier courierWithExistingLogin;
    Courier courierMissingLogin;
    Courier courierMissingPassword;

    @BeforeEach
    public void generateCourier() {
        courier = randomCourier();
    }

    @Test
    @DisplayName("Создаем курьера")
    public void createCourierWithValidLoginPasswordAndNameReturn201AndTrue(){
        Response response = courierApi.createCourier(courier);
        courierApi.checkCourierCreated(response);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void duplicateCourierReturns409() {
        Response response = courierApi.createCourier(courier);
        courierApi.checkCourierCreated(response);
        Response response2 = courierApi.createCourier(courier);
        courierApi.checkCourierConflict(response2);
    }

    @Test
    @DisplayName("Нельзя создать курьера с логином, который уже есть")
    public void createCourierWithExistingLoginReturn409() {
        Response response = courierApi.createCourier(courier);
        courierApi.checkCourierCreated(response);
        courierWithExistingLogin = courierWithExistingLogin(courier);
        Response response2 = courierApi.createCourier(courierWithExistingLogin);
        courierApi.checkCourierConflict(response2);
    }

    @Test
    @DisplayName("Нельзя создать курьера, если логин не заполнен")
    public void createCourierMissingLoginReturn400() {
        courierMissingLogin = courierWithMissingLogin();
        Response response = courierApi.createCourier(courierMissingLogin);
        courierApi.checkCourierBadRequest(response);
    }

    @Test
    @DisplayName("Нельзя создать курьера, если пароль не заполнен")
    public void createCourierMissingPasswordReturn400() {
        courierMissingPassword = courierWithMissingPassword();
        Response response = courierApi.createCourier(courierMissingPassword);
        courierApi.checkCourierBadRequest(response);
    }

    @AfterEach
    public void deleteCourierIfExists() {
        courierApi.deleteCourierIfExists(courierMissingPassword);
        courierApi.deleteCourierIfExists(courierMissingLogin);
        courierApi.deleteCourierIfExists(courierWithExistingLogin);
        courierApi.deleteCourierIfExists(courier);
    }

}
