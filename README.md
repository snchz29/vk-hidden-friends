# vk-hidden-friends

## Описание

Веб-приложение помогает найти скрытых друзей в VK.
На стратовой странице нужно ввести ID пользователя и нажать RUN.
После этого программа выведет найденных скрытых друзей.
Граф включает в себя друзей до 3-го рукопожатия, но он не полный, так как если бы рассматривались все пользователи, то приложение работало бы неприлично долго.
При повторных использованиях программы граф будет включать в себя все больше друзей, используя сохраненные в базе данных записи.
Устаревшие записи (созданные более суток назад) удаляются, что сокращает вероятность ошибок первого рода.

## Настройка

1. Нужно созадать файл `src/main/resources/vkAuth.properties` и внести в него следующие данные:

```
APP_ID="ID приложения"
SECURE_KEY="Защищённый ключ"
SERVICE_TOKEN="Сервисный ключ доступа"
```

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

