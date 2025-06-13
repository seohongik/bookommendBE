package com.project.bookommendbe.account;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum BookCategory {
    FICTION,        // 소설 및 창작물
    NON_FICTION,    // 비문학 (실용서, 인문서 등)
    TARGET,         // 대상 독자 (성인, 아동 등)
    FORMAT,         // 책의 형태 (전자책, 종이책 등)
    SPECIAL         // 특별 분류 (베스트셀러, 신간 등)
}
