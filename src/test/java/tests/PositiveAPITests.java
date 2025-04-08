package tests;

import dto.Entity;
import helpers.EntityHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

@Epic("Тестирование API")
@Feature("Позитивные тесты")
public class PositiveAPITests extends BaseAPITests {

    @Story("Создание Entity")
    @Description("Тест создания нового Entity со сгенерированными данными")
    @Test(groups = "positive")
    public void createEntityTest() {
        String entityId = EntityHelper.createEntity();
        Entity createdEntity = EntityHelper.getEntity(entityId);
        EntityHelper.verifyEntityCreated(createdEntity, entityId);
    }

    @Story("Изменение Entity")
    @Description("Тест изменения существующего Entity")
    @Test(groups = "positive")
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
    @Test(groups = "positive")
    public void deleteEntityTest() {
        String entityId = EntityHelper.createEntity();
        EntityHelper.deleteEntity(entityId);
        EntityHelper.verifyEntityDeleted(entityId);
    }

    @Story("Получение Entity")
    @Description("Тест получения Entity по его ID")
    @Test(groups = "positive")
    public void getEntityTest() {
        String entityId = EntityHelper.createEntity();
        Entity entity = EntityHelper.getEntity(entityId);

        assertNotNull(entity.getId(), "ID Entity не должен быть null");
        assertNotNull(entity.getTitle(), "Название Entity не должно быть null");
    }

    @Story("Получение всех Entities")
    @Description("Тест получения всех Entities")
    @Test(groups = "positive")
    public void getAllEntitiesTest() {
        int initialCount = EntityHelper.getAllEntities().size();

        String entity1 = EntityHelper.createEntity();
        String entity2 = EntityHelper.createEntity();

        List<Entity> entities = EntityHelper.getAllEntities();
        EntityHelper.verifyEntitiesCountIncreased(entities, initialCount, entity1, entity2);
    }

    @Story("Получение всех Entities со случайными параметрами фильтрации")
    @Description("Тест получения всех Entities со случайно сгенерированными параметрами фильтрации")
    @Test(groups = "positive")
    public void getAllEntitiesWithRandomParamsTest() {
        List<Entity> allEntities = EntityHelper.getAllEntities();
        if (allEntities.isEmpty()) {
            fail("На сервере нет Entities для фильтрации");
            return;
        }
        Map<String, String> params = EntityHelper.generateRandomFilterParams(allEntities);
        List<Entity> filteredEntities = EntityHelper.getAllEntitiesWithParams(params);

        boolean randomVerified = Boolean.parseBoolean(params.get("verified"));
        assertTrue(filteredEntities.stream().allMatch(entity -> entity.isVerified() == randomVerified),
                "Entities должны быть с verified = " + randomVerified);
        String randomTitle = params.get("title");
        assertTrue(filteredEntities.stream().allMatch(entity -> entity.getTitle().contains(randomTitle)),
                "Entities должны иметь title, соответствующий фильтру: " + randomTitle);
        int randomPerPage = Integer.parseInt(params.get("perPage"));
        assertTrue(filteredEntities.size() <= randomPerPage,
                "Количество Entities на странице не должно превышать " + randomPerPage);
    }
}
