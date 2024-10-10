package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> create(User user) {
        return userRepository.create(user);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void delete(int userId) {
        userRepository.delete(userId);
    }

    @Override
    public List<User> findAllOrderById() {
        return userRepository.findAllOrderById();
    }

    @Override
    public Optional<User> findById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findByLikeLogin(String key) {
        return userRepository.findByLikeLogin(key);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
}
