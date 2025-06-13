package com.project.bookommendbe.account;

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
