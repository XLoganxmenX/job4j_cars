package ru.job4j.repository;
import ru.job4j.model.CarModel;
import ru.job4j.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Optional<Post> findById(int id);

    List<Post> findAll();

    List<Post> findAllCreatedToday();

    List<Post> findByCarModel(CarModel carModel);
}
