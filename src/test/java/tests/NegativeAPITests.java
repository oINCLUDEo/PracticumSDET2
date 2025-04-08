package tests;

import dto.Entity;
import helpers.EntityHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Тестирование API")
@Feature("Негативные тесты")
public class NegativeAPITests extends BaseAPITests {

    private static final String nonExistentEntityId = "non-existent-id";

    @Story("Удаление несуществующего Entity")
    @Description("Тест удаления Entity, которого не существует")
    @Test(groups = "negative")
    public void deleteNonExistentEntityTest() {
        EntityHelper.verifyEntityDeleteFails(nonExistentEntityId);
    }

    @Story("Получение несуществующего Entity")
    @Description("Тест получения Entity, которого не существует")
    @Test(groups = "negative")
    public void getNonExistentEntityTest() {
        EntityHelper.verifyEntityNotFound(nonExistentEntityId);
    }

    @Story("Обновление несуществующего Entity")
    @Description("Тест обновления Entity, которого не существует")
    @Test(groups = "negative")
    public void updateNonExistentEntityTest() {
        Entity updatedEntity = EntityHelper.generateRandomEntity();
        EntityHelper.verifyEntityUpdateFails(nonExistentEntityId, updatedEntity);
    }

    @Story("Создание Entity с некорректными данными")
    @Description("Тест создания Entity с недостающими или неверными данными")
    @Test(groups = "negative")
    public void createEntityWithInvalidDataTest() {
        Entity invalidEntity = new Entity();
        EntityHelper.verifyEntityCreationFails(invalidEntity);
    }
}
