package com.project.bookommendbe.service.searchTag;

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

    //private final Map<String,QnaService> qnaServiceMap;
    private final ApplicationContext acc;

    @Autowired
    public QnaController( ApplicationContext acc) {
       // this.qnaServiceMap = qnaServiceMap;
        this.acc = acc;
    }


    @GetMapping("/r1/qna")
    public Map<String,Map<String,String>> qna(@RequestParam  String title, @RequestParam  String body ) {

        Map<String,Map<String,String>> ans = new HashMap<>();
        if(false){ // 조건문 로직 짜야함 구글 네이버는 불법이기에 일단 false
            ans.put("goggle",getAnswer(title,body,"qnaServiceGoogleImpl"));
        }else if(false){
            ans.put("naver",getAnswer(title,body,"qnaServiceNaverImpl"));
        }else {
            ans.put("wiki",getAnswer(title,body,"qnaServiceWikiImpl"));
        }

        System.out.println("ans = " + ans);
        return ans;

    }

    public  Map<String, String> getAnswer(String title, String body,String beanName){
        QnaService qnaService=acc.getBean(beanName,QnaService.class);
        return qnaService.getQna(title,body);
    }

}