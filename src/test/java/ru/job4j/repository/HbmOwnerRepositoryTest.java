package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.Main;
import ru.job4j.model.Owner;
import ru.job4j.model.User;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

class HbmOwnerRepositoryTest {
    private static OwnerRepository ownerRepository;
    private static CrudRepository crudRepository;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        SessionFactory sf = context.getBean(SessionFactory.class);
        crudRepository = new CrudRepository(sf);
        ownerRepository = new HbmOwnerRepository(crudRepository);
    }

    @AfterEach
    public void deleteAll() throws Exception {
        crudRepository.run("DELETE Owner", Map.of());
        crudRepository.run("DELETE User", Map.of());
    }

    @Test
    public void whenFindOwnerByIdThenGetOwnerOptional() throws Exception {
        var user = new User(0, "user", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var expectedOwner = new Owner(0, "Owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(expectedOwner));
        var actualOwner = ownerRepository.findById(expectedOwner.getId()).get();
        assertThat(actualOwner).usingRecursiveComparison().isEqualTo(expectedOwner);
    }

    @Test
    public void whenFindNotExistOwnerByIdThenGetEmptyOptional() throws Exception {
        var actualOwner = ownerRepository.findById(0);
        assertThat(actualOwner).isEmpty();
    }

    @Test
    public void whenFindAllThenGetOwnersList() throws Exception {
        var user = new User(0, "user", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var owner1 = new Owner(0, "owner1", user);
        var owner2 = new Owner(0, "owner2", user);
        var owner3 = new Owner(0, "owner3", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner1));
        crudRepository.run((Consumer<Session>) session -> session.persist(owner2));
        crudRepository.run((Consumer<Session>) session -> session.persist(owner3));
        var expectedOwners = List.of(owner1, owner2, owner3);

        var actualOwners = ownerRepository.findAll();
        assertThat(actualOwners).usingRecursiveComparison().isEqualTo(expectedOwners);
    }

    @Test
    public void whenFindAllNotExistThenGetEmptyList() throws Exception {
        var actualOwners = ownerRepository.findAll();
        assertThat(actualOwners).isEmpty();
    }

}