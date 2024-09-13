package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Car;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class HbmCarRepository implements CarRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmCarRepository.class);
    private final CrudRepository crudRepository;

    @Override
    public Car save(Car car) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.persist(car));
        } catch (Exception e) {
            LOGGER.error("Exception on save Car", e);
        }
        return car;
    }

    @Override
    public boolean update(Car car) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.update(car));
            return crudRepository.run("""
                    UPDATE Car SET name = :fName, engine_id = :fEngineId
                    WHERE id = :fId
                    """,
                    Map.of("fName", car.getName(),
                            "fEngineId", car.getEngine().getId(),
                            "fId", car.getId()));
        } catch (Exception e) {
            LOGGER.error("Exception on update Car", e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            return crudRepository.run("DELETE Car WHERE id = :fId", Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on delete Car", e);
        }
        return false;
    }

    @Override
    public Optional<Car> findById(int id) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT c from Car c JOIN FETCH c.engine JOIN FETCH c.owners WHERE c.id = :fId",
                    Car.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOGGER.error("Exception on find Car ById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Car> findAllOrderById() {
        try {
            return crudRepository.query(
                    "SELECT DISTINCT c from Car c JOIN FETCH c.engine JOIN FETCH c.owners ORDER BY c.id",
                    Car.class
            );
        } catch (Exception e) {
            LOGGER.error("Exception on findAll Car orderById", e);
        }
        return Collections.emptyList();
    }
}
