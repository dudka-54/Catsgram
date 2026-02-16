package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.Instant;

@EqualsAndHashCode(of = { "id" })
@Data
public class Post {
    Long id;
    long authorId;
    String description;
    Instant postDate;
}
