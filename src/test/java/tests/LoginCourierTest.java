package tests;

import api.CourierApi;
import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

public class LoginCourierTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();

    String randomLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
    String randomPassword = "pass_" + UUID.randomUUID().toString().substring(0, 8);

    private String originalLogin = randomLogin;
    private String originalPassword = randomPassword;

    Courier courier = new Courier(originalLogin, originalPassword);

    @Test
    @DisplayName("Ошибка при запросе на несуществующую пару логин-пароль")
    public void nonExistentCourierReturnsStatusCode404AndMessage() {
        Response response = loginCourierStep(courier);
        checkLoginNotFoundStep(response);
    }

    @AfterEach
    public void deleteCourier(){
        deleteCourierIfExistsStep();
    }

    // ----------------- Steps -----------------

    @Step("Не создем курьера, отправляем запрос id логина на курьера: {courier.login}")
    public Response loginCourierStep(Courier courier) {
        return courierApi.loginCourier(courier);
    }

    @Step("Проверяем, что запрос неуспешен (код 404 и сообщение 'Учетная запись не найдена')")
    public void checkLoginNotFoundStep(Response response) {
        courierApi.checkLoginNotFound(response);
    }

    @Step("Удяляем курьера, если создался")
    public void deleteCourierIfExistsStep() {
        courierApi.deleteCourierIfExists(courier);
    }
}
