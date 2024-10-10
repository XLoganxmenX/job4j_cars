package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.dto.FileDto;
import ru.job4j.dto.ListPagePostDto;
import ru.job4j.dto.OnePagePostDto;
import ru.job4j.mappers.PostMapper;
import ru.job4j.model.*;
import ru.job4j.repository.PostRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PriceHistoryService priceHistoryService;
    private final FileService fileService;

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public OnePagePostDto findPostDtoById(int id) {
        var post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));
        return postMapper.getOnePagePostDtoFromPost(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllCreatedToday() {
        return postRepository.findAllCreatedToday();
    }

    @Override
    public List<ListPagePostDto> findPostDtoByCarModel(CarModel carModel) {
        var posts = postRepository.findByCarModel(carModel);
        return posts.stream().map(postMapper::getListPagePostDtoFromPost).toList();
    }

    @Override
    public List<ListPagePostDto> findPostDtoByEngine(Engine engine) {
        var posts = postRepository.findByEngine(engine);
        return posts.stream().map(postMapper::getListPagePostDtoFromPost).toList();
    }

    @Override
    public List<Post> findAllWithFiles() {
        return postRepository.findAllWithFiles();
    }

    @Override
    public List<ListPagePostDto> findAllListPagePostDto() {
        var posts = postRepository.findAll();
        return posts.stream().map(postMapper::getListPagePostDtoFromPost).toList();
    }

    @Override
    public Post createNewPost(User user, String description, LocalDateTime created,
                              int price, Car car, List<FileDto> filesDto, boolean sold) {

        var priceHistory = new PriceHistory(0, BigInteger.valueOf(0), BigInteger.valueOf(price), created);
        priceHistoryService.save(priceHistory);
        var files = filesDto.stream().map(fileService::save).toList();

        var post = new Post(0, description, created, user, List.of(priceHistory), List.of(user), car, files, sold);
        return postRepository.save(post);
    }

    @Override
    public boolean sellPostById(int id) {
        return postRepository.sellPostById(id);
    }
}
