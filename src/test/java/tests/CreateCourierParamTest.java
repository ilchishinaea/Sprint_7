package tests;

import api.CourierApi;
import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.UUID;
import java.util.stream.Stream;

public class CreateCourierParamTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();
    Courier courier;

    @ParameterizedTest
    @DisplayName("Нельзя создать курьера, если логин или пароль не заполнено")
    @MethodSource("courierData")
    public void createCourierMissingFieldReturn400(Courier courier) {
        this.courier = courier;
        Response response = createCourierStep(courier);
        checkCourierBadRequestStep(response);
    }

    @AfterEach
    public void deleteCourier(){
        deleteCourierIfExistsStep();
    }

    // ----------------- Steps -----------------

    @Step("Отправляем запрос на создание курьера")
    public Response createCourierStep(Courier courier) {
        return courierApi.createCourier(courier);
    }

    @Step("Проверяем, что вернулся код 400 и сообщение о недостаточных данных")
    public void checkCourierBadRequestStep(Response response) {
        courierApi.checkCourierBadRequest(response);
    }

    @Step("Удяляем курьера, если создался")
    public void deleteCourierIfExistsStep() {
        courierApi.deleteCourierIfExists(courier);
    }

    static Stream<Courier> courierData() {
        String randomLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String randomPassword = "pass_" + UUID.randomUUID().toString().substring(0, 8);
        String randomName = "Name_" + UUID.randomUUID().toString().substring(0, 5);

        return Stream.of(
                new Courier(null, randomPassword, randomName),
                new Courier(randomLogin, null, randomName)
        );
    }
}
