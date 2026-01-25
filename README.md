# BloodLink - Система управления донорством крови

## Описание

BloodLink — веб-приложение для управления запросами и резервами донорской крови между медицинскими учреждениями и банками крови.

### Основные возможности

- **Медицинские учреждения**: создание запросов на кровь, отслеживание статуса
- **Банки крови**: управление запасами, обработка запросов
- **Администраторы**: управление организациями, пользователями и заявками на регистрацию

## Архитектура

### Технологии

**Backend:**
- Java 21
- Spring Boot 3.3.5
- Spring Security (session-based authentication)
- Spring Data JPA
- PostgreSQL 16
- Flyway (миграции БД)
- MapStruct (маппинг DTO)

**Frontend:**
- Vue.js 3
- Vite
- Tailwind CSS
- Axios

**Инфраструктура:**
- Docker & Docker Compose
- Nginx (reverse proxy)

### Структура проекта

```
BloodDonor/
├── backend/
│   ├── src/main/java/com/bloodlink/
│   │   ├── controllers/    # REST контроллеры
│   │   ├── service/        # Бизнес-логика
│   │   ├── repositories/   # Доступ к данным (JPA)
│   │   ├── entities/       # JPA сущности
│   │   ├── dto/
│   │   │   ├── request/    # Входящие DTO
│   │   │   ├── response/   # Исходящие DTO
│   │   │   └── mapper/     # MapStruct маперы
│   │   ├── configs/        # Конфигурация Spring
│   │   ├── security/       # Конфигурация безопасности
│   │   ├── exceptions/     # Кастомные исключения
│   │   └── validators/     # Валидаторы
│   └── src/main/resources/
│       ├── db/migration/   # Flyway миграции
│       └── application.properties
├── frontend/
│   ├── src/
│   │   ├── api/            # Централизованные API клиенты
│   │   ├── components/     # Vue компоненты
│   │   ├── views/          # Страницы (views)
│   │   ├── routes/         # Vue Router (lazy loading)
│   │   ├── js/             # Утилиты
│   │   └── assets/         # Стили (Tailwind)
│   └── vite.config.js
├── documentation/          # Документация проекта
└── docker-compose.yml
```

## Запуск

### Docker (рекомендуется)

1. Создать файл `.env`:
```env
DB_USERNAME=admin
DB_PASSWORD=your_secure_password
DB_NAME=BloodLink
```

2. Запустить:
```bash
docker compose up --build
```

3. Открыть http://localhost

### Локальная разработка

**Требования:**
- Java 21
- Node.js 18+
- PostgreSQL 16

**Backend:**
```bash
cd backend
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```

## API Документация

### Swagger UI

После запуска доступен по адресу: http://localhost:8080/swagger-ui.html

### Основные endpoints

| Метод | URL | Описание | Роль |
|-------|-----|----------|------|
| POST | /api/auth/login | Авторизация | - |
| POST | /api/logout | Выход | * |
| GET | /api/users/me | Текущий пользователь | * |
| GET | /api/blood-requests/bank | Запросы для банка | BANK_EMPLOYEE |
| GET | /api/blood-requests/medical | Запросы медучреждения | MEDICAL_EMPLOYEE |
| POST | /api/blood-requests | Создать запрос | MEDICAL_EMPLOYEE |
| PATCH | /api/blood-requests/{id}/accept | Принять запрос | BANK_EMPLOYEE |
| GET | /api/blood-units | Партии крови | BANK_EMPLOYEE |
| POST | /api/blood-units | Добавить партию | BANK_EMPLOYEE |
| GET | /api/organizations | Список организаций | * |
| GET | /api/users | Список пользователей | ADMIN |
| GET | /api/registration-requests | Заявки на регистрацию | ADMIN |

## Роли пользователей

| Роль | Описание |
|------|----------|
| `ADMIN` | Администратор организации — управление пользователями, организациями, заявками на регистрацию |
| `BANK_EMPLOYEE` | Сотрудник банка крови — управление запасами крови, обработка запросов |
| `MEDICAL_EMPLOYEE` | Сотрудник медучреждения — создание запросов на кровь |

## База данных

