package api;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class OrderApi {

    @Step("Отправляем запрос на создание заказа: {order}")
    public Response createOrder(Order order) {
        Response response =
                given()
                    .header("Content-type", "application/json")
                    .auth().none()
                    .body(order)
                    .when()
                    .post("/api/v1/orders");
        return response;
    }

    @Step("Отправляем запрос на получение списка заказов")
    public Response getOrders() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().none()
                        .when()
                        .get("/api/v1/orders");
        return response;
    }

    @Step("Проверяем, что заказ создан успешно (код 201, track не null)")
    public void checkOrderCreated(Response response) {
        response.then()
                .assertThat().statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue());
    }

    @Step("Проверяем ответ на список заказов (код 200, orders не null)")
    public void checkOrdersListOk(Response response) {
        response.then()
                .assertThat().statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());
    }
}
