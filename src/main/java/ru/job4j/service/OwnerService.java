package ru.job4j.service;

import ru.job4j.model.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerService {
    Owner save(Owner owner);

    Optional<Owner> findById(int id);

    List<Owner> findAll();
}
