package com.project.bookommendbe.dto.api.library;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    @JsonProperty
    private String totalCount;
    @JsonProperty
    private String pageNo;
    @JsonProperty
    private List<Doc> docs;
}
