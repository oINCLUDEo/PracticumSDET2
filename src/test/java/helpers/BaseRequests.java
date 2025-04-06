package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BaseRequests {

    private static final String BASE_URI = "http://localhost:8080/api";

    /**
     * Инициализирует спецификацию запросов с базовыми настройками.
     *
     * @return RequestSpecification с настроенными параметрами.
     */
    public static RequestSpecification initRequestSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .setAccept(ContentType.JSON)
                .log(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails().logDetailOfRequestAndResponseIfValidationFails())
                .build();
    }

    /**
     * Выполняет GET-запрос к указанному эндпоинту.
     *
     * @param endpoint Эндпоинт для выполнения запроса.
     * @return Ответ от сервера.
     */
    public static Response get(String endpoint) {
        return given()
                .spec(initRequestSpecification())
                .log().all()
                .when()
                .get(endpoint);
    }

    /**
     * Выполняет POST-запрос к указанному эндпоинту с переданным телом.
     *
     * @param endpoint Эндпоинт для выполнения запроса.
     * @param body     Тело запроса.
     * @return Ответ от сервера.
     */
    public static Response post(String endpoint, Object body) {
        return given()
                .spec(initRequestSpecification())
                .log().all()
                .body(body)
                .when()
                .post(endpoint);
    }

    /**
     * Выполняет PATCH-запрос к указанному эндпоинту с переданным телом.
     *
     * @param endpoint Эндпоинт для выполнения запроса.
     * @param body     Тело запроса.
     * @return Ответ от сервера.
     */
    public static Response patch(String endpoint, Object body) {
        return given()
                .spec(initRequestSpecification())
                .log().all()
                .body(body)
                .when()
                .patch(endpoint);
    }

    /**
     * Выполняет DELETE-запрос к указанному эндпоинту.
     *
     * @param endpoint Эндпоинт для выполнения запроса.
     * @return Ответ от сервера.
     */
    public static Response delete(String endpoint) {
        return given()
                .spec(initRequestSpecification())
                .log().all()
                .when()
                .delete(endpoint);
    }
}
