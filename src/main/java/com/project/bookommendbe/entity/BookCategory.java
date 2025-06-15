package com.project.bookommendbe.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum BookCategory {
    GENERALITIES("000", "총류", "도서관 과학, 출판학, 정보학 등"),
    PHILOSOPHY("100", "철학", "철학, 심리학, 윤리학 등"),
    RELIGION("200", "종교", "종교, 신학, 미신 등"),
    SOCIAL_SCIENCE("300", "사회과학", "정치, 경제, 법, 교육 등"),
    NATURAL_SCIENCE("400", "자연과학", "수학, 물리학, 화학, 생물학 등"),
    TECHNOLOGY("500", "기술과학", "공학, 농업, 건축 등"),
    FINE_ARTS("600", "예술", "음악, 미술, 연극, 영화 등"),
    LANGUAGE("700", "언어", "언어학, 문법, 사전 등"),
    LITERATURE("800", "문학", "소설, 시, 희곡 등"),
    HISTORY("900", "역사", "한국사, 세계사, 지역사 등");

    private final String code;
    private final String koreanTitle;
    private final String description;

    BookCategory(String code, String koreanTitle, String description) {
        this.code = code;
        this.koreanTitle = koreanTitle;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getKoreanTitle() {
        return koreanTitle;
    }

    public String getDescription() {
        return description;
    }

    public static BookCategory fromCode(String code) {
        for (BookCategory category : values()) {
            if (category.code.equals(code)) {
                return category;
            }
        }
        return null;
    }
}
