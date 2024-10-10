package ru.job4j.repository;

import ru.job4j.model.CarModel;

import java.util.List;
import java.util.Optional;

public interface CarModelRepository {
    Optional<CarModel> findById(int id);

    List<CarModel> findAll();
}
