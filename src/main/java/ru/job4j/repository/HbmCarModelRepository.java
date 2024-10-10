package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.CarModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmCarModelRepository implements CarModelRepository {
    private final CrudRepository crudRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmCarModelRepository.class);

    @Override
    public Optional<CarModel> findById(int id) {
        try {
            return crudRepository.optional("FROM CarModel WHERE id = :fId", CarModel.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on find CarModel ById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<CarModel> findAll() {
        try {
            return crudRepository.query("FROM CarModel ORDER BY id", CarModel.class);
        } catch (Exception e) {
            LOGGER.error("Exception on find CarModel ById", e);
        }
        return Collections.emptyList();
    }
}
