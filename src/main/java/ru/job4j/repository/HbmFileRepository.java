package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.File;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class HbmFileRepository implements FileRepository {
    private final CrudRepository crudRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmFileRepository.class);

    @Override
    public File save(File file) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.persist(file));
        } catch (Exception e) {
            LOGGER.error("Exception on save File", e);
        }
        return file;
    }

    @Override
    public Optional<File> findById(int id) {
        try {
            return crudRepository.optional(
                    "FROM File WHERE id = :fId", File.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on find File ById", e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(int id) {
        try {
            crudRepository.query("DELETE FROM File WHERE id = :fId", File.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on delete File ById", e);
        }
    }
}
