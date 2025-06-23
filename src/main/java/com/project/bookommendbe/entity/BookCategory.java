package com.project.bookommendbe.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum BookCategory {
    GENERALITIES(0, "총류", "도서관 과학, 출판학, 정보학 등", "GENERALITIES"),
    PHILOSOPHY(1, "철학", "철학, 심리학, 윤리학 등","PHILOSOPHY"),
    RELIGION(2, "종교", "종교, 신학, 미신 등","RELIGION"),
    SOCIAL_SCIENCE(3, "사회과학", "정치, 경제, 법, 교육 등","SOCIAL_SCIENCE"),
    NATURAL_SCIENCE(4, "자연과학", "수학, 물리학, 화학, 생물학 등","NATURAL_SCIENCE"),
    TECHNOLOGY(5, "기술과학", "공학, 농업, 건축 등","TECHNOLOGY"),
    FINE_ARTS(6, "예술", "음악, 미술, 연극, 영화 등","FINE_ARTS"),
    LANGUAGE(7, "언어", "언어학, 문법, 사전 등","LANGUAGE"),
    LITERATURE(8, "문학", "소설, 시, 희곡 등","LITERATURE"),
    HISTORY(9, "역사", "한국사, 세계사, 지역사 등","HISTORY");

    private final int code;
    private final String koreanTitle;
    private final String description;
    private final String category;

    BookCategory(int code, String koreanTitle, String description, String category) {
        this.code = code;
        this.koreanTitle = koreanTitle;
        this.description = description;
        this.category = category;
    }

    public int getCode() {
        return code;
    }

    public String getKoreanTitle() {
        return koreanTitle;
    }

    public String getDescription() {
        return description;
    }

    public static BookCategory fromCode(int code) {
        for (BookCategory category : values()) {
            if (category.code==(code)) {
                return category;
            }
        }
        return null;
    }
}
