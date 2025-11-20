import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {

    String randomLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
    String randomPassword = "pass_" + UUID.randomUUID().toString().substring(0, 8);

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Ошибка при запросе на несуществующую пару логин-пароль")
    void nonExistentCourierReturnsStatusCode404AndMessage() {
        Courier courier = new Courier(randomLogin, randomPassword);

        Response response = sendLoginRequestStep(courier);
        verifyLogPasFailedStep(response);
    }

    @Step("Отправляем запрос на курьера с логином: {courier.login} и паролем: {courier.password}")
    public Response sendLoginRequestStep(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Проверяем, что запрос неуспешен (код 404 и сообщение 'Учетная запись не найдена')")
    public void verifyLogPasFailedStep(Response response) {
        response.then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
}
