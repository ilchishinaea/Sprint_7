import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {

    String randomLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
    String randomPassword = "pass_" + UUID.randomUUID().toString().substring(0, 8);
    String randomName = "Name_" + UUID.randomUUID().toString().substring(0, 5);

    private String originalLogin = randomLogin;
    private String originalPassword = randomPassword;

    Courier courier = new Courier(originalLogin, originalPassword, randomName);
    private Integer courierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Создание курьера")
    public void createCourier_WithValidLoginPasswordAndName_Return201AndTrue(){
        createCourierStep(courier);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void duplicateCourierReturns409() {
        createCourierStep(courier);
        createDuplicateCourierStep(courier);
    }

    @Test
    @DisplayName("Нельзя создать курьера с логином, который уже есть")
    public void createCourierWithExistingLoginReturn409() {
        createCourierStep(courier);
        Courier courier2 = new Courier(courier.getLogin(), randomPassword, randomName);
        createCourierWithExistingLoginStep(courier2);
    }

    @AfterEach
    public void deleteCourier() {
        if (courierId == null) {
            Response loginResponse = loginCourierStep(courier);
            if (loginResponse.statusCode() == 200) {
                courierId = loginResponse.then().extract().path("id");
                deleteCourierStep(courierId);
            }
        } else {
            deleteCourierStep(courierId);
        }
        courierId = null;
    }

    // ----------------- Steps -----------------

    @Step("Создаём курьера с логином {courier.login}")
    public void createCourierStep(Courier courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().none()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(201);
    }

    @Step("Пытаемся создать дубликат курьера с логином {courier.login}")
    public void createDuplicateCourierStep(Courier courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().none()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"))
                .and().statusCode(409);
    }

    @Step("Пытаемся создать курьера с существующим логином {courier.login}")
    public void createCourierWithExistingLoginStep(Courier courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().none()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"))
                .and().statusCode(409);
    }

    @Step("Логиним курьера с логином {courier.login}")
    public Response loginCourierStep(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удаляем курьера с id {id}")
    public void deleteCourierStep(int id) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + id)
                .then()
                .statusCode(200);
    }
}
