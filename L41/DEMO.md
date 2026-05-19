# Демонстрація Spring Security — Інструкція

## Підготовка

### Запуск API сервера (порт 8080)
```bash
cd L41
./mvnw spring-boot:run
```

### Запуск CORS-демо сервера (порт 3000) — для демо CORS
```bash
cd L41/cors-demo
python3 -m http.server 3000
```

### Облікові записи
| Username | Password  | Роль       |
|----------|-----------|------------|
| admin    | admin123  | ROLE_ADMIN |
| user     | user123   | ROLE_USER  |
| oleksiy  | pass123   | ROLE_USER  |

---

## Демо 1: Secure by Default (5 хв)

**Мета:** показати що Spring Security закриває все автоматично.

1. Відкрити браузер → `http://localhost:8080/api/users`
2. Автоматичний редірект на `/login` → Spring Security заблокував
3. Показати в консолі Spring Boot рядок:
   ```
   Using generated security password: ...
   ```
4. Показати що без нашого `SecurityConfig` навіть публічні ендпоінти закриті

**Висновок:** Spring Security = secure by default. Все закрито, відкриваємо тільки те, що потрібно.

---

## Демо 2: Authentication — Хто ти? (10 хв)

**Мета:** показати flow автентифікації через CustomUserDetailsService + BCrypt.

### 2.1 — Невірний пароль → 401
```bash
curl -u admin:wrongpass http://localhost:8080/api/admin/stats
```
→ **401 Unauthorized**

Пояснення flow:
```
curl → BasicAuthenticationFilter
     → AuthenticationManager
     → DaoAuthenticationProvider
     → CustomUserDetailsService.loadUserByUsername("admin")
     → Знайдено User у БД
     → PasswordEncoder.matches("wrongpass", "$2a$10$...") → FALSE
     → 401 Unauthorized
```

### 2.2 — Вірний пароль → 200
```bash
curl -u admin:admin123 http://localhost:8080/api/admin/stats
```
→ **200 OK** — `{"totalUsers":3,"message":"This endpoint is only for ADMIN role"}`

Пояснення flow:
```
curl → BasicAuthenticationFilter
     → AuthenticationManager
     → DaoAuthenticationProvider
     → CustomUserDetailsService.loadUserByUsername("admin")
     → Знайдено User у БД
     → PasswordEncoder.matches("admin123", "$2a$10$...") → TRUE
     → SecurityContextHolder.setAuthentication(...)
     → 200 OK
```

### 2.3 — Неіснуючий користувач → 401
```bash
curl -u hacker:password http://localhost:8080/api/admin/stats
```
→ **401 Unauthorized** (UsernameNotFoundException)

### 2.4 — Показати BCrypt хеші в H2 Console
1. Відкрити `http://localhost:8080/h2-console/`
2. JDBC URL: `jdbc:h2:mem:demodb`, User: `sa`, Password: (пусто)
3. Виконати: `SELECT username, password, role FROM users`
4. Показати що password — це BCrypt hash, НЕ відкритий текст
5. Два однакові паролі мають РІЗНІ хеші (різний salt)

---

## Демо 3: Authorization — Що тобі дозволено? (10 хв)

**Мета:** показати RBAC — різні ролі мають різний доступ.

### 3.1 — Публічний ендпоінт (без логіну)
```bash
curl http://localhost:8080/api/users
```
→ **200 OK** — список всіх користувачів (permitAll)

### 3.2 — Мій профіль (/me) через @AuthenticationPrincipal
```bash
curl -u user:user123 http://localhost:8080/api/users/me
```
→ **200 OK** — `{"username":"user","role":"ROLE_USER","firstName":"Марія",...}`

```bash
curl -u admin:admin123 http://localhost:8080/api/users/me
```
→ **200 OK** — `{"username":"admin","role":"ROLE_ADMIN","firstName":"Іван",...}`

Пояснити `@AuthenticationPrincipal UserDetails` — Spring витягує Principal з SecurityContext.

### 3.3 — USER намагається зробити DELETE → 403
```bash
curl -u user:user123 -X DELETE http://localhost:8080/api/users/3
```
→ **403 Forbidden** — USER не має прав на DELETE

Пояснення: SecurityConfig → `.requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")`

### 3.4 — ADMIN робить DELETE → 200
```bash
curl -u admin:admin123 -X DELETE http://localhost:8080/api/users/3
```
→ **200 OK** — `User 3 deleted`

### 3.5 — USER намагається зайти в /api/admin → 403
```bash
curl -u user:user123 http://localhost:8080/api/admin/stats
```
→ **403 Forbidden**

### 3.6 — ADMIN заходить в /api/admin → 200
```bash
curl -u admin:admin123 http://localhost:8080/api/admin/stats
```
→ **200 OK** — `{"totalUsers":2,"message":"This endpoint is only for ADMIN role"}`

### 3.7 — Підсумкова таблиця (показати на слайді)
```
                    БЕЗ логіну    USER          ADMIN
GET /api/users      200 ✓         200 ✓         200 ✓
GET /api/users/me   401 ✗         200 ✓         200 ✓
DELETE /api/users/3  401 ✗         403 ✗         200 ✓
GET /api/admin/stats 401 ✗         403 ✗         200 ✓
```
Зверніть увагу: 401 = не знаю хто ти, 403 = знаю, але не дозволено.

---

## Демо 4: Form Login у браузері (5 хв)

**Мета:** показати автентифікацію через форму в браузері.

1. Відкрити `http://localhost:8080/bank` → редірект на `/login`
2. Ввести `user` / `user123` → потрапляємо на сторінку банку
3. Показати баланс: 30 000 грн
4. Зробити переказ через форму — працює
5. Відкрити DevTools (F12) → Elements → знайти `<form>` → показати приховане поле `_csrf`
6. Вийти: `http://localhost:8080/logout`
7. Залогінитись як `admin` / `admin123` → баланс: 50 000 грн

