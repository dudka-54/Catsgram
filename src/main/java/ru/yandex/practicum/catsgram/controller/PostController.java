package ru.yandex.practicum.catsgram.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(required = false) Integer size,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam(required = false) Integer from) {
        if (sort.isBlank()) {
            throw new ParameterNotValidException("sort", "параметр sort должен содержать корректное значение");
        }
        if (size < 0) {
            throw new ParameterNotValidException("size", "\"Некорректный размер выборки. Размер должен быть больше нуля\"");
        }
        if (from < 0) {
            throw new ParameterNotValidException("from", "параметр from не может быть меньше нуля");
        }
        return postService.findAll(size, sort, from);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Post getPostOnId(@PathVariable long id) {
        Optional<Post> post = postService.findPostById(id);
        if (post.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return post.orElse(null);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) throws NotFoundException {
        return postService.update(newPost);
    }
}
