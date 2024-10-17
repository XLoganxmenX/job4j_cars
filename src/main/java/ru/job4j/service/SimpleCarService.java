package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.dto.CreatePagePostDto;
import ru.job4j.model.Car;
import ru.job4j.model.Owner;
import ru.job4j.model.User;
import ru.job4j.repository.CarRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class SimpleCarService implements CarService {
    private final CarRepository carRepository;
    private final CarModelService carModelService;
    private final EngineService engineService;
    private final OwnerService ownerService;

    @Override
    public Car save(Car car) throws SQLException {
        return carRepository.save(car);
    }

    @Override
    public Car createCarFromPost(User user, CreatePagePostDto postDto) throws SQLException {
        var engine = engineService.findById(postDto.getEngineId())
                .orElseThrow(() -> new IllegalArgumentException("Двигатель не найден"));
        var carModel = carModelService.findById(postDto.getCarModelId())
                .orElseThrow(() -> new IllegalArgumentException("Модель авто не найдена"));
        var owner = new Owner(0, user.getName(), user);
        ownerService.save(owner);
        var car = new Car(0, postDto.getCarName(), engine, carModel, Set.of(owner));

        return save(car);
    }

    @Override
    public boolean update(Car car) {
        return carRepository.update(car);
    }

    @Override
    public boolean delete(int id) {
        return carRepository.delete(id);
    }

    @Override
    public Optional<Car> findById(int id) {
        return carRepository.findById(id);
    }

    @Override
    public List<Car> findAllOrderById() {
        return carRepository.findAllOrderById();
    }
}