---

## Демо 5: CSRF атака (10 хв)

**Мета:** показати як CSRF-токен захищає від шкідливих сайтів.

### 5.1 — CSRF увімкнений (захист працює)
1. Залогінитись на `http://localhost:8080/bank` (user/user123)
2. Запам'ятати баланс (30 000 грн)
3. В НОВІЙ вкладці відкрити `http://localhost:8080/evil.html`
4. Натиснути "Отримати приз!"
5. Результат → **403 Forbidden** (CSRF-токен відсутній у шкідливій формі)
6. Повернутися на `/bank` — баланс НЕ змінився → **атака заблокована**

**Пояснити:** evil.html має приховану форму, яка робить POST /transfer. Браузер автоматично додає session cookie, але НЕ додає _csrf токен. Сервер відхиляє запит.

### 5.2 — CSRF вимкнений (показати вразливість)
1. В `SecurityConfig.java` розкоментувати:
   ```java
   .csrf(csrf -> csrf.disable())
   ```
   і закоментувати блок з `.csrf(csrf -> csrf.ignoringRequestMatchers(...))`
2. Перезапустити додаток
3. Залогінитись на `/bank` → баланс 30 000 грн
4. Відкрити `evil.html` → натиснути "Отримати приз!"
5. Результат → баланс зменшився на 10 000 грн → **атака спрацювала!**

**Висновок:** CSRF-захист критичний для session-based додатків. Для REST API з JWT — вимикаємо, бо JWT не передається автоматично.

---

## Демо 6: CORS (10 хв)

**Мета:** показати як Same-Origin Policy та CORS працюють.

> Потрібен другий сервер: `cd cors-demo && python3 -m http.server 3000`

### 6.1 — CORS дозволений (поточна конфігурація)
1. Відкрити `http://localhost:3000` (фронтенд на іншому порту)
2. Натиснути "GET /api/users (fetch)"
3. Результат → **SUCCESS** — дані отримані
4. Відкрити F12 → Network → знайти запит → Response Headers:
   ```
   Access-Control-Allow-Origin: http://localhost:3000
   ```

### 6.2 — CORS заблокований
1. В `SecurityConfig.java` закоментувати:
   ```java
   // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
   ```
2. Перезапустити додаток
3. Відкрити `http://localhost:3000` → натиснути "GET /api/users"
4. Результат → **CORS BLOCKED!**
5. F12 → Console покаже:
   ```
   Access to fetch at 'http://localhost:8080/api/users' from origin
   'http://localhost:3000' has been blocked by CORS policy
   ```

**Пояснити:**
- Запит ДІЙШОВ до сервера (сервер відповів 200)
- Але БРАУЗЕР заблокував відповідь для JavaScript
- curl ігнорує CORS — показати: `curl http://localhost:8080/api/users` працює завжди
- CORS — це правило браузера, яке сервер може послабити через заголовки

### 6.3 — Що буде з origin: * ?
Пояснити різницю:
```java
config.setAllowedOrigins(List.of("http://localhost:3000")); // тільки наш фронт
config.setAllowedOrigins(List.of("*"));                      // будь-хто — небезпечно!
```

---

## Демо 7: H2 Console — Security Misconfiguration (3 хв)

**Мета:** показати приклад OWASP A05 — Security Misconfiguration.

1. Відкрити `http://localhost:8080/h2-console/`
2. Підключитися: JDBC URL = `jdbc:h2:mem:demodb`, User = `sa`, Password = (пусто)
3. Виконати: `SELECT * FROM users`
4. Показати що БЕЗ Security → будь-хто має повний доступ до БД через браузер
5. Пояснити: в production H2 Console має бути ВИМКНЕНА або захищена

---

## Шпаргалка curl-команд

```bash
# Публічний доступ
curl http://localhost:8080/api/users

# HTTP Basic auth
curl -u user:user123 http://localhost:8080/api/users/me
curl -u admin:admin123 http://localhost:8080/api/admin/stats

# DELETE (тільки admin)
curl -u admin:admin123 -X DELETE http://localhost:8080/api/users/3

# Невірний пароль
curl -u admin:wrong http://localhost:8080/api/admin/stats

# Перевірити HTTP код
curl -o /dev/null -w "HTTP %{http_code}" -u user:user123 http://localhost:8080/api/admin/stats
```

---

## Структура проекту

```
L41/
├── src/main/java/com/edu/l41/
│   ├── config/
│   │   ├── SecurityConfig.java          ← головна конфігурація безпеки
│   │   └── H2ConsoleConfig.java         ← реєстрація H2 Console servlet
│   ├── security/
│   │   └── CustomUserDetailsService.java ← завантаження User з БД для Spring Security
│   ├── controller/
│   │   ├── UserController.java          ← GET /api/users, DELETE, /me
│   │   ├── AdminController.java         ← GET /api/admin/stats (тільки ADMIN)
│   │   └── TransferController.java      ← GET /bank, POST /transfer (CSRF демо)
│   ├── entity/
│   │   └── User.java                   ← username, password (BCrypt), role
│   ├── repository/
│   │   └── UserRepository.java         ← findByUsername()
│   └── init/
│       └── DataLoader.java             ← створює 3 юзерів з BCrypt паролями
├── src/main/resources/
│   ├── templates/bank.html             ← легітимна форма (з CSRF-токеном)
│   ├── static/evil.html                ← "шкідливий сайт" (CSRF атака)
│   └── application.yaml
└── cors-demo/
    └── index.html                      ← фронтенд на порту 3000 (CORS демо)
```
