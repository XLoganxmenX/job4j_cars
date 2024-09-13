package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Engine;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmEngineRepository implements EngineRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmEngineRepository.class);
    private final CrudRepository crudRepository;

    @Override
    public Optional<Engine> findById(int id) {
        try {
            return crudRepository.optional("FROM Engine WHERE id = :fId", Engine.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on find Engine ById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Engine> findAll() {
        try {
            return crudRepository.query("FROM Engine ORDER BY id", Engine.class);
        } catch (Exception e) {
            LOGGER.error("Exception on find Engine ById", e);
        }
        return Collections.emptyList();
    }
}
