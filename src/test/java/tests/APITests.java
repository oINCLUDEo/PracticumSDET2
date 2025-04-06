package tests;

import dto.EntitiesWrapper;
import dto.Entity;
import helpers.EntityHelper;
import helpers.BaseRequests;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

@Epic("Тестирование API")
@Feature("Взаимодействие с Entity")
public class APITests {
    private static final List<String> createdEntities = Collections.synchronizedList(new ArrayList<>());

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
        assertNotNull(createdEntity, "Сущность не должна быть null");
        assertNotNull(createdEntity.getId(), "ID сущности не должен быть null");
        assertEquals(createdEntity.getId(), entityId, "ID созданной сущности должен соответствовать возвращенному при создании");
        assertNotNull(createdEntity.getTitle(), "Название сущности не должно быть null");
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
        assertEquals(updatedEntityFromServer.getTitle(), updatedEntity.getTitle(),
                "Название сущности должно обновиться");
        assertNotEquals(updatedEntityFromServer.getTitle(), originalEntity.getTitle(),
                "Название сущности должно отличаться от исходного");
        assertEquals(updatedEntityFromServer.getId(), originalEntity.getId(),
                "ID сущности не должен изменяться при обновлении");
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
        String Entity1 = EntityHelper.createEntity();
        String Entity2 = EntityHelper.createEntity();

        List<Entity> entities = BaseRequests.get("/getAll")
                .then()
                .statusCode(200)
                .extract()
                .as(EntitiesWrapper.class)
                .getEntity();
        assertTrue(entities.size() >= initialCount + 2,
                "Общее количество сущностей должно увеличиться на 2");
        assertTrue(entities.stream().anyMatch(e -> e.getId().equals(Entity1)),
                "Список должен содержать первую созданную сущность");
        assertTrue(entities.stream().anyMatch(e -> e.getId().equals(Entity2)),
                "Список должен содержать вторую созданную сущность");
    }
}
