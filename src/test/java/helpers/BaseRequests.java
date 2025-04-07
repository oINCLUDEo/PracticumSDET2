package helpers;

import io.qameta.allure.Attachment;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static io.restassured.RestAssured.given;

public class BaseRequests {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRequests.class);
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
                .build();
    }

    /**
     * Выполняет GET-запрос к указанному эндпоинту.
     *
     * @param endpoint Эндпоинт для выполнения запроса.
     * @return Ответ от сервера.
     */
    @Step("Выполнение GET-запроса по эндпоинту {endpoint}")
    public static Response get(String endpoint) {
        LOGGER.info("Отправка GET-запроса на эндпоинт: {}", endpoint);
        Response response = given()
                .spec(initRequestSpecification())
                .get(endpoint);

        attachResponse(response);  // Добавление тела ответа в Allure
        return response;
    }

    /**
     * Выполняет POST-запрос к указанному эндпоинту с переданным телом.
     *
     * @param endpoint Эндпоинт для выполнения запроса.
     * @param body     Тело запроса.
     * @return Ответ от сервера.
     */
    @Step("Выполнение POST-запроса на эндпоинт {endpoint} с телом {body}")
    public static Response post(String endpoint, Object body) {
        LOGGER.info("Отправка POST-запроса на эндпоинт: {} с телом: {}", endpoint, body);
        Response response = given()
                .spec(initRequestSpecification())
                .body(body)
                .post(endpoint);

        attachResponse(response);  // Добавление тела ответа в Allure
        return response;
    }

    /**
     * Выполняет PATCH-запрос к указанному эндпоинту с переданным телом.
     *
     * @param endpoint Эндпоинт для выполнения запроса.
     * @param body     Тело запроса.
     * @return Ответ от сервера.
     */
    @Step("Выполнение PATCH-запроса на эндпоинт {endpoint} с телом {body}")
    public static Response patch(String endpoint, Object body) {
        LOGGER.info("Отправка PATCH-запроса на эндпоинт: {} с телом: {}", endpoint, body);
        Response response = given()
                .spec(initRequestSpecification())
                .body(body)
                .patch(endpoint);

        attachResponse(response);
        return response;
    }

    /**
     * Выполняет DELETE-запрос к указанному эндпоинту.
     *
     * @param endpoint Эндпоинт для выполнения запроса.
     * @return Ответ от сервера.
     */
    @Step("Выполнение DELETE-запроса по эндпоинту {endpoint}")
    public static Response delete(String endpoint) {
        LOGGER.info("Отправка DELETE-запроса на эндпоинт: {}", endpoint);
        Response response = given()
                .spec(initRequestSpecification())
                .delete(endpoint);

        attachResponse(response);
        return response;
    }

    @Attachment(value = "Ответ от сервера", type = "application/json")
    public static String attachResponse(Response response) {
        return response.asString();
    }
}
