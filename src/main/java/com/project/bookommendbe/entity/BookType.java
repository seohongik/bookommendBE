package com.project.bookommendbe.entity;

import lombok.Getter;

@Getter
public enum BookType {

    EBOOK("E"),
    PAPER("P");

    private final String description;

    BookType(String description) {
        this.description = description;
    }
}
