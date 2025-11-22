package api;
import io.restassured.response.Response;
import models.Order;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class OrderApi {

    //создание заказа
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

    //вернуть список заказов
    public Response getOrders() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().none()
                        .when()
                        .get("/api/v1/orders");
        return response;
    }

    //проверка успешного создания заказа
    public void checkOrderCreated(Response response) {
        response.then()
                .assertThat().statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue());
    }

    //проверка, что список заказов не пустой
    public void checkOrdersListOk(Response response) {
        response.then()
                .assertThat().statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());
    }
}
