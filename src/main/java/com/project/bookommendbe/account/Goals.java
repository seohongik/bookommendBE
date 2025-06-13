package com.project.bookommendbe.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;


/*컬럼명	데이터 타입	필수 여부	설명	예시
id	INTEGER (PK)	필수	예산 ID	1
user_id	INTEGER (FK)	필수	사용자 ID	101
category_id	INTEGER (FK)	필수	카테고리 ID	10 (식비)
month	VARCHAR(7)	필수	예산 설정 월 (YYYY-MM)	2025-06
amount_limit	DECIMAL(10,2)	필수	월간 예산 금액	300000.00
created_at	DATETIME	필수	예산 등록 시간	2025-05-31 12:00:00*/

@Data
@EqualsAndHashCode
@ToString
@Entity
public class Goals {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String coalsMonth; // YYYY-MM

    private LocalDate createdAt;
}

