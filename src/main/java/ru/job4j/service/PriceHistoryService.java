package ru.job4j.service;

import ru.job4j.model.PriceHistory;

import java.util.Optional;

public interface PriceHistoryService {
    PriceHistory save(PriceHistory priceHistory);

    Optional<PriceHistory> findById(int id);
}
