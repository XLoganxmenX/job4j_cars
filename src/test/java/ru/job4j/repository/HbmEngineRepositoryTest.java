package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.Main;
import ru.job4j.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

class HbmEngineRepositoryTest {

    private static EngineRepository engineRepository;
    private static CrudRepository crudRepository;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        SessionFactory sf = context.getBean(SessionFactory.class);
        crudRepository = new CrudRepository(sf);
        engineRepository = new HbmEngineRepository(crudRepository);
    }

    @BeforeEach
    public void deleteAll() throws Exception {
        crudRepository.run("DELETE Engine", Map.of());
    }

    @Test
    public void whenFindEngineByIdThenGetEngineOptional() throws Exception {
        var expectedEngine = new Engine(0, "Engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(expectedEngine));
        var actualEngine = engineRepository.findById(expectedEngine.getId()).get();
        assertThat(actualEngine).usingRecursiveComparison().isEqualTo(expectedEngine);
    }

    @Test
    public void whenFindNotExistEngineByIdThenGetEmptyOptional() throws Exception {
        var actualEngine = engineRepository.findById(0);
        assertThat(actualEngine).isEmpty();
    }

    @Test
    public void whenFindAllThenGetEngineList() throws Exception {
        var engine1 = new Engine(0, "engine1");
        var engine2 = new Engine(0, "engine2");
        var engine3 = new Engine(0, "engine3");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine1));
        crudRepository.run((Consumer<Session>) session -> session.persist(engine2));
        crudRepository.run((Consumer<Session>) session -> session.persist(engine3));
        var expectedEngines = List.of(engine1, engine2, engine3);

        var actualEngines = engineRepository.findAll();
        assertThat(actualEngines).usingRecursiveComparison().isEqualTo(expectedEngines);
    }

    @Test
    public void whenFindAllNotExistThenGetEmptyList() throws Exception {
        var actualEngines = engineRepository.findAll();
        assertThat(actualEngines).isEmpty();
    }
}