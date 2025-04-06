# API Testing Project

# 🏦 Автотесты для API сущности "Entity"

[![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk)](https://www.java.com/ru/)
[![RestAssured](https://img.shields.io/badge/RestAssured-5.3-blue?logo=restassured)](https://rest-assured.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red?logo=testng)](https://testng.org)
[![Maven](https://img.shields.io/badge/Maven-3.8-orange?logo=apachemaven)](https://maven.apache.org)

Автоматизированные тесты для взаимодействия с API сущности "Entity" с использованием RestAssured и TestNG.

## 📌 О проекте

Проект содержит автотесты для проверки API сущности "Entity" с операциями:
1. Создание новой сущности
2. Получение сущности по ID
3. Получение всех сущностей
4. Обновление сущности
5. Удаление сущности по ID

Тесты используют случайно сгенерированные данные для проверки корректности работы API.

## 🛠 Технологии
1. **Язык**: Java 17
2. **Тестирование API**: RestAssured 4.4.0
3. **Фреймворк**: TestNG 7.4
4. **Сборка**: Maven 3.8+
5. **CI/CD**: GitHub Actions

## 🚀 Развертывание и использование

### Начало работы

1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/bondarenkokate73/simbirsoft_sdet_project.git
    cd simbirsoft_sdet_project
    ```

2. Установите Docker и Docker Compose, если они еще не установлены. Следуйте инструкциям на официальных страницах:
   - [Docker](https://docs.docker.com/get-docker/)
   - [Docker Compose](https://docs.docker.com/compose/install/)

3. Для запуска сервиса, базы данных и миграций, перейдите в каталог проекта и выполните:

    ```bash
    make run
    ```
    или
    ```bash
    docker-compose up --build -d
    ```

4. Проверьте, что сервис работает, зайдя по адресу [http://localhost:8080](http://localhost:8080).

### Точки доступа API

1. **Создание сущности**: `POST /api/create`
2. **Удаление сущности**: `DELETE /api/delete/{id}`
3. **Получение сущности**: `GET /api/get/{id}`
4. **Получение всех сущностей**: `POST /api/getAll`
5. **Обновление сущности**: `PATCH /api/patch/{id}`

### Документация Swagger

- Swagger документация доступна по следующему адресу: [http://localhost:8080/api/_/docs/swagger/](http://localhost:8080/api/_/docs/swagger/)

## 🧪 Тестирование

### Запуск тестов

Проект включает автотесты для взаимодействия с API. Перед запуском тестов, убедитесь, что сервис и база данных развернуты.

1. Клонируйте репозиторий с тестами:
    ```bash
    git clone https://github.com/ваш-проект/api-testing-project.git
    cd api-testing-project
    ```

2. Соберите проект:
    ```bash
    mvn clean install
    ```

3. Запуск всех тестов:
    ```bash
    mvn test
    ```

4. Параллельный запуск тестов (3 потока):
    ```bash
    mvn test -Dsurefire.suiteXmlFiles=test_suite.xml
    ```

5. Генерация Allure отчета:
    ```bash
    mvn clean test
    allure serve allure-results
    ```

### Пример конфигурации для параллельного запуска тестов

Для параллельного выполнения тестов с использованием TestNG, используйте следующий `test_suite.xml`:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.1.dtd">
<suite name="API Test Suite" parallel="methods" thread-count="5">
    <test name="Add Customer Test">
        <classes>
            <class name="tests.APITests">
                <methods>
                    <include name="createEntityTest"/>
                    <include name="getEntityTest"/>
                    <include name="getAllEntitiesTest"/>
                    <include name="updateEntityTest"/>
                    <include name="deleteEntityTest"/>
                </methods>
            </class>
        </classes>
    </test>
</suite>
```

## 🔄 CI/CD Pipeline

Проект включает GitHub Actions workflow (`run_tests.yml`) с:
- Автоматическим запуском тестов при push/pull request
- Кешированием Maven зависимостей
- Генерацией и публикацией Allure отчетов
- Деплоем отчета на GitHub Pages

## 📊 Отчеты

Проект поддерживает:

- Подробные Allure отчеты с шагами
- Скриншоты для упавших тестов (настроено в Selenide)

## 🌟 Особенности реализации

- Генерация случайных данных для создания новых сущностей
- Параметризованный тест для работы с различными сущностями
- Поддержка различных операций CRUD (Create, Read, Update, Delete)
- Тесты с проверкой на корректность работы API
- Умный алгоритм получения случайных сущностей для тестов

---