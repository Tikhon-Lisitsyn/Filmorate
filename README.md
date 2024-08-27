# ТЗ 12
## Ссылка на [диаграмму](https://github.com/Tikhon-Lisitsyn/Filmorate/blob/master/diagram.jpg)
## Команды для:
### Поиска общих друзей: 
SELECT uf1.friend_id
FROM user_friends uf1
JOIN user_friends uf2 ON uf1.friend_id = uf2.friend_id
WHERE uf1.user_id = user1_id
  AND uf2.user_id = user2_id;
### Получения топ-10 фильмов:
SELECT film_id, COUNT(user_id) AS like_count
FROM user_likes
GROUP BY film_id
ORDER BY like_count DESC
LIMIT 10;

