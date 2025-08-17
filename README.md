# Online Food Ordering System — Spring Boot + MySQL (Beginner-Friendly)

**No Lombok, No JavaMelody.** Uses Spring Boot Web, Security, Data JPA, MySQL, JUnit/Mockito.
Implements: user registration/login, menu management, cart & checkout, mock payment (async), order tracking, feedback, logging, error handling, audit logs (JDBC).

## 1) MySQL Setup
```sql
CREATE DATABASE fooddb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Update `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:mysql://localhost:3306/fooddb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASS
```

## 2) Run
```bash
mvn spring-boot:run
```
Tables are auto-created (`spring.jpa.hibernate.ddl-auto=update`). `data.sql` seeds sample users/restaurants/menu.

## 3) Default Users (HTTP Basic)
- Admin: `admin@food.com` / `admin123`
- User:  `user@food.com`  / `user123`

## 4) Key Endpoints
Public:
- `POST /auth/register` `{email, password, fullName}`
- `GET /restaurants`
- `GET /restaurants/{id}/menu`

User:
- `POST /cart/add?itemId=&qty=`
- `GET /cart`
- `DELETE /cart`
- `POST /orders/place?restaurantId=`
- `GET /orders/{id}/status`
- `GET /orders/my`

Admin:
- `POST /restaurants/admin/{id}/menu` body: `MenuItem`
- `POST /orders/admin/{id}/status?status=PREPARING|DISPATCHED|DELIVERED`

Payment:
- `POST /payments/initiate?orderId=&token=demo` (async; SUCCESS if token non-empty)

Feedback:
- `POST /feedback?orderId=&rating=&comment=` (after status DELIVERED)

## 5) Notes for Viva
- **OOP**: Entities, services; enums; encapsulation.
- **Collections**: `ConcurrentHashMap` for carts; lists of items.
- **Exceptions**: `GlobalExceptionHandler` + custom messages.
- **Multithreading**: Payment async processing via `Executors`.
- **JDBC**: `AuditJdbcDao` writes to `tx_audit`.
- **JPA**: Entities/relations; repositories.
- **Security**: HTTP Basic + roles.
- **Tests**: JUnit + Mockito (`OrderServiceTest`).

