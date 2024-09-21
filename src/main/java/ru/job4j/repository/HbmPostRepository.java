package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.CarModel;
import ru.job4j.model.File;
import ru.job4j.model.Post;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class HbmPostRepository implements PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmPostRepository.class);
    private final CrudRepository crudRepository;

    @Override
    public Post save(Post post) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.persist(post));
        } catch (Exception e) {
            LOGGER.error("Exception on save Post", e);
        }
        return post;
    }

    @Override
    public Optional<Post> findById(int id) {
        try {
            var optionalPost = crudRepository.optional("""
                    SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.user
                    JOIN FETCH p.priceHistory
                    JOIN FETCH p.car c
                        JOIN FETCH c.engine
                        JOIN FETCH c.owners
                        JOIN FETCH c.carModel
                    WHERE p.id = :fId
                    """, Post.class, Map.of("fId", id));
            if (optionalPost.isPresent()) {
                var post = optionalPost.get();
                post.setParticipates(crudRepository.optional("""
                    SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.participates
                    WHERE p IN :fPost
                    """, Post.class, Map.of("fPost", post)).get().getParticipates());
                var postFiles = crudRepository.optional("""
                    SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.files
                    WHERE p IN :fPost
                    """,
                        Post.class,
                        Map.of("fPost", post)
                );

                List<File> files = List.of();
                if (postFiles.isPresent()) {
                    files = postFiles.get().getFiles();
                }
                post.setFiles(files);
            }
            return optionalPost;
        } catch (Exception e) {
            LOGGER.error("Exception on find Post ById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Post> findAll() {
        try {
            var posts = crudRepository.query("""
                    SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.user
                    JOIN FETCH p.priceHistory
                    JOIN FETCH p.car c
                        JOIN FETCH c.engine
                        JOIN FETCH c.owners
                        JOIN FETCH c.carModel
                    """,
                    Post.class
            );
            loadPostDetails(posts);

            return posts;
        } catch (Exception e) {
            LOGGER.error("Exception on findAll Post", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Post> findAllCreatedToday() {
        try {
            var today = LocalDate.now().atStartOfDay();
            var posts = crudRepository.query("""
                    SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.user
                    JOIN FETCH p.priceHistory
                    JOIN FETCH p.car c
                        JOIN FETCH c.engine
                        JOIN FETCH c.owners
                        JOIN FETCH c.carModel
                    WHERE p.created > :fToday
                    """,
                    Post.class,
                    Map.of("fToday", today)
            );
            loadPostDetails(posts);

            return posts;
        } catch (Exception e) {
            LOGGER.error("Exception on findAll Post CreatedToday", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Post> findByCarModel(CarModel carModel) {
        try {
            var posts = crudRepository.query("""
                    SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.user
                    JOIN FETCH p.priceHistory
                    JOIN FETCH p.car c
                        JOIN FETCH c.engine
                        JOIN FETCH c.owners
                        JOIN FETCH c.carModel m
                    WHERE m IN :fCarModel
                    """,
                    Post.class,
                    Map.of("fCarModel", carModel)
            );
            loadPostDetails(posts);

            return posts;
        } catch (Exception e) {
            LOGGER.error("Exception on find Post ByCarModel ", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Post> findAllWithFiles() {
        try {
            var posts = crudRepository.query("""
                    SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.user
                    JOIN FETCH p.files f
                    JOIN FETCH p.car c
                        JOIN FETCH c.engine
                        JOIN FETCH c.owners
                        JOIN FETCH c.carModel m
                    WHERE f IS NOT NULL
                    """,
                    Post.class
            );
            var postsParticipates = crudRepository.query("""
                    SELECT DISTINCT p FROM Post p
                    LEFT JOIN FETCH p.participates
                    WHERE p IN :fPost
                    """,
                    Post.class,
                    Map.of("fPost", posts)
            );
            var postPriceHistory = crudRepository.query("""
                    SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.priceHistory
                    WHERE p IN :fPost
                    """,
                    Post.class,
                    Map.of("fPost", posts)
            );
            var participatesMap = postsParticipates.stream().collect(Collectors.toMap(Post::getId, post -> post));
            var priceHistoryMap = postPriceHistory.stream().collect(Collectors.toMap(Post::getId, post -> post));
            posts.forEach(post -> {
                post.setParticipates(participatesMap.get(post.getId()).getParticipates());
                post.setPriceHistory(priceHistoryMap.get(post.getId()).getPriceHistory());
            });

            return posts;
        } catch (Exception e) {
            LOGGER.error("Exception on findAll Post WithFiles", e);
        }
        return Collections.emptyList();
    }

    private void loadPostDetails(List<Post> posts) throws Exception {
        var postsParticipates = crudRepository.query("""
                    SELECT DISTINCT p FROM Post p
                    LEFT JOIN FETCH p.participates
                    WHERE p IN :fPost
                    """,
                Post.class,
                Map.of("fPost", posts)
        );
        var postsFiles = crudRepository.query("""
                    SELECT DISTINCT p FROM Post p
                    LEFT JOIN FETCH p.files
                    WHERE p IN :fPost
                    """,
                Post.class,
                Map.of("fPost", posts)
        );
        var participatesMap = postsParticipates.stream().collect(Collectors.toMap(Post::getId, post -> post));
        var filesMap = postsFiles.stream().collect(Collectors.toMap(Post::getId, post -> post));
        posts.forEach(post -> {
            post.setParticipates(participatesMap.get(post.getId()).getParticipates());
            post.setFiles(filesMap.get(post.getId()).getFiles());
        });
    }
}
