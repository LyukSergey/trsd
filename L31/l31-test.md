# L31 - REST API Testing Guide

Base URL: `http://localhost:8080`

---

## 1. Banks (Банки)

### Отримати всi банки
```http
GET http://localhost:8080/banks
```

Очiкувана вiдповiдь:
```json
[
  { "id": 1, "name": "Приват Банк", "totalAmount": 1000000.0 },
  { "id": 2, "name": "Райфайзен Банк", "totalAmount": 1000000.0 }
]
```

### Отримати банк по ID
```http
GET http://localhost:8080/banks/1
```

### Отримати банк по ID користувача
```http
GET http://localhost:8080/banks/user/1
```

Поверне банк, до якого належить користувач з id=1 (Iван Франко -> Приват Банк).

---

## 2. Users (Користувачi)

### Отримати всiх користувачiв
```http
GET http://localhost:8080/users
```

### Отримати користувачiв банку (OneToMany)
```http
GET http://localhost:8080/banks/1/users
```

Поверне 10 користувачiв Приват Банку.

```http
GET http://localhost:8080/banks/2/users
```

Поверне 5 користувачiв Райфайзен Банку.

### Створити нового користувача в банку
```http
POST http://localhost:8080/banks/1/users
Content-Type: application/json

{
  "name": "Новий",
  "surname": "Користувач"
}
```

### Видалити користувача
```http
DELETE http://localhost:8080/banks/users/16
```

### Користувач з найдовшим прiзвищем в банку (SQL vs Stream)

За допомогою SQL-запиту:
```http
GET http://localhost:8080/banks/1/users/max-surname-length
```

За допомогою Java Stream:
```http
GET http://localhost:8080/banks/1/users/max-surname-length?with-stream=true
```

Порiвняйте час виконання в логах сервера.

---

## 3. Roles (Ролi) - ManyToMany

### Отримати всi ролi
```http
GET http://localhost:8080/roles
```

Очiкувана вiдповiдь:
```json
[
  { "id": 1, "name": "ADMIN" },
  { "id": 2, "name": "USER" },
  { "id": 3, "name": "MANAGER" }
]
```

### Отримати роль по ID
```http
GET http://localhost:8080/roles/1
```

### Отримати ролi користувача (ManyToMany)
```http
GET http://localhost:8080/roles/user/1
```

Iван Франко має ролi ADMIN та USER:
```json
[
  { "id": 1, "name": "ADMIN" },
  { "id": 2, "name": "USER" }
]
```

```http
GET http://localhost:8080/roles/user/3
```

Леся Українка має ролi USER та MANAGER:
```json
[
  { "id": 2, "name": "USER" },
  { "id": 3, "name": "MANAGER" }
]
```

---

## 4. User Profiles (Профiлi) - OneToOne

### Отримати профiль користувача
```http
GET http://localhost:8080/profiles/user/1
```

Профiль Iвана Франка:
```json
{
  "id": 1,
  "email": "ivan.franko@example.com",
  "phone": "+380501111111",
  "userId": 1
}
```

```http
GET http://localhost:8080/profiles/user/2
```

Профiль Тараса Шевченка:
```json
{
  "id": 2,
  "email": "taras.shevchenko@example.com",
  "phone": "+380502222222",
  "userId": 2
}
```

```http
GET http://localhost:8080/profiles/user/11
```

Профiль Марiї Кюрi:
```json
{
  "id": 4,
  "email": "maria.curie@example.com",
  "phone": "+380504444444",
  "userId": 11
}
```

---

## 5. Демонстрацiя зв'язкiв (Relationships)

### OneToMany: Bank -> Users
Один банк має багато користувачiв:
```http
GET http://localhost:8080/banks/1/users
```

### ManyToOne: User -> Bank
Кожен користувач належить одному банку:
```http
GET http://localhost:8080/banks/user/1
```

### OneToOne: User <-> UserProfile
Кожен користувач має один профiль:
```http
GET http://localhost:8080/profiles/user/1
```

### ManyToMany: User <-> Role
Користувач може мати багато ролей, роль може належати багатьом користувачам:
```http
GET http://localhost:8080/roles/user/1
```

---

## 6. Тестовi данi (test-data.xml)

### Банки
| ID | Назва           | Сума       |
|----|-----------------|------------|
| 1  | Приват Банк     | 1000000.00 |
| 2  | Райфайзен Банк  | 1000000.00 |

### Користувачi
| ID  | Iм'я      | Прiзвище        | Банк           |
|-----|-----------|-----------------|----------------|
| 1   | Iван      | Франко          | Приват Банк    |
| 2   | Тарас     | Шевченко        | Приват Банк    |
| 3   | Леся      | Українка        | Приват Банк    |
| 4   | Григорiй  | Сковорода       | Приват Банк    |
| 5   | Ольга     | Кобилянська     | Приват Банк    |
| 6   | Богдан    | Хмельницький    | Приват Банк    |
| 7   | Ярослав   | Мудрий          | Приват Банк    |
| 8   | Соломiя   | Крушельницька   | Приват Банк    |
| 9   | Михайло   | Грушевський     | Приват Банк    |
| 10  | Лiна      | Костенко        | Приват Банк    |
| 11  | Марiя     | Кюрi            | Райфайзен Банк |
| 12  | Альберт   | Ейнштейн        | Райфайзен Банк |
| 13  | Нiкола    | Тесла           | Райфайзен Банк |
| 14  | Iсаак     | Ньютон          | Райфайзен Банк |
| 15  | Леонардо  | да Вiнчi        | Райфайзен Банк |

### Ролi
| ID | Назва   |
|----|---------|
| 1  | ADMIN   |
| 2  | USER    |
| 3  | MANAGER |

### Призначення ролей (user_role)
| Користувач         | Ролi            |
|--------------------|-----------------|
| Iван Франко        | ADMIN, USER     |
| Тарас Шевченко     | USER            |
| Леся Українка      | USER, MANAGER   |
| Марiя Кюрi         | ADMIN           |
| Альберт Ейнштейн   | USER, MANAGER   |

### Профiлi користувачiв (user_profile)
| Користувач         | Email                          | Телефон        |
|--------------------|--------------------------------|----------------|
| Iван Франко        | ivan.franko@example.com        | +380501111111  |
| Тарас Шевченко     | taras.shevchenko@example.com   | +380502222222  |
| Леся Українка      | lesya.ukrainka@example.com     | +380503333333  |
| Марiя Кюрi         | maria.curie@example.com        | +380504444444  |
| Альберт Ейнштейн   | albert.einstein@example.com    | +380505555555  |
