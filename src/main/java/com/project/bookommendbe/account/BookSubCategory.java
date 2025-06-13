package com.project.bookommendbe.account;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 가계부의 세부 항목 정의 Enum
 * 각 항목은 CategoryType(대분류)과 연결되어 있음
 */
public enum BookSubCategory {

    // --- FICTION 소설 ---
    ROMANCE(BookCategory.FICTION, "로맨스 소설"),
    FANTASY(BookCategory.FICTION, "판타지 소설"),
    SCIENCE_FICTION(BookCategory.FICTION, "SF 소설"),
    MYSTERY(BookCategory.FICTION, "미스터리, 스릴러"),
    HISTORICAL(BookCategory.FICTION, "역사 소설"),
    HORROR(BookCategory.FICTION, "공포, 호러"),
    ESSAY(BookCategory.FICTION, "에세이"),
    POETRY(BookCategory.FICTION, "시/시집"),
    PLAY(BookCategory.FICTION, "희곡"),
    GRAPHIC_NOVEL(BookCategory.FICTION, "만화, 그래픽 노블"),

    // --- NON_FICTION 비문학 ---
    SELF_HELP(BookCategory.NON_FICTION, "자기계발"),
    HUMANITIES(BookCategory.NON_FICTION, "인문학"),
    PHILOSOPHY(BookCategory.NON_FICTION, "철학"),
    HISTORY(BookCategory.NON_FICTION, "역사"),
    SOCIETY_POLITICS(BookCategory.NON_FICTION, "사회/정치"),
    ECONOMY_BUSINESS(BookCategory.NON_FICTION, "경제/경영"),
    SCIENCE(BookCategory.NON_FICTION, "과학"),
    TECHNOLOGY_IT(BookCategory.NON_FICTION, "기술/IT"),
    ART_DESIGN(BookCategory.NON_FICTION, "예술/디자인"),
    RELIGION_PSYCHOLOGY(BookCategory.NON_FICTION, "종교, 심리, 명상"),
    TRAVEL(BookCategory.NON_FICTION, "여행"),
    HEALTH_MEDICAL(BookCategory.NON_FICTION, "건강/의학"),
    COOKING(BookCategory.NON_FICTION, "요리"),
    PARENTING(BookCategory.NON_FICTION, "육아/가정"),
    EDUCATION(BookCategory.NON_FICTION, "교육/학습법"),

    // --- TARGET 대상 독자 ---
    ADULT(BookCategory.TARGET, "성인 대상 도서"),
    TEENAGER(BookCategory.TARGET, "청소년 대상 도서"),
    CHILD(BookCategory.TARGET, "아동 대상 도서"),
    TODDLER(BookCategory.TARGET, "유아 대상 도서"),

    // --- FORMAT 책 형태 ---
    PAPER(BookCategory.FORMAT, "종이책"),
    EBOOK(BookCategory.FORMAT, "전자책"),
    AUDIOBOOK(BookCategory.FORMAT, "오디오북"),

    // --- SPECIAL 특별 분류 ---
    BESTSELLER(BookCategory.SPECIAL, "베스트셀러"),
    NEW_RELEASE(BookCategory.SPECIAL, "신간"),
    CLASSIC(BookCategory.SPECIAL, "고전"),
    AWARD_WINNER(BookCategory.SPECIAL, "수상작"),
    RECOMMENDED(BookCategory.SPECIAL, "추천 도서"),
    TRANSLATED(BookCategory.SPECIAL, "번역서"),
    ORIGINAL(BookCategory.SPECIAL, "원서"),
    SERIES(BookCategory.SPECIAL, "시리즈물"),
    STANDALONE(BookCategory.SPECIAL, "단행본");

    private final BookCategory category;     // 대분류
    private final String description;        // 설명 문자열

    @JsonCreator
    BookSubCategory(BookCategory category, String description) {
        this.category = category;
        this.description = description;
    }

    /**
     * 소속된 대분류 반환
     */
    public BookCategory getCategory() {
        return category;
    }

    /**
     * 사용자에게 보여줄 설명 반환
     */
    public String getDescription() {
        return description;
    }
}
