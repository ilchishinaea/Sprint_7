package tests;

import api.CourierApi;
import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

public class LoginCourierWithCreateCourierTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();

    String randomLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
    String randomPassword = "pass_" + UUID.randomUUID().toString().substring(0, 8);

    String randomInvalidLogin = "userInvalid_" + UUID.randomUUID().toString().substring(0, 8);
    String randomInvalidPassword = "passInvalid_" + UUID.randomUUID().toString().substring(0, 8);

    private String originalLogin = randomLogin;
    private String originalPassword = randomPassword;

    Courier courier;

    @BeforeEach
    public void setUpCourier() {
        courier = new Courier(originalLogin, originalPassword);
    }

    @Test
    @DisplayName("Авторизация валидного курьера")
    void authValidCourierReturnsStatusCode200AndId() {
        createCourierStep(courier);
        Response response = loginCourierStep(courier);
        checkLoginOkStep(response);
    }

    @Test
    @DisplayName("Ошибка на неверный логин")
    void invalidLoginReturnsStatusCode404AndMessage() {
        createCourierStep(courier);
        courier.setLogin(randomInvalidLogin);
        Response response = loginCourierStep(courier);
        checkLoginNotFoundStep(response);
    }

    @Test
    @DisplayName("Ошибка на неверный пароль")
    void invalidPasswordReturnsStatusCode404AndMessage() {
        createCourierStep(courier);
        courier.setPassword(randomInvalidPassword);
        Response response = loginCourierStep(courier);
        checkLoginNotFoundStep(response);
    }

    @Test
    @DisplayName("Ошибка на отсутствующий логин")
    public void noLoginReturnsStatusCode400AndMessage() {
        createCourierStep(courier);
        courier.setLogin(null);
        Response response = loginCourierStep(courier);
        checkLoginBadRequestStep(response);
    }

    @Test
    @DisplayName("Ошибка на отсутствующий пароль")
    public void noPasswordReturnsStatusCode400AndMessage() {
        createCourierStep(courier);
        courier.setPassword(null);
        Response response = loginCourierStep(courier);
        checkLoginBadRequestStep(response);
    }

    @AfterEach
    public void deleteCourier(){
        deleteCourierIfExistsStep();
    }

    // ----------------- Steps -----------------

    @Step("Создаем курьера: {courier.login}")
    public void createCourierStep(Courier courier) {
        Response response = courierApi.createCourier(courier);
        courierApi.checkCourierCreated(response);
    }

    @Step("Отправляем запрос на id логина: {courier.login}")
    public Response loginCourierStep(Courier courier) {
        Response response = courierApi.loginCourier(courier);
        return response;
    }

    @Step("Проверяем успешный логин курьера (код 200 и наличие id)")
    public void checkLoginOkStep(Response responseLogin) {
        courierApi.checkLoginOk(responseLogin);
    }

    @Step("Проверяем, что логин неуспешен без логина или пароля (код 400 и сообщение 'Недостаточно данных для входа')")
    public void checkLoginBadRequestStep(Response responseLogin) {
        courierApi.checkLoginBadRequest(responseLogin);
    }

    @Step("Проверяем, что логин неуспешен если в логине или пароле ошибка (код 404 и сообщение 'Учетная запись не найдена')")
    public void checkLoginNotFoundStep(Response responseLogin) {
        courierApi.checkLoginNotFound(responseLogin);
    }

    @Step("Удяляем курьера, если создался")
    public void deleteCourierIfExistsStep() {
        courierApi.deleteCourierIfExists(courier);
    }
}
