package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Engine;
import ru.job4j.model.Owner;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmOwnerRepository implements OwnerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmOwnerRepository.class);
    private final CrudRepository crudRepository;

    @Override
    public Optional<Owner> findById(int id) {
        try {
            return crudRepository.optional(
                    "FROM Owner o JOIN FETCH o.user WHERE o.id = :fId", Owner.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on find Owner ById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Owner> findAll() {
        try {
            return crudRepository.query("FROM Owner o JOIN FETCH o.user ORDER BY o.id", Owner.class);
        } catch (Exception e) {
            LOGGER.error("Exception on find Owner ById", e);
        }
        return Collections.emptyList();
    }
}
