# документация к auto_oop
📍 Общая структура Приложение построено с использованием Spring Boot. Реализует:

Авторизацию с JWT.

CRUD для автомобилей (Auto).

Управление пользователями.

Обработку исключений.

REST API с контроллерами.

📦 Контроллеры

AuthController

Маршруты: /auth/**

Назначение: Обработка логина и генерации JWT-токенов.

Методы:

java

Добавить

Редактировать

@PostMapping("/login")

public ResponseEntity<?> login(@RequestBody AuthRequest authRequest)

Описание: Принимает имя пользователя и пароль, возвращает JWT-токен.

Параметры: AuthRequest {username, password}

Ответ: JSON {token: "..."} или ошибка.

UserController

Маршруты: /user/**

Назначение: Получение информации о текущем пользователе.

Методы:

java

Добавить

Редактировать

@GetMapping("/info")

public ResponseEntity<?> userInfo()

Описание: Возвращает имя пользователя, роль и другие данные текущего авторизованного пользователя.

Ответ: JSON с информацией о пользователе.

CrudController

Маршруты: /crud/**

Назначение: CRUD-операции для объектов Auto.

Методы:

java

Добавить

Редактировать

@GetMapping

public List getAll()

Получить список всех автомобилей.

java

Добавить

Редактировать

@PostMapping

public Flower create(@RequestBody Auto auto)

Создать автомобиль.

java

Добавить

Редактировать

@PutMapping("/{id}")

public Auto update(@PathVariable Long id, @RequestBody Auto auto)

Обновить автомобиль по ID.

java

Добавить

Редактировать

@DeleteMapping("/{id}")

public void delete(@PathVariable Long id)

Удалить автомобиль по ID.

TestController, MainController, ViewController

Предположительно связаны с UI или тестированием представлений (MVC, шаблоны).

Могут использоваться для вспомогательных или учебных целей.

🧩 Модели

User

Пользователь системы.

java

Добавить

Редактировать

private Long id;

private String username;

private String password;

private Role role;

Role

java

Добавить

Редактировать

public enum Role { USER, ADMIN }

Flower

java

Добавить

Редактировать

private Long id;

private String name;

private Double price;

AuthRequest

DTO для логина.

java

Добавить

Редактировать

private String username;

private String password;

🛡 Безопасность JwtUtil

Утилита для создания и валидации JWT-токенов.

java

Добавить

Редактировать

public String generateToken(String username);

public boolean validateToken(String token);

public String extractUsername(String token);

SecurityConfig

Конфигурирует Spring Security:

Устанавливает JwtFilter

Разрешает аутентификацию на /auth/login

Требует авторизацию для остальных маршрутов

🔧 Сервисы

CustomUserDetailsService

Реализует загрузку пользователей из БД по имени.

