package tests;

import api.OrderApi;
import base.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrdersListTest extends BaseTest {

    private final OrderApi orderApi = new OrderApi();

    @Test
    @DisplayName("Чтение списка заказов")
    void orderListReturnsStatusCode200AndOrders() {
        Response response = orderApi.getOrders();
        orderApi.checkOrdersListOk(response);
    }

}
