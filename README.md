# 📦 Subscription Service — Микросервис управления пользователями и подписками 

📄 [Техническое задание](TECHNICAL_TASK)

---

## 🛠 Структура проекта

- **Язык:** Java 17  
- **Фреймворк:** Spring Boot 3  
- **СУБД:** PostgreSQL  
- **Миграции:** Liquibase  
- **Логирование:** SLF4J + Logback  
- **Тестирование:** JUnit + Spring Test  
- **Документация:** Swagger UI  
- **Развёртывание:** Docker + Docker Compose  

---

## ⚙️ Требования для запуска

- Локально запущенный PostgreSQL (для dev/test)
  - host: `localhost`
  - port: `5432`
  - database: `subscriptiondb`
  - user: `postgres`
  - password: `postgres`

---

## 🔁 Liquibase: миграции базы данных

Миграции Liquibase выполняются автоматически при старте приложения.

### 🧱 Создание таблиц:

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE
);

CREATE TABLE subscriptions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE
);
```

---

## 🔗 Эндпоинты REST API

### 📁 UserController `/users`

| Метод | URI            | Описание                         |
|-------|----------------|----------------------------------|
| POST  | `/users`       | Создать нового пользователя      |
| GET   | `/users/{id}`  | Получить пользователя по ID      |
| GET   | `/users`       | Получить всех пользователей      |
| PUT   | `/users/{id}`  | Обновить пользователя по ID      |
| DELETE| `/users/{id}`  | Удалить пользователя по ID       |

---

### 📁 SubscriptionController

| Метод | URI                                      | Описание                                 |
|-------|------------------------------------------|------------------------------------------|
| POST  | `/users/{id}/subscriptions`              | Добавить подписку пользователю           |
| GET   | `/users/{id}/subscriptions`              | Получить список подписок пользователя    |
| DELETE| `/users/{id}/subscriptions/{subId}`      | Удалить подписку у пользователя          |
| GET   | `/subscriptions`                         | Получить все подписки                    |
| GET   | `/subscriptions/top`                     | Получить ТОП-3 популярных подписки       |

---

## ✅ Тестирование

- Тесты находятся в `src/test/java/com.example.subscription`
- Конфигурация тестов: `src/test/resources/application-test.yml`
- Для корректной работы тестов необходим локальный PostgreSQL (см. выше)

---

## 📖 Swagger UI

Swagger доступен по адресу:  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🐳 Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/subscription-service-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: subscriptiondb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/subscriptiondb
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
```

---

## 🚀 Клонирование и запуск

```bash
git clone https://github.com/yourname/subscription-service.git
cd subscription-service
mvn clean install
docker-compose up --build
```

---

## 🔍 Проверка работы

После запуска:

### Зайти в контейнер с базой:

```bash
docker exec -it subscription-service-db-1 psql -U postgres -d subscriptiondb
```

Примеры команд внутри:

```sql
\dt                -- список таблиц  
\d users           -- структура таблицы users  
SELECT * FROM users;  -- данные из таблицы  
```

### Тестовые curl-запросы:

#### Linux/macOS:

```bash
curl -X POST http://localhost:8080/users \
     -H "Content-Type: application/json" \
     -d '{"name":"John Doe","email":"john@example.com"}'

curl -X POST http://localhost:8080/users/1/subscriptions \
     -H "Content-Type: application/json" \
     -d '{"name":"Netflix"}'
```

#### PowerShell:

```bash
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john@example.com"}'
```

---

## 🧪 Дополнительно

- Используйте Postman или Swagger UI для работы с API
- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## ⚙️ Полезные команды для IntelliJ IDEA

```bash
docker-compose down -v       # Остановить и удалить контейнеры
docker-compose up --build    # Пересобрать и запустить контейнеры
Ctrl + C                     # Остановить процесс
```

---

> ✉️ Если возникли ошибки подключения к БД — проверьте, что PostgreSQL запущен и доступен по адресу `localhost:5432`, либо используйте Docker.

---

## 👤 Автор

- **Alexiisssss**  
  [![GitHub: Alexiisssss](https://img.shields.io/badge/GitHub-Alexiisssss-181717?style=flat-square&logo=github)](https://github.com/Alexiisssss)
