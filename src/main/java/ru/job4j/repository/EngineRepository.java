package ru.job4j.repository;

import ru.job4j.model.Engine;

import java.util.List;
import java.util.Optional;

public interface EngineRepository {
    Optional<Engine> findById(int id);

    List<Engine> findAll();
}
