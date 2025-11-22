package api;
import models.Courier;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierApi {

    //создание курьера
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

    //логиним курьера, чтобы узнать его id
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

    //удаление курьера
    public Response deleteCourier (int id){
        Response response =
                given()
                .header("Content-type", "application/json")
                .auth().none()
                .when()
                .delete("/api/v1/courier/" + id);
        return response;
    }

    //удаление курьера, если существует
    public void deleteCourierIfExists(Courier c) {

        Response loginResponse = loginCourier(c);
        int status = loginResponse.statusCode();
        int courierId;

        if (status == 200) {
            courierId = loginResponse.then().extract().path("id");
            deleteCourier(courierId);
        } else if (status == 404) {
            System.out.println("Учетная запись не найдена");
        } else if (status == 400) {
            System.out.println("Недостаточно данных для входа");
        } else {
            System.out.println("Неожиданный код ответа при попытке авторизации курьера: " + status);
        }
    }

    //проверка, что курьер создался
    public void checkCourierCreated (Response response){
        response.then()
                .assertThat().statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

    //проверка ошибки на повторяющийся запрос для создания курьера
    public Response checkCourierConflict(Response response){
        response.then()
                .assertThat().statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
        return response;
    }

    //проверка ошибки на неполный запрос для создания курьера
    public void checkCourierBadRequest (Response response){
        response.then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //проверяем, что в ответе есть id логина
    public void checkLoginOk(Response response) {
        response.then()
                .assertThat().statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

    }

    //проверяем ошибку на недостаточность данных запроса для логина
    public void checkLoginBadRequest(Response response) {
        response.then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    //проверяем ошибку на несуществующую пару
    public void checkLoginNotFound(Response response) {
        response.then()
                .assertThat().statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    //проверяем, что курьер удален
    public void chekDeleteCourierOk (Response response){
        response.then()
                .statusCode(200);
    }

}
