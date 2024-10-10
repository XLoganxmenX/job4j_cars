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

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

class HbmPostRepositoryTest {
    private static PostRepository postRepository;
    private static CrudRepository crudRepository;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        SessionFactory sf = context.getBean(SessionFactory.class);
        crudRepository = new CrudRepository(sf);
        postRepository = new HbmPostRepository(crudRepository);
    }

    @AfterEach
    public void deleteAll() throws Exception {
        crudRepository.run("DELETE PriceHistory", Map.of());
        crudRepository.run("DELETE Post", Map.of());
        crudRepository.run("DELETE Car", Map.of());
        crudRepository.run("DELETE Engine", Map.of());
        crudRepository.run("DELETE Owner", Map.of());
        crudRepository.run("DELETE User", Map.of());
        crudRepository.run("DELETE CarModel", Map.of());
        crudRepository.run("DELETE File", Map.of());
    }

    @Test
    public void whenSavePostAndGetById() throws Exception {
        var user = new User(0, "name", "user", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var priceHistory = new PriceHistory(0, BigInteger.valueOf(100), BigInteger.valueOf(200), LocalDateTime.now());
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory));
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));
        var carModel = new CarModel(0, "model");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel));
        var car = new Car(0, "car", engine, carModel, Set.of(owner));
        crudRepository.run((Consumer<Session>) session -> session.persist(car));
        var file = new File(0, "file", "root");
        crudRepository.run((Consumer<Session>) session -> session.persist(file));

        var post = postRepository.save(
                new Post(0, "post", LocalDateTime.now(), user,
                        List.of(priceHistory), List.of(user), car, List.of(file), false)
        );
        var actualPost = postRepository.findById(post.getId());

        assertThat(actualPost).isEqualTo(Optional.of(post));
    }

    @Test
    public void whenFindAll() throws Exception {
        var user = new User(0, "name", "user", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var priceHistory1 = new PriceHistory(0, BigInteger.valueOf(100), BigInteger.valueOf(200), LocalDateTime.now());
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory1));
        var priceHistory2 = new PriceHistory(0, BigInteger.valueOf(300), BigInteger.valueOf(400), LocalDateTime.now());
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory2));
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));
        var carModel = new CarModel(0, "model");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel));
        var car = new Car(0, "car", engine, carModel, Set.of(owner));
        crudRepository.run((Consumer<Session>) session -> session.persist(car));
        var file1 = new File(0, "file1", "root1");
        crudRepository.run((Consumer<Session>) session -> session.persist(file1));

        var post1 = new Post(0, "post1", LocalDateTime.now(), user,
                List.of(priceHistory1), List.of(user), car, List.of(file1), false
        );
        postRepository.save(post1);
        var post2 = new Post(0, "post2", LocalDateTime.now(), user,
                List.of(priceHistory2), List.of(user), car, List.of(), false
        );
        postRepository.save(post2);
        var expectedPosts = List.of(post1, post2);

        var actualPosts = postRepository.findAll();

        assertThat(actualPosts).isEqualTo(expectedPosts);
    }

    @Test
    public void whenFindAllCreatedToday() throws Exception {
        var user = new User(0, "name", "user", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var priceHistory1 = new PriceHistory(
                0, BigInteger.valueOf(100), BigInteger.valueOf(200), LocalDateTime.now());
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory1));
        var priceHistory2 = new PriceHistory(
                0, BigInteger.valueOf(300), BigInteger.valueOf(400), LocalDateTime.now().minusDays(3));
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory2));
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));
        var carModel = new CarModel(0, "model");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel));
        var car = new Car(0, "car", engine, carModel, Set.of(owner));
        crudRepository.run((Consumer<Session>) session -> session.persist(car));
        var file1 = new File(0, "file1", "root1");
        crudRepository.run((Consumer<Session>) session -> session.persist(file1));

        var post1 = new Post(0, "post1", LocalDateTime.now(), user,
                List.of(priceHistory1), List.of(user), car, List.of(file1), false
        );
        postRepository.save(post1);
        var post2 = new Post(0, "post2", LocalDateTime.now().minusDays(3), user,
                List.of(priceHistory2), List.of(user), car, List.of(), false
        );
        postRepository.save(post2);
        var expectedPosts = List.of(post1);

        var actualPosts = postRepository.findAllCreatedToday();

        assertThat(actualPosts).isEqualTo(expectedPosts);
    }

    @Test
    public void whenFindByCarModel() throws Exception {
        var user = new User(0, "name", "user", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var priceHistory1 = new PriceHistory(0, BigInteger.valueOf(100), BigInteger.valueOf(200), LocalDateTime.now());
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory1));
        var priceHistory2 = new PriceHistory(0, BigInteger.valueOf(300), BigInteger.valueOf(400), LocalDateTime.now());
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory2));
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));

        var carModel1 = new CarModel(0, "model1");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel1));
        var carModel2 = new CarModel(0, "model2");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel2));

        var car1 = new Car(0, "car1", engine, carModel1, Set.of(owner));
        crudRepository.run((Consumer<Session>) session -> session.persist(car1));
        var car2 = new Car(0, "car2", engine, carModel2, Set.of(owner));
        crudRepository.run((Consumer<Session>) session -> session.persist(car2));

        var file1 = new File(0, "file1", "root1");
        crudRepository.run((Consumer<Session>) session -> session.persist(file1));
        var file2 = new File(0, "file2", "root2");
        crudRepository.run((Consumer<Session>) session -> session.persist(file2));

        var post1 = new Post(0, "post1", LocalDateTime.now(), user,
                List.of(priceHistory1), List.of(user), car1, List.of(file1), false
        );
        postRepository.save(post1);
        var post2 = new Post(0, "post2", LocalDateTime.now(), user,
                List.of(priceHistory2), List.of(user), car2, List.of(), false
        );
        postRepository.save(post2);
        var expectedPosts = List.of(post1);

        var actualPosts = postRepository.findByCarModel(carModel1);

        assertThat(actualPosts).isEqualTo(expectedPosts);
    }

    @Test
    public void whenFindAllWithFiles() throws Exception {
        var user = new User(0, "name", "user", "password");
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var priceHistory1 = new PriceHistory(0, BigInteger.valueOf(100), BigInteger.valueOf(200), LocalDateTime.now());
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory1));
        var priceHistory2 = new PriceHistory(0, BigInteger.valueOf(300), BigInteger.valueOf(400), LocalDateTime.now());
        crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory2));
        var engine = new Engine(0, "engine");
        crudRepository.run((Consumer<Session>) session -> session.persist(engine));
        var owner = new Owner(0, "owner", user);
        crudRepository.run((Consumer<Session>) session -> session.persist(owner));

        var carModel1 = new CarModel(0, "model1");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel1));
        var carModel2 = new CarModel(0, "model2");
        crudRepository.run((Consumer<Session>) session -> session.persist(carModel2));

        var car1 = new Car(0, "car1", engine, carModel1, Set.of(owner));
        crudRepository.run((Consumer<Session>) session -> session.persist(car1));
        var car2 = new Car(0, "car2", engine, carModel2, Set.of(owner));
        crudRepository.run((Consumer<Session>) session -> session.persist(car2));

        var file1 = new File(0, "file1", "root1");
        crudRepository.run((Consumer<Session>) session -> session.persist(file1));
        var file2 = new File(0, "file2", "root2");
        crudRepository.run((Consumer<Session>) session -> session.persist(file2));

        var post1 = new Post(0, "post1", LocalDateTime.now(), user,
                List.of(priceHistory1), List.of(user), car1, List.of(file1), false
        );
        postRepository.save(post1);
        var post2 = new Post(0, "post2", LocalDateTime.now(), user,
                List.of(priceHistory2), List.of(user), car2, List.of(), false
        );
        postRepository.save(post2);
        var expectedPosts = List.of(post1);

        var actualPosts = postRepository.findAllWithFiles();

        assertThat(actualPosts).isEqualTo(expectedPosts);
    }
}