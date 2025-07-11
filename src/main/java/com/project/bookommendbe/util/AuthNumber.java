package com.project.bookommendbe.util;

public class AuthNumber {
    private final int number;
    public AuthNumber() {
        number = (int)(Math.random() * 900000) + 100000;
    }

    public int getNumber() {
        return number;
    }
}
