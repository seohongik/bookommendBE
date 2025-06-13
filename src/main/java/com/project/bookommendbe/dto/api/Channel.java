package com.project.bookommendbe.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Channel {
    @JsonProperty
    private String title;
    @JsonProperty
    private String link;
    @JsonProperty
    private String description;
    @JsonProperty
    private String lastBuildDate; // 생성 시간
    @JsonProperty
    private int total;      // 총 검색 결과 수
    @JsonProperty
    private int start;      // 검색 시작 위치
    @JsonProperty
    private int display;    // 표시 결과 수
    @JsonProperty
    private List<Item> items;
}