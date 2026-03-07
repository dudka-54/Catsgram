package ru.yandex.practicum.catsgram.service;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) throws DuplicatedDataException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (userIsExist(user) && emailIsExist(user)) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    private boolean emailIsExist(@RequestBody User user) {
        return findAll()
                .stream()
                .anyMatch(user1 -> {
                    return user1.getEmail().equals(user.getEmail());
                });
    }

    private boolean userIsExist(@RequestBody User user) {
        return findAll().stream()
                .anyMatch(user1 -> {
                    return findAll().contains(user1);
                });
    }

    private boolean emailToUserIsExist(@RequestBody User user) {
        String email = user.getEmail();
        return findAll().stream()
                .anyMatch(user1 -> {
                    if (userIsExist(user)) {
                        if (!emailIsExist(user)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(findAll().stream()
                .filter(u -> u.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new ConditionsNotMetException("Автор с id = " + id + " не найден")));
}
    public User update(User newUser) throws NotFoundException, DuplicatedDataException {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if(emailToUserIsExist(newUser)){
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
                throw new ConditionsNotMetException("Имейл должен быть указан");
            }
            oldUser.setEmail(newUser.getEmail());
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(newUser.getPassword());

            return oldUser;
        }
        throw new NotFoundException("Пост с id = " + newUser.getId() + " не найден");
    }

}
