package com.project.bookommendbe.service.searchTag;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class QnaController {

    private final Map<String,QnaService> qnaServiceMap;

    @Autowired
    public QnaController(Map<String,QnaService> qnaServiceMap) {
        this.qnaServiceMap = qnaServiceMap;
    }


    @GetMapping("/r1/qna")
    public Map<String,Map<String,String>> qna(@RequestParam  String title, @RequestParam  String body ) {

        Map<String,Map<String,String>> ans = new HashMap<>();
        if(false){ // 조건문 로직 짜야함 구글 네이버는 불법이기에 일단 false
            ans.put("goggle",getAnswer(title,body, "goggle"));
        }

        if(true){
            ans.put("naver",getAnswer(title,body, "naver"));
        }
        if(true){
            ans.put("wiki",getAnswer(title,body,"wiki"));
        }

        return ans;

    }

    public  Map<String, String> getAnswer(String title, String body,String mapKey){
        QnaService qnaService=qnaServiceMap.get(mapKey);
        return qnaService.getQna(title,body);

    }

}