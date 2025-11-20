import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderParamTest {

    private static final Random random = new Random();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @ParameterizedTest
    @DisplayName("Создание заказа")
    @MethodSource("orderData")
    public void createOrderReturn201(Order order) {
        Response response = sendCreateOrderRequestStep(order);
        verifyOrderCreatedStep(response);
    }

    @Step("Отправляем запрос на создание заказа с данными: {order}")
    public Response sendCreateOrderRequestStep(Order order) {
        return given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Проверяем, что заказ создан успешно (код 201 и трек не null)")
    public void verifyOrderCreatedStep(Response response) {
        response.then().assertThat().body("track", notNullValue())
                .and().statusCode(201);
    }

    static Stream<Order> orderData() {
        return Stream.of(
                new String[]{"BLACK"},
                new String[]{"GREY"},
                new String[]{"BLACK", "GREY"},
                new String[]{},
                null
        ).map(colors -> new Order(
                randomString("Name"),
                randomString("Last"),
                randomString("Street "),
                String.valueOf(random.nextInt(10) + 1), // metro
                randomPhone(),
                String.valueOf(random.nextInt(10) + 1), // rentTime
                LocalDate.now().plusDays(random.nextInt(5)).toString(),
                randomString("Comment"),
                colors
        ));
    }

    private static String randomString(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 5);
    }

    private static String randomPhone() {
        return "+7999" + (100000 + random.nextInt(900000));
    }
}
