package helpers;

import com.github.javafaker.Faker;
import dto.Entity;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.testng.Assert.*;

public class EntityHelper {
    private static final String CREATE_ENTITY = "/create";
    private static final String GET_ENTITY = "/get/";
    private static final String PATCH_ENTITY = "/patch/";
    private static final String DELETE_ENTITY = "/delete/";
    private static final String GET_ALL_ENTITIES = "/getAll";

    private static final Faker faker = new Faker(new Locale("ru"));
    private static final ThreadLocal<List<String>> threadEntities =
            ThreadLocal.withInitial(ArrayList::new);

    /**
     * Генерирует случайный объект Entity с использованием JavaFaker.
     *
     * @return Объект Entity с заполненными случайными данными.
     */
    @Step("Генерация случайных данных для Entity")
    public static Entity generateRandomEntity() {
        Entity entity = new Entity();
        entity.setTitle(faker.funnyName().name());
        entity.setVerified(faker.bool().bool());

        Entity.Addition addition = new Entity.Addition();
        addition.setAdditionalInfo(faker.job().title());
        addition.setAdditionalNumber(faker.number().numberBetween(1, 1000));
        entity.setAddition(addition);

        int arraySize = faker.number().numberBetween(1, 10);
        int[] important_numbers = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            important_numbers[i] = faker.number().numberBetween(1, 100);
        }
        entity.setImportantNumbers(important_numbers);
        return entity;
    }

    /**
     * Генерирует случайные параметры для фильтрации сущностей.
     * @param allEntities Список всех сущностей для фильтрации.
     * @return Случайно сгенерированные параметры.
     */
    public static Map<String, String> generateRandomFilterParams(List<Entity> allEntities) {
        if (allEntities.isEmpty()) {
            throw new IllegalStateException("Список сущностей не должен быть пустым");
        }

        Faker faker = new Faker();
        String randomTitle = allEntities.get(faker.number().numberBetween(0, allEntities.size())).getTitle();
        boolean randomVerified = faker.bool().bool();
        int totalPages = (int) Math.ceil(allEntities.size() / 10.0);
        int randomPage = faker.number().numberBetween(1, totalPages > 0 ? totalPages : 1);
        int randomPerPage = faker.number().numberBetween(2, 10);

        return Map.of(
                "title", randomTitle,
                "verified", String.valueOf(randomVerified),
                "page", String.valueOf(randomPage),
                "perPage", String.valueOf(randomPerPage)
        );
    }

    @Step("Создание нового Entity на сервере")
    public static String createEntity() {
        Entity entity = generateRandomEntity();

        String entityId = BaseRequests.post(CREATE_ENTITY, entity)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        threadEntities.get().add(entityId);
        return entityId;
    }

    @Step("Получение Entity с ID {entityId}")
    public static Entity getEntity(String entityId) {
        return BaseRequests.get(GET_ENTITY + entityId)
                .then()
                .statusCode(200)
                .extract()
                .as(Entity.class);
    }

    @Step("Обновление Entity с ID {entityId}")
    public static void updateEntity(String entityId, Entity updatedEntity) {
        BaseRequests.patch(PATCH_ENTITY + entityId, updatedEntity)
                .then()
                .statusCode(204);
    }

    @Step("Удаление Entity с ID {entityId}")
    public static void deleteEntity(String entityId) {
        BaseRequests.delete(DELETE_ENTITY + entityId)
                .then()
                .statusCode(204);
        threadEntities.get().remove(entityId);
    }

    @Step("Получение всех Entities без параметров")
    public static List<Entity> getAllEntities() {
        return BaseRequests.get(GET_ALL_ENTITIES)
                .then()
                .statusCode(200)
                .extract()
                .as(dto.EntitiesWrapper.class)
                .getEntity();
    }

    @Step("Получение всех Entities с параметрами {params}")
    public static List<Entity> getAllEntitiesWithParams(Map<String, String> params) {
        return BaseRequests.getWithParams(GET_ALL_ENTITIES, params)
                .then()
                .statusCode(200)
                .extract()
                .as(dto.EntitiesWrapper.class)
                .getEntity();
    }

    @Step("Очистка Entities после выполнения тестов")
    public static void cleanup() {
        List<String> entitiesToDelete = threadEntities.get();
        if (entitiesToDelete == null || entitiesToDelete.isEmpty()) {
            return;
        }

        for (String entityId : new ArrayList<>(entitiesToDelete)) {
            BaseRequests.delete(DELETE_ENTITY + entityId)
                    .then()
                    .statusCode(204);
        }
    }

    @Step("Проверка созданного Entity с ID {expectedEntityId}")
    public static void verifyEntityCreated(Entity createdEntity, String expectedEntityId) {
        assertNotNull(createdEntity, "Entity не должен быть null");
        assertNotNull(createdEntity.getId(), "ID Entity не должен быть null");
        assertEquals(createdEntity.getId(), expectedEntityId, "ID созданного Entity должен соответствовать возвращенному при создании");
        assertNotNull(createdEntity.getTitle(), "Название Entity не должно быть null");
    }

    @Step("Проверка обновленного Entity")
    public static void verifyEntityUpdated(Entity updatedEntityFromServer, Entity updatedEntity, Entity originalEntity) {
        assertEquals(updatedEntityFromServer.getTitle(), updatedEntity.getTitle(),
                "Название Entity должно обновиться");
        assertNotEquals(updatedEntityFromServer.getTitle(), originalEntity.getTitle(),
                "Название Entity должно отличаться от исходного");
        assertEquals(updatedEntityFromServer.getId(), originalEntity.getId(),
                "ID Entity не должен изменяться при обновлении");
    }

    @Step("Проверка, что Entity с ID {entityId} успешно удалён")
    public static void verifyEntityDeleted(String entityId) {
        BaseRequests.get(GET_ENTITY + entityId)
                .then()
                .statusCode(500);
    }

    @Step("Проверка увеличения количества Entities")
    public static void verifyEntitiesCountIncreased(List<Entity> entities, int initialCount, String entity1Id, String entity2Id) {
        assertTrue(entities.size() >= initialCount + 2,
                "Общее количество сущностей должно увеличиться на 2");
        assertTrue(entities.stream().anyMatch(e -> e.getId().equals(entity1Id)),
                "Список должен содержать первую созданную сущность");
        assertTrue(entities.stream().anyMatch(e -> e.getId().equals(entity2Id)),
                "Список должен содержать вторую созданную сущность");
    }

    @Step("Проверка на ошибку 500 при удалении несуществующего Entity с ID {entityId}")
    public static void verifyEntityDeleteFails(String entityId) {
        BaseRequests.delete(DELETE_ENTITY + entityId)
                .then()
                .statusCode(500);
    }

    @Step("Проверка на ошибку 500 для несуществующего Entity с ID {entityId}")
    public static void verifyEntityNotFound(String entityId) {
        BaseRequests.get(GET_ENTITY + entityId)
                .then()
                .statusCode(500);
    }

    @Step("Проверка на ошибку 400 при обновлении несуществующего Entity с ID {entityId}")
    public static void verifyEntityUpdateFails(String entityId, Entity updatedEntity) {
        BaseRequests.patch(PATCH_ENTITY + entityId, updatedEntity)
                .then()
                .statusCode(400);
    }

    @Step("Проверка на ошибку при создании Entity с некорректными данными")
    public static void verifyEntityCreationFails(Entity entity) {
        BaseRequests.post(CREATE_ENTITY, entity)
                .then()
                .statusCode(500);
    }


    @Step("Освобождение ресурсов потока")
    public static void releaseResources() {
        threadEntities.remove();
    }
}
