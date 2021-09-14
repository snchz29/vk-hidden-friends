# vk-hidden-friends

## Описание

Веб-приложение позволяет найти скрытых друзей в VK.
На стратовой странице нужно ввести ID пользователя и нажать RUN.
После этого программа выведет найденных скрытых друзей.
Граф включает в себя друзей до 3-го рукопожатия, но он не полный, так как если бы рассматривались все пользователи, то приложение работало бы неприлично долго.
При повторных использованиях программы граф будет включать в себя все больше друзей, используя сохраненные в базе данных записи.
Устаревшие записи (созданные более суток назад) удаляются, что сокращает вероятность ошибок первого рода.

## Настройка

1. Нужно созадать файл `src/main/resources/vkAuth.properties` и внести в него следующие данные:

```
auth.APP_ID="ID приложения"
auth.SECURE_KEY="Защищённый ключ"
auth.SERVICE_TOKEN="Сервисный ключ доступа"
auth.loginLink="Ссылка для авторизации"
test.login="Логин для тестирования"
test.password="Пароль для тестирования"
```

2. Нужно создать и подключить базу данных PostgreSQL.
Никаких таблиц создавать не нужно, при необходимости все создастся автоматически.
