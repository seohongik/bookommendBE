package com.project.bookommendbe.service.searchTag;

import java.util.Map;

public interface QnaService {
    Map<String, String> getQna(String title, String body);
}
