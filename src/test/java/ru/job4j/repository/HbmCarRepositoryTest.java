package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.Main;
import ru.job4j.model.*;

import java.util.*;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

class HbmCarRepositoryTest {
    private static CarRepository carRepository;
    private static CrudRepository crudRepository;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        SessionFactory sf = context.getBean(SessionFactory.class);
        crudRepository = new CrudRepository(sf);
        carRepository = new HbmCarRepository(crudRepository);
    }

    @AfterEach
    public void deleteAll() throws Exception {
        crudRepository.run("DELETE Car", Map.of());
        crudRepository.run("DELETE Engine", Map.of());
        crudRepository.run("DELETE Owner", Map.of());
        crudRepository.run("DELETE User", Map.of());
        crudRepository.run("DELETE CarModel", Map.of());
    }

    @Test
    public void whenSaveAndFindById() throws Exception {
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var user = new User(0, "user", "login", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));
        var carModel = new CarModel(0, "model");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel));
        var car = new Car(0, "car", engine, carModel, Set.of(owner));
        var expectedCar = carRepository.save(car);
        var actualCar = carRepository.findById(expectedCar.getId()).get();

        assertThat(actualCar).isEqualTo(expectedCar);
    }

    @Test
    public void whenUpdateThenGetSame() throws Exception {
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var user = new User(0, "user", "login", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));
        var carModel = new CarModel(0, "model");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel));
        var expectedCar = carRepository.save(new Car(0, "car", engine, carModel, Set.of(owner)));
        expectedCar.setName("New Car");

        var isUpdated = carRepository.update(expectedCar);
        var actualCar = carRepository.findById(expectedCar.getId()).get();

        assertThat(actualCar).isEqualTo(expectedCar);
        assertThat(isUpdated).isTrue();
    }

    @Test
    public void whenFindAll() throws Exception {
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var user = new User(0, "user", "test", "test");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));
        var carModel = new CarModel(0, "model");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel));
        var expectedCar1 = carRepository.save(new Car(0, "car1", engine, carModel, Set.of(owner)));
        var expectedCar2 = carRepository.save(new Car(0, "car2", engine, carModel, Set.of(owner)));
        var expectedCar3 = carRepository.save(new Car(0, "car3", engine, carModel, Set.of(owner)));
        var cars = List.of(expectedCar1, expectedCar2, expectedCar3);

        var actualCars = carRepository.findAllOrderById();

        assertThat(actualCars).isEqualTo(cars);
    }

    @Test
    public void whenDeleteThenNotFound() throws Exception {
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var user = new User(0, "user", "login", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));
        var carModel = new CarModel(0, "model");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel));
        var expectedCar = carRepository.save(new Car(0, "car", engine, carModel, Set.of(owner)));

        var isDeleted = carRepository.delete(expectedCar.getId());
        var actualCar = carRepository.findById(expectedCar.getId());

        assertThat(actualCar).isEmpty();
        assertThat(isDeleted).isTrue();
    }

    @Test
    public void whenDeleteNotExistThenGetFalse() {
        var isDeleted = carRepository.delete(0);
        assertThat(isDeleted).isFalse();
    }
}