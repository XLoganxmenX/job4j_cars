package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    @Override
    public User create(User user) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    @Override
    public void update(User user) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery("UPDATE User SET login = :fLogin, password = :fPassword WHERE id = :fId")
                        .setParameter("fLogin", user.getLogin())
                        .setParameter("fPassword", user.getPassword())
                        .setParameter("fId", user.getId())
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    @Override
    public void delete(int userId) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.createQuery("DELETE User WHERE id = :fId")
                        .setParameter("fId", userId)
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    /**
     * Список пользователей отсортированных по id.
     * @return список пользователей.
     */
    @Override
    public List<User> findAllOrderById() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User ORDER BY id", User.class);
            List<User> users = query.getResultList();
            session.getTransaction().commit();
            return users;
        }
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    @Override
    public Optional<User> findById(int userId) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User WHERE id = :fId", User.class)
                    .setParameter("fId", userId);
            User user = query.uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    @Override
    public List<User> findByLikeLogin(String key) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User WHERE login LIKE :fLogin", User.class)
                    .setParameter("fLogin",  "%" + key + "%");
            List<User> users = query.getResultList();
            session.getTransaction().commit();
            return users;
        }
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    @Override
    public Optional<User> findByLogin(String login) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User WHERE login = :fLogin", User.class)
                    .setParameter("fLogin", login);
            User user = query.uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }
}
