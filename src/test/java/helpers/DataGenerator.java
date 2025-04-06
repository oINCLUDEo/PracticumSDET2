package helpers;

import com.github.javafaker.Faker;
import dto.Entity;

import java.util.Locale;

public class DataGenerator {

    private static final Faker faker = new Faker(new Locale("ru"));

    /**
     * Генерирует случайный объект Entity с использованием JavaFaker.
     *
     * @return Объект Entity с заполненными случайными данными.
     */
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
}