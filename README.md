# Приложение "TODO список"

## Описание проекта

TODO список - это приложение для управления списком заданий.</br>

Сервис позволяет:
1. Создать новое задание.
2. Просмотреть список всех заданий.
3. Отредактировать задание.
4. Сменить статус задания на "Выполнено".
5. Удалить задание.

### Технологии используемые в проекте:
- Java 17
- Spring boot 2.7.3
- Hibernate 5.6.11.Final
- Tomcat
- Thymeleaf 3.0.15
- Bootstrap
- Liquibase 4.15.0
- PostgreSQL 42.2.9
- H2 2.2.220
- Junit5
- Jacoco 0.8.7
- AssertJ
- Mockito
- Docker
- Docker Compose

### Требования к окружению:
- Интернет
- Docker
- Docker Compose

# Запуск

1. Установите и запустите Docker и Docker Compose<br><br>

2. Склонируйте репозиторий
```
git clone https://github.com/hrodvar-one/job4j_todo
```

3. Перейдите в папку куда склонировали проект
```
cd job4j_todo
```

4. В командной строке из папки выполните команду:
```
docker-compose up --build
```
Дождитесь развёртывания Docker контейнеров.
Запущенный проект будет доступен по адресу: [http://localhost:8080](http://localhost:8080)

Взаимодействие с приложением:

1. Страница входа

![Страница входа](screenshots/login_page.png)

2. Страница регистрации

![Страница регистрации](screenshots/registration_page.png)

3. Главная страница

![Главная страница](screenshots/list_page.png)

4. Выполненные задания

![Выполненные задания](screenshots/completed_task_page.png)

5. Невыполненные задания

![Невыполненные задания](screenshots/uncompleted_task_page.png)

6. Просмотр выбранного задания

![Просмотр выбранного задания](screenshots/preview_task_page.png)

7. Добавление нового задания

![Добавление нового задания](screenshots/add_task_page.png)

8. Редактирование выбранного задания

![Редактирование выбранного задания](screenshots/edit_task_page.png)

## Контакты

Telegram: @alex_dev_java