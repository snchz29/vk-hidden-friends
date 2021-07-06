# vk-hidden-friends

## Настройка

1. Нужно созадать файл `src/main/resources/app.properties` и внести в него следующие данные:

```
APP_ID="ID приложения"
SECURE_KEY="Защищённый ключ"
SERVICE_TOKEN="Сервисный ключ доступа"
ACCESS_TOKEN="Ключ доступа"
```

Чтобы получить access token необходимо перейти по указанной ниже ссылке, указав вместо "ID_приложения" Ваш ID приложения.

https://oauth.vk.com/authorize?client_id=ID_приложения&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,groups,photos&response_type=token&v=5.120

2. Нужно создать и подключить базу данных PostgreSQL.
Для этого можно воспользоваться следующим SQL-кодом.
   
```postgresql
CREATE TABLE public.person
(
    vk_id integer NOT NULL,
    first_name character varying(20) COLLATE pg_catalog."default",
    last_name character varying(40) COLLATE pg_catalog."default",
    photo_uri character varying(255) COLLATE pg_catalog."default",
    created_on timestamp without time zone,
    friends integer[],
    CONSTRAINT person_pkey PRIMARY KEY (vk_id)
)

    TABLESPACE pg_default;

ALTER TABLE public.person
    OWNER to postgres;
```

После создания таблиц нужно указать Spring как подключиться к базе данных.
Для этого следует перейти в файл `src/main/java/ru/snchz29/config/SpringConfig.java` и в бине `dataSource` указать Ваши данные для подключения (URL, Username, Password).


## Описание

Программа помогает найти скрытых друзей в VK. Для ее использования нужно в `Main` в вызов функции `FriendshipGraphHandler::findHiddenFriends` передать ID пользователя, от которого будет строиться граф.
Граф включает в себя друзей до 3-го рукопожатия, но не всех.
При повторных использованиях программы граф будет включать в себя все больше друзей, используя сохраненные в базе данных записи.
Устаревшие записи (созданные более суток назад) удаляются, что сокращает вероятность ошибок первого рода.