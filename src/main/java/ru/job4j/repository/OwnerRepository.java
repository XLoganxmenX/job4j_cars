package ru.job4j.repository;
import ru.job4j.model.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository {
    Owner save(Owner owner);

    Optional<Owner> findById(int id);

    List<Owner> findAll();
}
