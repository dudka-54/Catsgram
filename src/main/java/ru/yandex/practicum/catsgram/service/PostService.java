package ru.yandex.practicum.catsgram.service;

import javassist.NotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();

    private final UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(Integer size,
                                    String sort,
                                    Integer from) {
        boolean isSizeNull = false;
        boolean isSortNull = false;
        boolean isFromNull = false;

        if(isFromNull && isSizeNull && isSortNull){
            return new ArrayList<>(posts.values()).stream()
                    .sorted(Comparator.comparing(Post::getPostDate).reversed())
                    .limit(9)
                    .collect(Collectors.toList());
        }
        if(size == null){
            isSizeNull = true;
        }
        if(sort == null){
            isSortNull = true;
        }
        if(from == null){
            isFromNull = true;
        }

        List<Post> postList = new ArrayList<>(posts.values());
        if(!isSortNull){
            SortOrder sortOrder = SortOrder.from(sort);
            if(sortOrder == SortOrder.ASCENDING){
                postList.sort(Comparator.comparing(Post::getPostDate));
            } else {
                postList.sort(Comparator.comparing(Post::getPostDate).reversed());
            }
        }
        if(!isFromNull){
            if (from < 1) {
                throw new IllegalArgumentException("from должен быть >= 1");
            }
            if (from - 1 >= postList.size()) {
                return Collections.emptyList();
            }
            if(isSizeNull) {
                postList = postList.subList(from - 1, postList.size());
            } else {
                postList = postList.subList(from - 1, size - 1);
            }
        }
    return postList;
    }

    public enum SortOrder {
        ASCENDING, DESCENDING;

        public static SortOrder from(String order) {
            switch (order.toLowerCase()) {
                case "ascending":
                case "asc":
                    return ASCENDING;
                case "descending":
                case "desc":
                    return DESCENDING;
                default: return null;
            }
        }
    }
    private Collection<Post> findAll() {
        return posts.values();
    }


    public Optional<Post> findPostById(long id) {
        return Optional.ofNullable(findAll().stream()
                .filter(u -> u.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new ConditionsNotMetException("Автор с id = " + id + " не найден")));
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        userService.findUserById(post.getAuthorId());
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) throws NotFoundException {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
