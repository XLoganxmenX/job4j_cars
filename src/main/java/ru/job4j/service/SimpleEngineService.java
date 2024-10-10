package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Engine;
import ru.job4j.repository.EngineRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleEngineService implements EngineService {
    private final EngineRepository engineRepository;

    @Override
    public Optional<Engine> findById(int id) {
        return engineRepository.findById(id);
    }

    @Override
    public List<Engine> findAll() {
        return engineRepository.findAll();
    }
}
