package com.project.bookommendbe.service.user;

public class UserException extends RuntimeException {
    private final String message;
    public UserException(String message) {
        super(message);
        this.message = message;
    }
}
