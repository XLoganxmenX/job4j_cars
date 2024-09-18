package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.Main;
import ru.job4j.model.User;

import static org.assertj.core.api.Assertions.*;

class HbmUserRepositoryTest {
    private static CrudRepository crudRepository;
    private static UserRepository userRepository;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        SessionFactory sf = context.getBean(SessionFactory.class);
        crudRepository = new CrudRepository(sf);
        userRepository = new HbmUserRepository(crudRepository);
    }

    @AfterEach
    public void deleteAll() {
        userRepository.findAllOrderById().forEach(user -> userRepository.delete(user.getId()));
    }

    @Test
    public void whenSaveUserAndFindById() throws Exception {
        var expectedUser = userRepository.create(new User(0, "login", "password"));
        var actualUser = userRepository.findById(expectedUser.getId()).get();
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    public void whenDeleteUser() {
        var savedUser = userRepository.create(new User(0, "login", "password"));
        userRepository.delete(savedUser.getId());
        var foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isEmpty();
        assertThat(userRepository.findAllOrderById()).isEmpty();
    }
}