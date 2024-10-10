package ru.job4j.repository;

import ru.job4j.model.File;

import java.util.Optional;

public interface FileRepository {
    File save(File fileDto);

    Optional<File> findById(int id);

    void deleteById(int id);
}
