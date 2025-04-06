package tests;

import dto.Entity;
import helpers.DataGenerator;
import helpers.BaseRequests;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

@Epic("Тестирование API")
@Feature("Взаимодействие с Entity")
public class APITests {

    @Story("Создание Entity")
    @Description("Тест создания нового Entity со сгенерированными данными")
    @Test
    public void createEntityTest() {
        Entity entity = DataGenerator.generateRandomEntity();

        BaseRequests.post("/create", entity)
                .then()
                .log().all()
                .statusCode(200)
                .body(notNullValue());
    }

    @Story("Получение Entity по ID")
    @Description("Тест получения Entity по его ID")
    @Test
    public void getEntityTest() {
        BaseRequests.get("/get/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("title", notNullValue());
    }

    @Story("Получение всех Entities")
    @Description("Тест получения всех Entities")
    @Test
    public void getAllEntitiesTest() {
        BaseRequests.get("/getAll")
                .then()
                .log().all()
                .statusCode(200)
                .body("entity", notNullValue())
                .body("entity.size()", greaterThan(0));
    }

    @Story("Изменение Entity")
    @Description("Тест изменения существующего Entity")
    @Test
    public void updateEntityTest() {
        Entity entity = DataGenerator.generateRandomEntity();

        BaseRequests.patch("/patch/1", entity)
                .then()
                .statusCode(204);
    }

    @Story("Удаление Entity")
    @Description("Тест удаления Entity по его ID")
    @Test
    public void deleteEntityTest() {
        BaseRequests.delete("/delete/8")
                .then()
                .log().all()
                .statusCode(204);
    }
}