### ER-диаграмма

См. `documentation/databaseDocs/diagrams/`

### Основные таблицы

| Таблица | Описание |
|---------|----------|
| `users` | Пользователи системы |
| `organizations` | Организации (банки крови, медучреждения) |
| `blood_units` | Партии крови в банках |
| `blood_requests` | Запросы на кровь |
| `blood_reserves` | Связь запросов с банками крови |
| `notifications` | Уведомления пользователей |
| `registration_requests` | Заявки на регистрацию |

## Тестовые данные

После запуска доступны тестовые пользователи:

| Email | Пароль | Роль | Организация |
|-------|--------|------|-------------|
| admin@hospital.com | admin | ADMIN | Городская больница №1 |
| medic@hospital.com | medic | MEDICAL_EMPLOYEE | Городская больница №1 |
| banker@bloodbank.com | banker | BANK_EMPLOYEE | Центральный банк крови |

## Разработка

### Структура коммитов

Используется [Conventional Commits](https://www.conventionalcommits.org/):
- `feat:` — новая функциональность
- `fix:` — исправление бага
- `refactor:` — рефакторинг
- `docs:` — документация

### Сборка

```bash
# Backend
cd backend && ./gradlew build

# Frontend
cd frontend && npm run build
```

## Дополнительная документация

- [Техническое задание](Task.md)
- [Source Requirements Specification](documentation/Srs.docx)
- [Документация БД](documentation/databaseDocs)
  * [Интерфейс с входящими заявками от банков крови](images/bloodRequestsBankPage.png). Страница содержит все запросы, 
которые когда-либо поступали в банк сотрудника. Есть удобные селекторы для быстрого поиска нужного запроса. 
Также есть возможность принять или отклонить заявку, с указанием причины отклонения. Есть возможность просмотра подробной информации о заявке.
  

* **Прочее**
 
  * У каждого сотрудника есть панель с [уведомлениями](images/notifications.png).
  * **[Детали]** В приложении есть *запланированная задача*, которая проходится по таблице BloodUnits раз в 24 часа и удаляет просрочные партии крови, уведомляя об этом сотрудников банка
(да, у партий крови есть свой срок годности).
  * **[Детали]** В приложении есть триггер, который при каждой *DML операции* обновляет общее количество крови определенного вида,
которое хранится в другой таблице, тем самым при отображении общего количества крови определенного вида приложение
не проходит по всей таблице BloodUnits и не складывает партии крови, а просто берет информацию из дополнительной таблицы
  * **[Детали]** В приложении предусмотренно несколько индексов для оптимизации запросов

    
Подробно ознакомиться с организацией базы данных (таблицы, триггеры, индексы, представления) можно в [этом разделе](backend/db). Здесь представлены скрипты, которые используются для управления базой.

    spring.jpa.hibernate.ddl-auto=none # Hibernate базой не управляет


## Техническое описание

Проект не использует каких-то серьезный enterprise технологий. В них не было необходимости, но при желании проект можно 
расширять и добавлять массу нового функционала.

### Backend

* Написан на *Spring*
* База данных *PostgreSQL*
* Для развертки базы данных используется *Docker*.
* Проект использует лишь основные "концепции\фреймворки" *Spring* среди которых можно отметить:
    * *SpringMVC*
    * *SpringSecurity*
    * *Java Transaction API*
    * *Spring DATA (Hibernate)*
    * *Jakarta Validation*
* Для большинства запросов используется *Page* и *Specification* интерфейсы (ну это уже, конечно, мелочи)

### Frontend

* Написан на Vue.
* Для актуализации данных используется *polling* метод.
* Для более компактной кастомизации фронта использую *TailWind* css-фреймворк.

## Инструкция по развертыванию

* Убедитесь, что у вас установлены
  * Java 23
  * Docker 27.4.0
  * Node.js 22.14.0
* Приложение доступно по адресу localhost:5353 - это адрес фронт сервера. Бэкэнд часть работает на порту 8080.
* Для запуска компилируем бэк
  1. ./gradlew build
  2. ./gradlew bootRun
* Фронт запускается примерно так
  1. npm install
  2. npm run dev