package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmEngineRepository.class);
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    @Override
    public Optional<User> create(User user) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            LOGGER.error("Exception on save User", e);
        }
        return Optional.empty();
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    @Override
    public void update(User user) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.merge(user));
        } catch (Exception e) {
            LOGGER.error("Exception on update User", e);
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    @Override
    public void delete(int userId) {
        try {
            crudRepository.run(
                    "DELETE FROM User WHERE id = :fId",
                    Map.of("fId", userId)
            );
        } catch (Exception e) {
            LOGGER.error("Exception on delete User", e);
        }
    }

    /**
     * Список пользователей отсортированных по id.
     * @return список пользователей.
     */
    @Override
    public List<User> findAllOrderById() {
        try {
            return crudRepository.query("from User order by id asc", User.class);
        } catch (Exception e) {
            LOGGER.error("Exception on findAll User", e);
        }
        return Collections.emptyList();
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    @Override
    public Optional<User> findById(int userId) {
        try {
            return crudRepository.optional(
                    "from User where id = :fId", User.class,
                    Map.of("fId", userId)
            );
        } catch (Exception e) {
            LOGGER.error("Exception on findById User", e);
        }
        return Optional.empty();
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    @Override
    public List<User> findByLikeLogin(String key) {
        try {
            return crudRepository.query(
                    "from User where login like :fKey", User.class,
                    Map.of("fKey", "%" + key + "%")
            );
        } catch (Exception e) {
            LOGGER.error("Exception on findByLikeLogin User", e);
        }
        return Collections.emptyList();
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    @Override
    public Optional<User> findByLogin(String login) {
        try {
            return crudRepository.optional(
                    "from User where login = :fLogin", User.class,
                    Map.of("fLogin", login)
            );
        } catch (Exception e) {
            LOGGER.error("Exception on findByLogin User", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try {
            return crudRepository.optional("FROM User WHERE login = :fLogin AND password = :fPassword", User.class,
                    Map.of("fLogin", login,
                            "fPassword", password
                    ));
        } catch (Exception e) {
            LOGGER.error("Exception on find User ByLoginAndPassword", e);
        }
        return Optional.empty();
    }
}
