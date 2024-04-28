package pl.ambsoft.movieteka.exception.errors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCodes {

    ENTITY_DOES_NOT_EXIST("ENTITY_DOES_NOT_EXIST"),

    ENTITY_ALREADY_EXIST("ENTITY_ALREADY_EXIST"),

    WRONG_FORMAT("WRONG_FORMAT"),

    FIELD_ERROR("FIELD_ERROR"),

    DUPLICATE_NAME("DUPLICATE_NAME"),

    FILE_ERROR("FILE_ERROR");

    private final String message;
}
