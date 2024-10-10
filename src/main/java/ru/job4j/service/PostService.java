package ru.job4j.service;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dto.FileDto;
import ru.job4j.dto.ListPagePostDto;
import ru.job4j.dto.OnePagePostDto;
import ru.job4j.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostService {
    Post save(Post post);

    Optional<Post> findById(int id);

    OnePagePostDto findPostDtoById(int id);

    List<Post> findAll();

    List<Post> findAllCreatedToday();

    List<ListPagePostDto> findPostDtoByCarModel(CarModel carModel);

    List<ListPagePostDto> findPostDtoByEngine(Engine engine);

    List<Post> findAllWithFiles();

    List<ListPagePostDto> findAllListPagePostDto();

    Post createNewPost(User user, String description, LocalDateTime created,
                       int price, Car car, List<FileDto> filesDto, boolean sold);

    boolean sellPostById(int id);
}
