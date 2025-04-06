package helpers;

import com.github.javafaker.Faker;
import dto.Entity;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntityHelper {
    private static final Faker faker = new Faker(new Locale("ru"));
    private static final ThreadLocal<List<String>> threadEntities =
            ThreadLocal.withInitial(ArrayList::new);

    /**
     * Генерирует случайный объект Entity с использованием JavaFaker.
     *
     * @return Объект Entity с заполненными случайными данными.
     */
    @Step("Генерация Entity")
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
        entity.setImportant_numbers(important_numbers);
        return entity;
    }

    @Step("Отправка Entity на сервер")
    public static String createEntity() {
        Entity entity = generateRandomEntity();

        String entityId = BaseRequests.post("/create", entity)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        threadEntities.get().add(entityId);
        return entityId;
    }

    @Step("Получение Entity")
    public static Entity getEntity(String entityId) {
        return BaseRequests.get("/get/" + entityId)
                .then()
                .statusCode(200)
                .extract()
                .as(Entity.class);
    }

    @Step("Очистка Entity")
    public static void cleanup() {
        List<String> entitiesToDelete = threadEntities.get();
        if (entitiesToDelete == null || entitiesToDelete.isEmpty()) {
            return;
        }

        for (String entityId : new ArrayList<>(entitiesToDelete)) {
            BaseRequests.delete("/delete/" + entityId)
                    .then()
                    .statusCode(204);
        }
    }

    @Step("Очистка данных потока")
    public static void releaseResources() {
        threadEntities.remove();
    }
}
