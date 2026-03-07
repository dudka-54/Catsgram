package ru.yandex.practicum.catsgram.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @RequestMapping("/home")
    public String homePage() {
        return "<h1>Приветствуем вас в приложении Котограм<h1>";
    }
}

