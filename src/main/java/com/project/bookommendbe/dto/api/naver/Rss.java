package com.project.bookommendbe.dto.api.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Rss {
    @JsonProperty
    private Channel channel;

}
