import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierWithCreateCourierTest {

    String randomLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
    String randomPassword = "pass_" + UUID.randomUUID().toString().substring(0, 8);

    private String originalLogin = randomLogin;
    private String originalPassword = randomPassword;

    Courier courier = new Courier(originalLogin, originalPassword);

    private Integer courierId;

    @BeforeEach
    public void setUpAndCreateCourier() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        createCourierStep(courier);
    }

    @Test
    @DisplayName("Авторизация валидного курьера")
    void authValidCourierReturnsStatusCode200AndId() {
        Response response = loginCourierStep(courier);
        verifyLoginSuccessStep(response);
        courierId = response.then().extract().path("id");
    }

    @Test
    @DisplayName("Ошибка на неверный логин")
    void invalidLoginReturnsStatusCode404AndMessage() {
        courier.setLogin("inValid125");
        Response response = loginCourierStep(courier);
        verifyLoginFailedStep(response, "Учетная запись не найдена", 404);
    }

    @Test
    @DisplayName("Ошибка на неверный пароль")
    void invalidPasswordReturnsStatusCode404AndMessage() {
        courier.setPassword("in_Valid125");
        Response response = loginCourierStep(courier);
        verifyLoginFailedStep(response, "Учетная запись не найдена", 404);
    }

    @Test
    @DisplayName("Ошибка на отсутствующий логин")
    public void noLoginReturnsStatusCode400AndMessage() {
        courier.setLogin(null);
        Response response = loginCourierStep(courier);
        verifyLoginFailedStep(response, "Недостаточно данных для входа", 400);
    }

    @Test
    @DisplayName("Ошибка на отсутствующий пароль")
    public void noPasswordReturnsStatusCode400AndMessage() {
        courier.setPassword(null);
        Response response = loginCourierStep(courier);
        verifyLoginFailedStep(response, "Недостаточно данных для входа", 400);
    }

    @AfterEach
    public void deleteCourier() {
        if (courierId == null) {
            Response loginResponse = loginCourierStep(courier);
            if (loginResponse.statusCode() == 200) {
                courierId = loginResponse.then().extract().path("id");
            }
        }
        if (courierId != null) {
            deleteCourierStep(courierId);
        }
        courierId = null;
    }

    @Step("Создаем курьера с логином: {courier.login}")
    public void createCourierStep(Courier courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().none()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Step("Отправляем запрос на курьера с логином: {courier.login} и паролем: {courier.password}")
    public Response loginCourierStep(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Проверяем успешный логин курьера (код 200 и наличие id)")
    public void verifyLoginSuccessStep(Response response) {
        response.then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Проверяем, что логин неуспешен (код {statusCode} и сообщение '{expectedMessage}')")
    public void verifyLoginFailedStep(Response response, String expectedMessage, int statusCode) {
        response.then().assertThat()
                .body("message", equalTo(expectedMessage))
                .and()
                .statusCode(statusCode);
    }

    @Step("Удаляем курьера с id: {courierId}")
    public void deleteCourierStep(Integer courierId) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + courierId)
                .then()
                .statusCode(200);
    }
}
