# Backend сервиса аренды вещей.

Приложение состоит из двух модулей:
- **Gateway** отвечает за валидацию запросов пользователей
- **Server** хранит базу данных и обрабатывает запросы пользователей.

Есть возможность поиска предмотов в базе, добавления отзывов и подсчета рейтинга пользователей, создания запроса на
аренду.

## Использованные технологии

- Java 21
- SpringBoot
- Maven
- Lombok
- Docker
- PostgresSQL
- RestTemplate
- Hibernate
- Mockito

## Эндпойнты

### Пользователи

- добавление пользователя *POST /users*

```
{
    "name" : String,
    "email" : String
}
```
- получение пользователя по id *GET /users/{userId}*
- изменение информации о пользователе по id *PATCH /users/{userId}*
- удаление пользователя по id *DELETE /users/{userId}*
### Предметы
Требуется заголовок "X-Sharer-User-Id" c id пользователя.

- добавление предмета *POST /items*
```
{
     "name" : String,
     "description" : String,
     "requestId" : Long
}
```
- добавление комментария после аренды предмета *POST /items/{itemId}/comment*
```
{
    "text" : String
}
```
- обновление информации о предмете *PATCH /items/{itemId}*
- получение информции о предмете по id *GET /items/{itemId}*
- получение информации о всех предметах пользователя *GET /items*
- поиск предмета по названию или описанию *GET /items/search?String*
### Запросы на аренду
Требуется заголовок "X-Sharer-User-Id" c id пользователя.
- добавление нового запроса *POST /requests*
```
{
    "description" : String
}
```
- получение всех запросов пользователя *GET /requests*
- получение всех имеющихся запросов на аренду *GET /requests/all*
- получение информации о конкретном запросе *GET /requests/{requestId}*
### Бронирование предметов.
Требуется заголовок "X-Sharer-User-Id" c id пользователя.

- новый запрос на бронирование *POST /bookings*
```
{
    "start" : LocalDateTime
    "end" : LocalDateTime
    "itemId" : Long
}
```
- подтверждение/отклонение бронирования владельцем *PATCH /bookings/{bookingId}?String*
```
    APPROVED
    REJECTED
```

- получение информации о бронировании *GET /bookings/{bookingId}*
- получение информации о всех бронированиях пользователя по статусу *GET /bookings?String*
```
{
    ALL(default)
    CURRENT
    FUTURE
    PAST
    REJECTED
    WAITING
}
```
- получение информации о бронированиях предметов пользователя *GET /bookings/owner?String*
