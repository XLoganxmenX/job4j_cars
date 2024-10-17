package ru.job4j.service;

import ru.job4j.dto.CreatePagePostDto;
import ru.job4j.model.Car;
import ru.job4j.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CarService {
    Car save(Car car) throws SQLException;

    Car createCarFromPost(User user, CreatePagePostDto postDto) throws SQLException;

    boolean update(Car car);

    boolean delete(int id);

    Optional<Car> findById(int id);

    List<Car> findAllOrderById();
}
