package com.project.bookommendbe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum RatingEnum {

    ONE(1, "매우 나쁨"),
    TWO(2, "나쁨"),
    THREE(3, "보통"),
    FOUR(4, "좋음"),
    FIVE(5, "매우 좋음");

    private final int value;
    private final String description;

    @JsonCreator
    RatingEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static RatingEnum fromValue(int value) {
        for (RatingEnum rating : values()) {
            if (rating.value == value) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating value: " + value);
    }
}