package ru.job4j.service;

import ru.job4j.dto.FileDto;
import ru.job4j.model.Car;
import ru.job4j.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarService {
    Car save(Car car);

    Car createCar(User user, String carName, int engineId, int carModelId);

    boolean update(Car car);

    boolean delete(int id);

    Optional<Car> findById(int id);

    List<Car> findAllOrderById();
}
