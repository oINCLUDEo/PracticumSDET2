package tests;

import dto.EntitiesWrapper;
import dto.Entity;
import helpers.EntityHelper;
import helpers.BaseRequests;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

@Epic("Тестирование API")
@Feature("Взаимодействие с Entity")
public class APITests {
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        EntityHelper.cleanup();
    }

    @AfterSuite
    public void suiteCleanup() {
        EntityHelper.releaseResources();
    }

    @Story("Создание Entity")
    @Description("Тест создания нового Entity со сгенерированными данными")
    @Test
    public void createEntityTest() {
        String entityId = EntityHelper.createEntity();

        Entity createdEntity = BaseRequests.get("/get/" + entityId)
                .then()
                .statusCode(200)
                .extract()
                .as(Entity.class);
        EntityHelper.verifyEntityCreated(createdEntity, entityId);  // Шаг для проверки созданной сущности
    }

    @Story("Изменение Entity")
    @Description("Тест изменения существующего Entity")
    @Test
    public void updateEntityTest() {
        String entityId = EntityHelper.createEntity();
        Entity originalEntity = BaseRequests.get("/get/" + entityId)
                .then()
                .statusCode(200)
                .extract()
                .as(Entity.class);

        Entity updatedEntity = EntityHelper.generateRandomEntity();
        BaseRequests.patch("/patch/" + entityId, updatedEntity)
                .then()
                .statusCode(204);

        Entity updatedEntityFromServer = BaseRequests.get("/get/" + entityId)
                .then()
                .statusCode(200)
                .extract()
                .as(Entity.class);
        EntityHelper.verifyEntityUpdated(updatedEntityFromServer, updatedEntity, originalEntity);
    }

    @Story("Удаление Entity")
    @Description("Тест удаления Entity по его ID")
    @Test
    public void deleteEntityTest() {
        Entity entity = EntityHelper.generateRandomEntity();
        String entityId = BaseRequests.post("/create", entity)
                .then()
                .statusCode(200)
                .extract()
                .asString();

        BaseRequests.delete("/delete/" + entityId)
                .then()
                .statusCode(204);
        BaseRequests.get("/get/" + entityId)
                .then()
                .statusCode(500);

    }

    @Story("Получение Entity по ID")
    @Test
    public void getEntityTest() {
        String entityId = EntityHelper.createEntity();
        Entity entity = EntityHelper.getEntity(entityId);

        assertNotNull(entity.getId(), "ID сущности не должен быть null");
        assertNotNull(entity.getTitle(), "Название сущности не должно быть null");
    }

    @Story("Получение всех Entities")
    @Test
    public void getAllEntitiesTest() {
        int initialCount = BaseRequests.get("/getAll")
                .then()
                .statusCode(200)
                .extract()
                .as(EntitiesWrapper.class)
                .getEntity()
                .size();
        String entity1 = EntityHelper.createEntity();
        String entity2 = EntityHelper.createEntity();

        List<Entity> entities = BaseRequests.get("/getAll")
                .then()
                .statusCode(200)
                .extract()
                .as(EntitiesWrapper.class)
                .getEntity();
        EntityHelper.verifyEntitiesCountIncreased(entities, initialCount, entity1, entity2);
    }
}
