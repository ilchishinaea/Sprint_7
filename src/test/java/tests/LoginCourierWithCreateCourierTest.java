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

public class LoginCourierWithCreateCourierTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();

    Courier courier;
    Courier courierWithInvalidLogin;
    Courier courierWithInvalidPassword;
    Courier courierWithNullLogin;
    Courier courierWithNullPassword;

    @BeforeEach
    public void setUpCourier() {
        courier = randomCourier();
        Response response = courierApi.createCourier(courier);
        courierApi.checkCourierCreated(response);
    }

    @Test
    @DisplayName("Авторизация валидного курьера")
    void authValidCourierReturnsStatusCode200AndId() {
        Response response = courierApi.loginCourier(courier);
        courierApi.checkLoginOk(response);
    }

    @Test
    @DisplayName("Ошибка на неверный логин")
    void invalidLoginReturnsStatusCode404AndMessage() {
        courierWithInvalidLogin = validCourierWithInvalidLogin(courier);
        Response response = courierApi.loginCourier(courierWithInvalidLogin);
        courierApi.checkLoginNotFound(response);
    }

    @Test
    @DisplayName("Ошибка на неверный пароль")
    void invalidPasswordReturnsStatusCode404AndMessage() {
        courierWithInvalidPassword = validCourierWithInvalidPassword(courier);
        Response response = courierApi.loginCourier(courierWithInvalidPassword);
        courierApi.checkLoginNotFound(response);
    }

    @Test
    @DisplayName("Ошибка на отсутствующий логин")
    public void noLoginReturnsStatusCode400AndMessage() {
        courierWithNullLogin = validCourierWithNullLogin(courier);
        Response response = courierApi.loginCourier(courierWithNullLogin);
        courierApi.checkLoginBadRequest(response);
    }

    @Test
    @DisplayName("Ошибка на отсутствующий пароль")
    public void noPasswordReturnsStatusCode400AndMessage() {
        courierWithNullPassword = validCourierWithNullPassword(courier);
        Response response = courierApi.loginCourier(courierWithNullPassword);
        courierApi.checkLoginBadRequest(response);
    }

    @AfterEach
    public void deleteCourierIfExistsStep(){
        courierApi.deleteCourierIfExists(courierWithInvalidLogin);
        courierApi.deleteCourierIfExists(courierWithInvalidPassword);
        courierApi.deleteCourierIfExists(courierWithNullLogin);
        courierApi.deleteCourierIfExists(courierWithNullPassword);
        courierApi.deleteCourierIfExists(courier);
    }
}
