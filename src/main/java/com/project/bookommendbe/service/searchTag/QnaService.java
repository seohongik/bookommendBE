package com.project.bookommendbe.service.searchTag;

import org.springframework.stereotype.Service;

import java.util.Map;

public interface QnaService {
    Map<String, String> getQna(String title, String body);
}
