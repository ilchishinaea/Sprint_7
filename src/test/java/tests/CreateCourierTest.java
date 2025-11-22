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

public class CreateCourierTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();

    String randomLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
    String randomPassword = "pass_" + UUID.randomUUID().toString().substring(0, 8);
    String randomName = "Name_" + UUID.randomUUID().toString().substring(0, 5);

    private String originalLogin = randomLogin;
    private String originalPassword = randomPassword;
    private String originalName = randomName;

    Courier courier;
    Courier courier2;

    @BeforeEach
    public void setUpCourier() {
        courier = new Courier(originalLogin, originalPassword, originalName);
        courier2 = new Courier(courier.getLogin(), originalPassword, originalName);
    }

    @Test
    @DisplayName("Создаем курьера")
    public void createCourierWithValidLoginPasswordAndNameReturn201AndTrue(){
        Response response = createCourierStep(courier);
        checkCourierCreatedStep(response);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void duplicateCourierReturns409() {
        Response response = createCourierStep(courier);
        checkCourierCreatedStep(response);
        Response response2 = createCourierStep(courier);
        checkCourierConflictStep(response2);
    }

    @Test
    @DisplayName("Нельзя создать курьера с логином, который уже есть")
    public void createCourierWithExistingLoginReturn409() {
        Response response = createCourierStep(courier);
        checkCourierCreatedStep(response);
        Response response2 = createCourierStep(courier2);
        checkCourierConflictStep(response2);
    }

    @AfterEach
    public void deleteCourier() {
        deleteCourierIfExistsStep();
    }

    // ----------------- Steps -----------------

    @Step("Отправляем запрос на создание курьера с логином {courier.login}")
    public Response createCourierStep(Courier courier) {
        return courierApi.createCourier(courier);
    }

    @Step("Проверяем, что курьер создался, код ответа: {statusCode}")
    public void checkCourierCreatedStep(Response response) {
        courierApi.checkCourierCreated(response);
    }

    @Step("Проевряем что курьер не создается, если запрос повторяется или логин уже зарегистирован, код ответа: {statusCode}")
    public void checkCourierConflictStep(Response response) {
        courierApi.checkCourierConflict(response);
    }

    @Step("Удяляем курьеров, если создались")
    public void deleteCourierIfExistsStep() {
        courierApi.deleteCourierIfExists(courier);
        courierApi.deleteCourierIfExists(courier2);
    }

}
