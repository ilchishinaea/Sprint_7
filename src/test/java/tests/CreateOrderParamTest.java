package tests;

import api.OrderApi;
import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

public class CreateOrderParamTest extends BaseTest {

    private final OrderApi orderApi = new OrderApi();

    private static final Random random = new Random();

    @ParameterizedTest
    @DisplayName("Создание заказа")
    @MethodSource("orderData")
    public void createOrderReturn201(Order order) {
        Response response = createOrderStep(order);
        checkOrderCreatedStep(response);
    }

    // ----------------- Steps -----------------

    @Step("Отправляем запрос на создание заказа с данными: {order}")
    public Response createOrderStep(Order order) {
        return orderApi.createOrder(order);
    }

    @Step("Проверяем, что заказ создан успешно (код 201 и трек не null)")
    public void checkOrderCreatedStep(Response response) {
        orderApi.checkOrderCreated(response);
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
