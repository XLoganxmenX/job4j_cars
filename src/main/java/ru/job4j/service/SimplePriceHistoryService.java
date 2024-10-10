package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.PriceHistory;
import ru.job4j.repository.PriceHistoryRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePriceHistoryService implements PriceHistoryService {
    private final PriceHistoryRepository priceHistoryRepository;

    @Override
    public PriceHistory save(PriceHistory priceHistory) {
        return priceHistoryRepository.save(priceHistory);
    }

    @Override
    public Optional<PriceHistory> findById(int id) {
        return priceHistoryRepository.findById(id);
    }
}
