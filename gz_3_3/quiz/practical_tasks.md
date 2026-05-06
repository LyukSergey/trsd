# Практичне заняття GZ 3.3

---

## Завдання 1 (4 бали) — Каталог фільмів

Реалізувати REST API сервіс каталогу фільмів з можливістю імпорту даних із зовнішнього джерела.

### Що має робити сервіс

Сервіс дозволяє користувачу знайти фільм за назвою у зовнішній базі OMDb, імпортувати його у локальну PostgreSQL базу, шукати серед збережених фільмів, залишати відгуки та переглядати рейтинг.

**Сценарій роботи з імпортом:**
1. Користувач шукає фільм за назвою — сервіс повертає список знайдених результатів з OMDb API
2. Користувач обирає потрібний фільм — сервіс завантажує повну інформацію та зберігає у базу разом із жанрами

**Зв'язки між сутностями:**
- Фільм належить до кількох жанрів, жанр містить багато фільмів → **Many-to-Many**
- Фільм має багато відгуків → **One-to-Many**

### OMDb API

Ключ: зареєструватись на https://www.omdbapi.com/apikey.aspx (безкоштовно, 1000 запитів/день).

Пошук за назвою:
```
GET http://www.omdbapi.com/?apikey=YOUR_KEY&s=Matrix
```
```json
{
  "Search": [
    {"Title": "The Matrix", "Year": "1999", "imdbID": "tt0133093", "Type": "movie", "Poster": "https://..."},
    {"Title": "The Matrix Reloaded", "Year": "2003", "imdbID": "tt0234215", "Type": "movie", "Poster": "https://..."}
  ],
  "totalResults": "20"
}
```

Деталі за ID:
```
GET http://www.omdbapi.com/?apikey=YOUR_KEY&i=tt0133093
```
```json
{
  "Title": "The Matrix",
  "Year": "1999",
  "Genre": "Action, Sci-Fi",
  "Plot": "When a beautiful stranger leads computer hacker Neo to...",
  "Poster": "https://...",
  "imdbRating": "8.7",
  "imdbID": "tt0133093"
}
```

Flow імпорту: пошук за назвою (`?s=`) → вибір → деталі за ID (`?i=`) → збереження.

### Вимоги

**Entities:**
- Movie (id, imdbId, title, year, plot, posterUrl, rating)
- Genre (id, name) — Many-to-Many з Movie
- Review (id, author, text, score, createdAt, movie_id) — One-to-Many

**Liquibase:**
- `001-create-tables.yaml` — таблиці movie, genre, review, movie_genre
- `002-insert-genres.yaml` — початкові жанри (Action, Drama, Comedy, Thriller, Sci-Fi)

**Repository:**
- `findByTitleContainingIgnoreCase(String title, Pageable pageable)`
- `@Query` — топ-5 фільмів за середнім score відгуків (JOIN з Review)
- `findByGenresNameIn(List<String> genres)`
- `findByMovieIdOrderByCreatedAtDesc(Long movieId, Pageable pageable)`

