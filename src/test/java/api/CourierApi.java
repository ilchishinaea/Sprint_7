package api;
import io.qameta.allure.Step;
import models.Courier;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierApi {

    @Step("Отправляем запрос на создание курьера: {courier.login}, {courier.password}, {courier.firstName}")
    public Response createCourier(Courier courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().none()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Отправляем запрос на получение id логина курьера: {courier.login}")
    public Response loginCourier(Courier courier) {
        Response response =
                given()
                .header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Удяляем курьера: {id}")
    public Response deleteCourier (int id){
        Response response =
                given()
                .header("Content-type", "application/json")
                .auth().none()
                .when()
                .delete("/api/v1/courier/" + id);
        return response;
    }

    @Step("Удяляем курьера, если создался: {c}")
    public void deleteCourierIfExists(Courier c) {
        if (c == null) {
            System.out.println("Курьер не передан (null), пропускаем удаление");
            return;
        }

        Response loginResponse = loginCourier(c);
        int status = loginResponse.statusCode();

        if (status == 200) {
            int courierId = loginResponse.then().extract().path("id");
            deleteCourier(courierId);
            System.out.println("Курьер удален");
        } else {
            System.out.println("Курьер не найден или невозможно удалить (код: " + status + ")");
        }
    }

    @Step("Проверяем, что курьер создался (код 201, содержит true)")
    public void checkCourierCreated (Response response){
        response.then()
                .assertThat().statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

    @Step("Проевряем, что курьер не создается, если запрос повторяется или логин уже зарегистирован (код 409, сообщение 'Этот логин уже используется')")
    public Response checkCourierConflict(Response response){
        response.then()
                .assertThat().statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
        return response;
    }

    @Step("Проверяем, что курьер не создается, если нет логина или пароля (код 400, сообщение 'Недостаточно данных для создания учетной записи')")
    public void checkCourierBadRequest (Response response){
        response.then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Проверяем успешный логин курьера (код 200, id не null)")
    public void checkLoginOk(Response response) {
        response.then()
                .assertThat().statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

    }

    @Step("Проверяем ошибку получения id логина если нет логина или пароля (код 400, сообщение 'Недостаточно данных для входа')")
    public void checkLoginBadRequest(Response response) {
        response.then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Проверяем ошибку получения id логина если в логине или пароле ошибка или пара не существует (код 404, сообщение 'Учетная запись не найдена')")
    public void checkLoginNotFound(Response response) {
        response.then()
                .assertThat().statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Проверяем, что курьер удален (код 200)")
    public void chekDeleteCourierOk (Response response){
        response.then()
                .statusCode(200);
    }

}
