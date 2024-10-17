package ru.job4j.service;

import ru.job4j.dto.CreatePagePostDto;
import ru.job4j.dto.FileDto;
import ru.job4j.dto.ListPagePostDto;
import ru.job4j.dto.OnePagePostDto;
import ru.job4j.model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostService {
    Post save(Post post) throws SQLException;

    Optional<Post> findById(int id);

    OnePagePostDto findPostDtoById(int id);

    List<Post> findAll();

    List<Post> findAllCreatedToday();

    List<ListPagePostDto> findPostDtoByCarModel(CarModel carModel);

    List<ListPagePostDto> findPostDtoByEngine(Engine engine);

    List<Post> findAllWithFiles();

    List<ListPagePostDto> findAllListPagePostDto();

    Post createNewPost(User user, CreatePagePostDto postDto, Car car, List<FileDto> filesDto) throws SQLException;

    boolean sellPostById(int id);
}
