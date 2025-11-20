import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierParamTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @ParameterizedTest
    @DisplayName("Нельзя создать курьера, если логин или пароль не заполнено")
    @MethodSource("courierData")
    public void createCourierMissingFieldReturn400(Courier courier) {
        sendCreateCourierRequestStep(courier)
                .then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }

    @Step("Отправляем запрос на создание курьера")
    public Response sendCreateCourierRequestStep(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .when()
                .post("/api/v1/courier");
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
