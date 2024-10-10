package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.PriceHistory;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class HbmPriceHistoryRepository implements PriceHistoryRepository {
    private final CrudRepository crudRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmPriceHistoryRepository.class);

    @Override
    public PriceHistory save(PriceHistory priceHistory) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.persist(priceHistory));
        } catch (Exception e) {
            LOGGER.error("Exception on save PriceHistory", e);
        }
        return priceHistory;
    }

    @Override
    public Optional<PriceHistory> findById(int id) {
        try {
            return crudRepository.optional(
                    "FROM PriceHistory WHERE id = :fId", PriceHistory.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on find PriceHistory ById", e);
        }
        return Optional.empty();
    }
}
