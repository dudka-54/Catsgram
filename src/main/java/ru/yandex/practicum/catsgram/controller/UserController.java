package ru.yandex.practicum.catsgram.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.service.UserService;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/users")
class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll(@RequestParam(required = false) int size,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam(required = false) int from) {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public User getUserOnId(@PathVariable long id){
        Optional<User> user = userService.findUserById(id);
        if(user.isEmpty()){
            throw new NoSuchElementException();
        } else {
            return user.get();
        }
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@RequestBody User user) throws DuplicatedDataException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) throws NotFoundException, DuplicatedDataException {
        return userService.update(newUser);
    }
}
