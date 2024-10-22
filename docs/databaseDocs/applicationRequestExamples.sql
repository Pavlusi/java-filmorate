--FILM:
--Получить список всех фильмов
SELECT * FROM films;
--Получить фильм по id
SELECT * FROM films WHERE id =?;
--Добавление нового фильма
INSERT INTO films (name, description, release_date, duration) VALUES ('Film One', 'Description for film one', '2023-01-01', 120);
--Лайк фильма
INSERT INTO film_likes(user_id, film_id)  VALUES(?,?);
--Удаление лайка
DELETE FROM film_likes WHERE user_id = ? AND film_id = ?;
--Получение списка фильмов сортированых по рейтингу, от большего к меньшему и ограниченых по колличеству.
SELECT
    f.id,
    f.name,
    f.description,
    f.release_date,
    f.duration,
    COUNT(fl.film_id) AS count_likes
FROM
    films AS f
        LEFT JOIN
    film_likes fl ON f.id = fl.film_id
GROUP BY
    f.id, f.name, f.description, f.release_date, f.duration
ORDER BY
    count_likes DESC
LIMIT ?;

--USER

--Полуить всех юзеров
SELECT * FROM users;
--Получить юзера по id
SELECT * FROM  users WHERE id = ?;
--Добавить нового юзера
INSERT INTO users (email, login, name, birthday) VALUES ('user1@example.com', 'user1', 'User One', '1990-01-01');
--Добавление в друзья
INSERT INTO user_friends(user_id, friend_id, status) VALUES (?,?);
--Удаление из друзей
DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?;
--Получение списка всех друзей юзера
SELECT u2.id,
       u2.email,
       u2.login,
       u2.name,
       u2.birthday
FROM users AS u
LEFT JOIN user_friends AS uf ON u.id = uf.user_id
LEFT JOIN users AS u2 ON uf.friend_id = u2.id
WHERE user_id =?;
--Получение списка общих друзей с другим юзером
SELECT uf1.friend_id AS id,
       u.email,
       u.login,
       u.name,
       u.birthday
FROM user_friends uf1
         JOIN user_friends uf2 ON uf1.friend_id = uf2.friend_id
         JOIN users u ON uf1.friend_id = u.id
WHERE uf1.user_id = ? AND uf2.user_id = ?;


