package ru.yandex.practicum.catsgram.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParameterNotValidException extends IllegalArgumentException {
    private String parameter;
    private String reason;
}
