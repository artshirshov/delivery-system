# Система автоматизации корпоративной логистической службы

Проект состоит из следующих микросервисов:

1. Gateway-сервис
2. Discovery-сервис (совмещен с Config-сервисом)
3. Auth-сервис
4. Order-сервис
5. Payment-сервис
6. Inventory-сервис
7. Delivery-сервис

## Среда выполнения

Для запуска PostgreSQL с Kafka выполните следующую команду в корневом каталоге проекта:
```
$sudo docker-compose up -d
```

Также у вас есть удобный интерфейс для Apache Kafka, доступный по адресу:

http://localhost:9999/

Теперь вы можете запускать сервисы приложения из вашей IDE в следующем порядке:

- Discovery
- Auth-сервис
- Order-сервис
- Payment-сервис
- Inventory-сервис
- Delivery-сервис
- Gateway-сервис

После этого вы найдете объединенный интерфейс Swagger UI на Gateway по адресу:

http://localhost:9090/swagger-ui.html

## Основные взаимодействия
Вы можете использовать следующие команды curl или выполнить все эти действия через интерфейс Swagger UI.

### Аутентификация
Чтобы создать пользователя, выполните этот запрос к auth-сервису:
```bash
curl --location --request POST 'http://localhost:9090/auth-service/user/signup' \
--header 'Content-Type: application/json' \
--data '{
    "name": "user1",
    "password": "user1"
}'
```

После этого вы можете получить токен:
```bash
curl --location --request POST 'http://localhost:9090/auth-service/token/generate' \
--header 'Content-Type: application/json' \
--data '{
    "name": "user1",
    "password": "user1"
}'
```

Теперь вы можете использовать этот токен для аутентификации запросов к другим сервисам.

### Payment-сервис

Для создания баланса выполните POST-запрос:

```bash
curl --location --request POST 'http://localhost:9090/payment-service/balance' \
--header 'Authorization: Bearer <put token here>'
```

Для пополнения баланса пользователя выполните PATCH-запрос:

```bash
curl --location --request PATCH 'http://localhost:9090/payment-service/balance/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <put token here>' \
--data '{
    "sum": 9999999
}'
```

### Inventory-сервис

Для создания инвентаря выполните POST-запрос:

```bash
curl --location --request POST 'http://localhost:9090/inventory-service/inventory' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <put token here>' \
--data '{
    "title": "Test inventory",
    "count": 22,
    "costPerPiece": 10
}'
```

Для пополнения инвентаря выполните PATCH-запрос:

```bash
curl --location --request PATCH 'http://localhost:9090/inventory-service/inventory/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <put token here>' \
--data '{
    "count": 20
}'
```

### Order-сервис

Для создания заказа выполните POST-запрос:

```bash
curl --location --request POST 'http://localhost:9090/order-service/order' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <put token here>' \
--data '{
  "orderDtoList": [
    {
      "productId": 1,
      "count": 2
    }
  ],
  "cost": 20,
  "destinationAddress": "test"
}'
```

Вы можете изменить статус заказа с помощью PATCH-запроса:

```bash
curl --location --request PATCH 'http://localhost:9090/order-service/order/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <put token here>' \
--data '{
    "status": "REGISTERED",
    "serviceName": "ORDER_SERVICE",
    "comment": "Some comment to status"
}'
```

### Delivery-сервис
Для удаления доставки выполните DELETE-запрос:

```bash
curl --location --request DELETE 'http://localhost:9090/delivery-service/delivery/1' \
--header 'Authorization: Bearer <put token here>'
```

## Мониторинг
Вы также можете просматривать различные метрики приложения, используя стандартные метрики по URL:

http://localhost:9998/ 