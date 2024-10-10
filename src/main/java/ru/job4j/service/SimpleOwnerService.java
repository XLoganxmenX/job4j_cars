package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Owner;
import ru.job4j.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleOwnerService implements OwnerService {
    private final OwnerRepository ownerRepository;

    @Override
    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public Optional<Owner> findById(int id) {
        return ownerRepository.findById(id);
    }

    @Override
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }
}
