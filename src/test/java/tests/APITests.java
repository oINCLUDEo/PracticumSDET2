package tests;

import dto.Entity;
import helpers.EntityHelper;
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
        Entity createdEntity = EntityHelper.getEntity(entityId);
        EntityHelper.verifyEntityCreated(createdEntity, entityId);
    }

    @Story("Изменение Entity")
    @Description("Тест изменения существующего Entity")
    @Test
    public void updateEntityTest() {
        String entityId = EntityHelper.createEntity();
        Entity originalEntity = EntityHelper.getEntity(entityId);

        Entity updatedEntity = EntityHelper.generateRandomEntity();
        EntityHelper.updateEntity(entityId, updatedEntity);

        Entity updatedEntityFromServer = EntityHelper.getEntity(entityId);
        EntityHelper.verifyEntityUpdated(updatedEntityFromServer, updatedEntity, originalEntity);
    }

    @Story("Удаление Entity")
    @Description("Тест удаления Entity по его ID")
    @Test
    public void deleteEntityTest() {
        String entityId = EntityHelper.createEntity();
        EntityHelper.deleteEntity(entityId);
        EntityHelper.verifyEntityDeleted(entityId);
    }

    @Story("Получение Entity по ID")
    @Test
    public void getEntityTest() {
        String entityId = EntityHelper.createEntity();
        Entity entity = EntityHelper.getEntity(entityId);

        assertNotNull(entity.getId(), "ID Entity не должен быть null");
        assertNotNull(entity.getTitle(), "Название Entity не должно быть null");
    }

    @Story("Получение всех Entities")
    @Test
    public void getAllEntitiesTest() {
        int initialCount = EntityHelper.getAllEntities().size();

        String entity1 = EntityHelper.createEntity();
        String entity2 = EntityHelper.createEntity();

        List<Entity> entities = EntityHelper.getAllEntities();
        EntityHelper.verifyEntitiesCountIncreased(entities, initialCount, entity1, entity2);
    }
}
