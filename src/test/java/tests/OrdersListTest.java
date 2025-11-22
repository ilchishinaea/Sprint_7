package tests;

import api.OrderApi;
import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrdersListTest extends BaseTest {

    private final OrderApi orderApi = new OrderApi();

    @Test
    @DisplayName("Чтение списка заказов")
    void orderListReturnsStatusCode200AndOrders() {
        Response response = getOrdersStep();
        checkOrdersListOkStep(response);
    }

    // ----------------- Steps -----------------

    @Step("Отправляем запрос на получение списка заказов")
    public Response getOrdersStep() {
        return orderApi.getOrders();
    }

    @Step("Проверяем, что список заказов не пустой и код ответа 200")
    public void checkOrdersListOkStep(Response response) {
        orderApi.checkOrdersListOk(response);
    }
}
