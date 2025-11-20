import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Чтение списка заказов")
    void orderListReturnsStatusCode200AndOrders() {
        Response response = getOrdersStep();
        verifyOrdersListStep(response);
    }

    @Step("Отправляем запрос на получение списка заказов")
    public Response getOrdersStep() {
        return given()
                .header("Content-type", "application/json")
                .auth().none()
                .when()
                .get("/api/v1/orders");
    }

    @Step("Проверяем, что список заказов не пустой и код ответа 200")
    public void verifyOrdersListStep(Response response) {
        response.then().assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }
}
