package com.project.bookommendbe.util;

public class AuthNumber {
    private static final AuthNumber instance = new AuthNumber();
    private final int number;

    // private 생성자
    private AuthNumber() {
        number = (int)(Math.random() * 900000) + 100000;
    }

    // 정적 메서드를 통해 인스턴스 제공
    public static AuthNumber getInstance() {
        return instance;
    }

    public int getAuthNumber() {
        return number;
    }
}
