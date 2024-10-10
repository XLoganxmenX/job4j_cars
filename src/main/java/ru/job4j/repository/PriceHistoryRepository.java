package ru.job4j.repository;


import ru.job4j.model.PriceHistory;

import java.util.Optional;

public interface PriceHistoryRepository {
    PriceHistory save(PriceHistory priceHistory);

    Optional<PriceHistory> findById(int id);
}
