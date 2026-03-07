package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@EqualsAndHashCode(of = { "id" })
@Data
@Getter
@Setter
public class User {
    Long id;
    String username;
    String email;
    String password;
    Instant registrationDate;
}