**Service:**
- Пошук фільмів у OMDb API за назвою
- Імпорт фільму з OMDb за imdbId (парсинг жанрів, збереження зв'язків)
- Локальний пошук з пагінацією
- Додавання відгуку

**Endpoints:**
- `GET /api/movies/search-external?title=Matrix`
- `POST /api/movies/import?imdbId=tt0133093`
- `GET /api/movies?title=Matrix&page=0&size=5`
- `GET /api/movies/top-rated`
- `POST /api/movies/{id}/reviews`
- `GET /api/movies/{id}/reviews?page=0&size=10`

### Оцінювання

| Критерій | Бали |
|----------|------|
| JPA entities з коректними відносинами | 1 |
| Liquibase міграції | 0.5 |
| Repository з кастомними query + Pagination | 1 |
| Service з інтеграцією OMDb API | 1 |
| REST endpoints | 0.5 |

---

## Завдання 2 (6 балів) — Планувальник подорожей

Реалізувати REST API сервіс для планування автомобільних подорожей між країнами з перевіркою маршруту та прогнозом погоди.

### Що має робити сервіс

Сервіс завантажує інформацію про країни із зовнішнього API, дозволяє створювати подорожі з маршрутом (впорядкований список країн), валідує що маршрут географічно можливий (кожна наступна країна межує з попередньою), та показує актуальну погоду для кожної точки маршруту.

**Сценарій роботи:**
1. Адмін синхронізує країни певного регіону з RestCountries API у локальну базу
2. Користувач шукає країни за фільтрами (регіон, мова, населення) з пагінацією
3. Користувач створює подорож — вказує назву, дати, та список країн у порядку відвідування
4. Сервіс валідує маршрут: перевіряє що кожна пара сусідніх країн має спільний кордон
5. При перегляді подорожі сервіс підтягує актуальну погоду у столиці кожної країни маршруту

**Зв'язки між сутностями:**
- Країна має кілька мов, мова використовується у кількох країнах → **Many-to-Many**
- Подорож містить список зупинок (країн) у певному порядку → **One-to-Many** з додатковими полями
- Країна має історію погодних знімків → **One-to-Many**

### Валідація маршруту

RestCountries API повертає для кожної країни поле `borders` — масив alpha3-кодів сусідніх країн:
```json
{"name": {"common": "Poland"}, "cca3": "POL", "borders": ["DEU", "CZE", "SVK", "UKR", "BLR", "LTU", "RUS"]}
{"name": {"common": "Czechia"}, "cca3": "CZE", "borders": ["AUT", "DEU", "POL", "SVK"]}
{"name": {"common": "Austria"}, "cca3": "AUT", "borders": ["CZE", "DEU", "HUN", "ITA", "LIE", "SVK", "SVN", "CHE"]}
```

При створенні подорожі сервіс перевіряє кожну пару сусідніх країн у маршруті:
- Польща → Чехія: `POL.borders` містить `CZE` → OK
- Чехія → Австрія: `CZE.borders` містить `AUT` → OK
- Польща → Італія: `POL.borders` НЕ містить `ITA` → помилка 400: "Польща та Італія не мають спільного кордону. Додайте проміжні країни."

Для цього при синхронізації потрібно зберігати `alpha3Code` та `borders` (як список кодів) для кожної країни.

### Валідації при створенні подорожі

- Назва обов'язкова, від 3 до 100 символів
- `startDate` не може бути в минулому
- `endDate` повинна бути після `startDate`
- Мінімум 2 країни у маршруті
- Країни не можуть повторюватись
- Всі `countryId` повинні існувати в базі
- Кожна наступна країна повинна межувати з попередньою (валідація по `borders`)
- Статус при створенні завжди `PLANNED`

### RestCountries API

Не потребує реєстрації.

Країни регіону:
```
GET https://restcountries.com/v3.1/region/europe
```
```json
[
  {
    "name": {"common": "Ukraine"},
    "capital": ["Kyiv"],
    "cca3": "UKR",
    "region": "Europe",
    "population": 44134693,
    "area": 603500.0,
    "languages": {"ukr": "Ukrainian"},
    "latlng": [49.0, 32.0],
    "capitalInfo": {"latlng": [50.43, 30.52]},
    "borders": ["BLR", "HUN", "MDA", "POL", "ROU", "RUS", "SVK"],
    "flags": {"png": "https://flagcdn.com/w320/ua.png"}
  }
]
```

### Open-Meteo API

Не потребує реєстрації.

Поточна погода за координатами столиці (capitalInfo.latlng):
```
GET https://api.open-meteo.com/v1/forecast?latitude=50.43&longitude=30.52&current=temperature_2m,relative_humidity_2m,weather_code
```
```json
{
  "current": {
    "temperature_2m": 22.4,
    "relative_humidity_2m": 55,
    "weather_code": 1
  }
}
```

### Вимоги

**Entities:**
- Country (id, name, alpha3Code, capital, region, population, area, flagUrl, capitalLat, capitalLng, borders)
  - `capitalLat`, `capitalLng` — координати столиці (з поля `capitalInfo.latlng`), використовуються для запиту погоди
  - `borders` — зберігати як список alpha3-кодів (наприклад `@ElementCollection` або JSON-колонка)
- Language (id, name, code) — Many-to-Many з Country
- Trip (id, name, startDate, endDate, status, createdAt)
- TripCountry (id, trip_id, country_id, orderIndex, notes) — One-to-Many від Trip, ManyToOne до Country
- WeatherSnapshot (id, country_id, temperature, humidity, weatherCode, fetchedAt) — One-to-Many від Country

**Liquibase:**
- `001-create-tables.yaml` — всі таблиці + country_language join table
- `002-create-indexes.yaml` — індекси на country.name, trip.status, weather_snapshot.fetched_at
- `003-insert-initial-data.yaml` — тестові дані

**Repository:**
- `findByRegion(String region, Pageable pageable)`
- `findByLanguagesCode(String langCode)`
- `@Query` — країни з населенням > N, сортування за area DESC, з пагінацією
- `@Query(nativeQuery)` — кількість країн по регіонах (GROUP BY)
- `findByStatusOrderByStartDateAsc(TripStatus status, Pageable pageable)`
- `@Query` — подорожі що містять конкретну країну (JOIN через TripCountry)
- `findTop1ByCountryIdOrderByFetchedAtDesc(Long countryId)`

**Service:**
- Синхронізація країн з RestCountries API (upsert Country + Language + borders)
- Пошук країн з фільтрами (Specification або Criteria API)
- Створення подорожі з валідацією маршруту (перевірка borders)
- Отримання подорожі з актуальною погодою (Open-Meteo API для кожної точки)
- Збереження WeatherSnapshot при кожному запиті погоди

**Endpoints:**
- `POST /api/countries/sync?region=Europe`
- `GET /api/countries?name=Ukr&region=Europe&lang=ukr&page=0&size=10`
- `GET /api/countries/{id}/weather`
- `GET /api/countries/stats`
- `POST /api/trips`
- `GET /api/trips/{id}` — з погодою для кожної країни
- `GET /api/trips?status=PLANNED&page=0&size=5`

**Тестування (обов'язково):**
- `@DataJpaTest` для CountryRepository — перевірити кастомні query
- `@DataJpaTest` для TripRepository — перевірити зв'язки через TripCountry
- H2 in-memory (окремий `application-test.yaml`)
- Мінімум 5 тест-методів

### Оцінювання

| Критерій | Бали |
|----------|------|
| JPA entities з відносинами (ManyToMany, OneToMany, TripCountry з orderIndex) | 1 |
| Liquibase міграції (таблиці + індекси + seed data) | 0.5 |
| Repository з @Query + native query + Specification | 1.5 |
| Service з інтеграцією двох API + валідація маршруту по borders | 1 |
| Pagination у всіх list-endpoints | 0.5 |
| @DataJpaTest тести (мінімум 5 методів) | 1 |
| REST endpoints | 0.5 |

---

## Технічний стек

- Spring Boot 3.x / 4.x
- Spring Data JPA + Hibernate
- PostgreSQL
- H2 (тести)
- Liquibase
- RestTemplate або WebClient
- JUnit 5 + @DataJpaTest
