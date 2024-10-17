package ru.job4j.repository;


import ru.job4j.model.PriceHistory;

import java.sql.SQLException;
import java.util.Optional;

public interface PriceHistoryRepository {
    PriceHistory save(PriceHistory priceHistory) throws SQLException;

    Optional<PriceHistory> findById(int id);
}
